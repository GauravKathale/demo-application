package com.vodafone.gno.delegate;

import com.vodafone.gno.application.service.jira.JiraIssueService;
import com.vodafone.gno.dto.ApiResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.vodafone.cognito.bpm.engine.RuntimeService;
import org.vodafone.cognito.bpm.engine.delegate.DelegateExecution;
import org.vodafone.cognito.bpm.engine.delegate.JavaDelegate;

import java.util.Map;

@Component
public class GetTicketStatus implements JavaDelegate {

    private final JiraIssueService jiraIssueService;

    public GetTicketStatus(JiraIssueService jiraIssueService) {
        this.jiraIssueService = jiraIssueService;
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String businessKey = execution.getProcessBusinessKey();

        ApiResponseDto statusResponse = jiraIssueService.getIssueStatus(businessKey);
        Object statusData = statusResponse.getData();
        if (statusData instanceof Map) {
            Object name = ((Map<?, ?>) statusData).get("name");
            execution.setVariable("ticketStatus", name);
        } else {
            execution.setVariable("ticketStatus", null);
        }
    }
}
