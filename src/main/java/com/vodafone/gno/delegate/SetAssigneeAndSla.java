package com.vodafone.gno.delegate;

import com.vodafone.gno.application.service.jira.JiraIssueService;
import com.vodafone.gno.dto.ApiResponseDto;
import org.springframework.stereotype.Component;
import org.vodafone.kramax.bpm.engine.delegate.BpmnError;
import org.vodafone.kramax.bpm.engine.delegate.DelegateExecution;
import org.vodafone.kramax.bpm.engine.delegate.JavaDelegate;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Component
public class SetAssigneeAndSla implements JavaDelegate {

    private static final String RETRY_MODE_VARIABLE = "assignee_sla_retry_mode";
    private static final String RETRY_MODE_BOUNDARY = "BOUNDARY_TIMER";
    private static final String RETRY_MAX_VARIABLE = "assignee_sla_retry_max_attempts";
    private static final String RETRY_COUNTER_VARIABLE = "assignee_sla_retry_counter_var";
    private static final String DEFAULT_COUNTER_VARIABLE = "assignAndSlaRetryCount";
    private static final String LAST_ERROR_VARIABLE = "assignee_sla_last_error";
    private static final String LAST_ATTEMPT_VARIABLE = "assignee_sla_last_attempt";
    private static final String RETRY_ERROR_CODE = "ERR_ASSIGN_SLA_RETRY";
    private static final String FINAL_ERROR_CODE = "ERR_ASSIGN_SLA_FINAL";

    private final JiraIssueService jiraIssueService;

    public SetAssigneeAndSla(JiraIssueService jiraIssueService) {
        this.jiraIssueService = jiraIssueService;
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String assignee = (String) execution.getVariable("assigneeName");
        Date sla_date = (Date) execution.getVariable("sla_date");
        String businessKey = execution.getProcessBusinessKey();
        String retryMode = asString(execution.getVariable(RETRY_MODE_VARIABLE));
        String retryCounterVar = asString(execution.getVariable(RETRY_COUNTER_VARIABLE));
        int maxRetries = asInt(execution.getVariable(RETRY_MAX_VARIABLE), 0);

        System.out.println("[SetAssigneeAndSla] Start for ticket: " + businessKey);
        System.out.println("[SetAssigneeAndSla] Retry mode=" + retryMode + ", maxRetries=" + maxRetries);

        if (retryCounterVar == null || retryCounterVar.trim().isEmpty()) {
            retryCounterVar = DEFAULT_COUNTER_VARIABLE;
        }

        if (businessKey == null || businessKey.trim().isEmpty()) {
            throw new IllegalArgumentException("Missing business key while assigning Jira ticket");
        }

        if (assignee == null || assignee.trim().isEmpty()) {
            throw new IllegalArgumentException("Missing assigneeName while assigning Jira ticket");
        }

        if (sla_date == null) {
            throw new IllegalArgumentException("Missing sla_date while assigning Jira ticket");
        }

        String formattedDate = sla_date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        ApiResponseDto assigneeResponse = jiraIssueService.updateAssignee(businessKey, assignee);
        if (!assigneeResponse.isSuccess()) {
            System.out.println("[SetAssigneeAndSla] Assignee update failed: " + assigneeResponse.getMessage());
            handleFailure(execution, retryMode, retryCounterVar, maxRetries,
                    messageOrDefault(assigneeResponse.getMessage(), "Unknown Jira assignee update failure"),
                    businessKey);
            return;
        }

        ApiResponseDto dueDateResponse = jiraIssueService.updateEndDate(businessKey, formattedDate);
        if (!dueDateResponse.isSuccess()) {
            System.out.println("[SetAssigneeAndSla] Due date update failed: " + dueDateResponse.getMessage());
            handleFailure(execution, retryMode, retryCounterVar, maxRetries,
                    messageOrDefault(dueDateResponse.getMessage(), "Unknown Jira due date update failure"),
                    businessKey);
            return;
        }

        System.out.println("[SetAssigneeAndSla] Assignee and SLA update succeeded for ticket: " + businessKey);
        execution.setVariable(retryCounterVar, 0);
        execution.setVariable(LAST_ERROR_VARIABLE, null);
    }

    private void handleFailure(DelegateExecution execution,
                               String retryMode,
                               String retryCounterVar,
                               int maxRetries,
                               String failureMessage,
                               String businessKey) {
        execution.setVariable(LAST_ERROR_VARIABLE, failureMessage);
        execution.setVariable(LAST_ATTEMPT_VARIABLE, new Date());

        if (RETRY_MODE_BOUNDARY.equalsIgnoreCase(retryMode)) {
            int retryCount = asInt(execution.getVariable(retryCounterVar), 0);
            if (retryCount < maxRetries) {
                System.out.println("[SetAssigneeAndSla] Retry " + (retryCount + 1) + " scheduled for ticket: " + businessKey);
                execution.setVariable(retryCounterVar, retryCount + 1);
                throw new BpmnError(RETRY_ERROR_CODE,
                        "Assign/SLA update failed for " + businessKey + ". Retrying attempt " + (retryCount + 1));
            }

            System.out.println("[SetAssigneeAndSla] Final failure after retries for ticket: " + businessKey);
            throw new BpmnError(FINAL_ERROR_CODE,
                    "Assign/SLA update failed for " + businessKey + " after " + retryCount + " retries");
        }

        throw new IllegalStateException("Failed to assign Jira ticket for " + businessKey + ": " + failureMessage);
    }

    private String messageOrDefault(String message, String defaultValue) {
        return message == null || message.trim().isEmpty() ? defaultValue : message;
    }

    private String asString(Object value) {
        return value == null ? null : String.valueOf(value);
    }

    private int asInt(Object value, int defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        try {
            return Integer.parseInt(String.valueOf(value));
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }
}
