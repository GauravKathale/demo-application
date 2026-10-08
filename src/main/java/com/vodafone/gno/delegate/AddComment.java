package com.vodafone.gno.delegate;

import com.vodafone.gno.application.service.jira.JiraIssueService;
import org.springframework.stereotype.Component;
import org.vodafone.kramax.bpm.engine.delegate.DelegateExecution;
import org.vodafone.kramax.bpm.engine.delegate.JavaDelegate;

@Component
public class AddComment implements JavaDelegate {

    private final JiraIssueService jiraIssueService;

    public AddComment(JiraIssueService jiraIssueService) {
        this.jiraIssueService = jiraIssueService;
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String businessKey = execution.getProcessBusinessKey();
        String message = (String) execution.getVariable("message");

        jiraIssueService.addCommentToIssue(businessKey, message);
    }
}
