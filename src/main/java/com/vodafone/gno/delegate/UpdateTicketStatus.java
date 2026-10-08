package com.vodafone.gno.delegate;

import com.vodafone.gno.application.service.jira.JiraIssueService;
import com.vodafone.gno.dto.ApiResponseDto;
import org.springframework.stereotype.Component;
import org.vodafone.kramax.bpm.engine.delegate.BpmnError;
import org.vodafone.kramax.bpm.engine.delegate.DelegateExecution;
import org.vodafone.kramax.bpm.engine.delegate.JavaDelegate;

@Component
public class UpdateTicketStatus implements JavaDelegate {

    private static final String RETRY_MODE_VARIABLE = "status_retry_mode";
    private static final String RETRY_MODE_BOUNDARY = "BOUNDARY_TIMER";
    private static final String RETRY_MAX_VARIABLE = "status_retry_max_attempts";
    private static final String RETRY_COUNTER_VARIABLE = "status_retry_counter_var";
    private static final String DEFAULT_COUNTER_VARIABLE = "setTicketInProgressRetryCount";
    private static final String LAST_ERROR_VARIABLE = "status_update_last_error";
    private static final String LAST_ATTEMPT_VARIABLE = "status_update_last_attempt";
    private static final String RETRY_ERROR_CODE = "ERR_STATUS_UPDATE_RETRY";
    private static final String FINAL_ERROR_CODE = "ERR_STATUS_UPDATE_FINAL";

    private final JiraIssueService jiraIssueService;

    public UpdateTicketStatus(JiraIssueService jiraIssueService) {
        this.jiraIssueService = jiraIssueService;
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String businessKey = execution.getProcessBusinessKey();
        String status = (String) execution.getVariable("status");
        String retryMode = asString(execution.getVariable(RETRY_MODE_VARIABLE));
        String retryCounterVar = asString(execution.getVariable(RETRY_COUNTER_VARIABLE));
        int maxRetries = asInt(execution.getVariable(RETRY_MAX_VARIABLE), 0);

        if (businessKey == null || businessKey.trim().isEmpty()) {
            throw new IllegalArgumentException("Missing business key while updating Jira ticket status");
        }

        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Missing status transition id for Jira status update");
        }

        if (retryCounterVar == null || retryCounterVar.trim().isEmpty()) {
            retryCounterVar = DEFAULT_COUNTER_VARIABLE;
        }

        ApiResponseDto response = jiraIssueService.changeIssueStatus(businessKey, status);
        if (response.isSuccess()) {
            execution.setVariable(retryCounterVar, 0);
            execution.setVariable(LAST_ERROR_VARIABLE, null);
            return;
        }

        String failureMessage = response.getMessage() != null
                ? response.getMessage()
                : "Unknown Jira status update failure";
        execution.setVariable(LAST_ERROR_VARIABLE, failureMessage);
        execution.setVariable(LAST_ATTEMPT_VARIABLE, new java.util.Date());

        if (RETRY_MODE_BOUNDARY.equalsIgnoreCase(retryMode)) {
            int retryCount = asInt(execution.getVariable(retryCounterVar), 0);
            if (retryCount < maxRetries) {
                execution.setVariable(retryCounterVar, retryCount + 1);
                throw new BpmnError(RETRY_ERROR_CODE,
                        "Status update failed for " + businessKey + ". Retrying attempt " + (retryCount + 1));
            }

            throw new BpmnError(FINAL_ERROR_CODE,
                    "Status update failed for " + businessKey + " after " + retryCount + " retries");
        }

        throw new IllegalStateException("Failed to change Jira status for " + businessKey + ": " + failureMessage);
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