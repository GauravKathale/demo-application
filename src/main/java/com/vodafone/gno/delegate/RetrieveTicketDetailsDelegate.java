package com.vodafone.gno.delegate;

import com.vodafone.gno.application.service.jira.JiraIssueService;
import com.vodafone.gno.dto.ApiResponseDto;
import org.springframework.stereotype.Component;
import org.vodafone.kramax.bpm.engine.delegate.DelegateExecution;
import org.vodafone.kramax.bpm.engine.delegate.JavaDelegate;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component("retrieveTicketDetailsDelegate")
public class RetrieveTicketDetailsDelegate implements JavaDelegate {

    private static final Pattern ASSIGNEE_AREA_PATTERN = Pattern.compile("Assignee\\s*Area\\s*:\\s*(.+)", Pattern.CASE_INSENSITIVE);
    private static final Pattern NE_TYPE_PATTERN = Pattern.compile("Network\\s*Element\\s*Type\\s*:\\s*(.+)", Pattern.CASE_INSENSITIVE);

    private final JiraIssueService jiraIssueService;

    public RetrieveTicketDetailsDelegate(JiraIssueService jiraIssueService) {
        this.jiraIssueService = jiraIssueService;
    }

    @Override
    public void execute(DelegateExecution execution) {
        String businessKey = execution.getProcessBusinessKey();
        System.out.println("[RetrieveTicketDetailsDelegate] Retrieving details for ticket: " + businessKey);

        if (businessKey == null || businessKey.trim().isEmpty()) {
            throw new IllegalArgumentException("Missing business key for Retrieve Ticket Details delegate");
        }

        ApiResponseDto response = jiraIssueService.fetchIssueDetailsById(businessKey);
        if (!response.isSuccess()) {
            throw new IllegalStateException("Failed to retrieve ticket details: " + response.getMessage());
        }

        Map<String, Object> data = (Map<String, Object>) response.getData();
        String description = data == null ? null : asString(data.get("description"));

        execution.setVariable("ticket_description", description);

        String assigneeArea = extract(ASSIGNEE_AREA_PATTERN, description);
        String neType = extract(NE_TYPE_PATTERN, description);

        execution.setVariable("assigneeArea", assigneeArea);
        execution.setVariable("neType", neType);

        if (isBlank(assigneeArea) || isBlank(neType)) {
            String missingFields = buildMissingFieldsMessage(assigneeArea, neType);
            String warningMessage = "Ticket details still incomplete after user update. Missing: " + missingFields;

            // Do not fail here; allow BPMN Extract + Gateway to route back to user task.
            execution.setVariable("missing_details_still_invalid", true);
            execution.setVariable("missing_details_reason", warningMessage);

            System.out.println("[RetrieveTicketDetailsDelegate] " + warningMessage + " for ticket: " + businessKey);
            return;
        }

        execution.setVariable("missing_details_still_invalid", false);
        execution.setVariable("missing_details_reason", null);

        System.out.println("[RetrieveTicketDetailsDelegate] Successfully extracted assigneeArea and neType for ticket: " + businessKey);
    }

    private String extract(Pattern pattern, String text) {
        if (isBlank(text)) {
            return null;
        }

        Matcher matcher = pattern.matcher(text);
        if (!matcher.find()) {
            return null;
        }

        String value = matcher.group(1);
        return value == null ? null : value.trim();
    }

    private String buildMissingFieldsMessage(String assigneeArea, String neType) {
        if (isBlank(assigneeArea) && isBlank(neType)) {
            return "Assignee Area, Network Element Type";
        }
        if (isBlank(assigneeArea)) {
            return "Assignee Area";
        }
        return "Network Element Type";
    }

    private String asString(Object value) {
        return value == null ? null : String.valueOf(value);
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}

