package com.vodafone.gno.delegate;

import com.vodafone.gno.application.service.jira.JiraIssueService;
import org.springframework.stereotype.Component;
import org.vodafone.cognito.bpm.engine.delegate.DelegateExecution;
import org.vodafone.cognito.bpm.engine.delegate.JavaDelegate;

@Component
public class UpdateTicketStatus implements JavaDelegate{

    private final JiraIssueService jiraIssueService;

    public UpdateTicketStatus(JiraIssueService jiraIssueService) {
        this.jiraIssueService = jiraIssueService;
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String businessKey = execution.getProcessBusinessKey();
        String status = (String) execution.getVariable("status");

        jiraIssueService.changeIssueStatus(businessKey, status);
    }
}