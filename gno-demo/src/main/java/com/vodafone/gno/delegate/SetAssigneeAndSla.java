package com.vodafone.gno.delegate;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.vodafone.gno.application.service.jira.JiraIssueService;
import org.springframework.stereotype.Component;
import org.vodafone.cognito.bpm.engine.delegate.DelegateExecution;
import org.vodafone.cognito.bpm.engine.delegate.JavaDelegate;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Component
public class SetAssigneeAndSla implements JavaDelegate {

    private final JiraIssueService jiraIssueService;

    public SetAssigneeAndSla(JiraIssueService jiraIssueService) {
        this.jiraIssueService = jiraIssueService;
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String assignee = (String) execution.getVariable("assigneeName");
        Date sla_date = (Date) execution.getVariable("sla_date");
        String businessKey = execution.getProcessBusinessKey();
        System.out.println("Assignee" + assignee);
        System.out.println("SLA Date" + sla_date);
        System.out.println("Business Key" + businessKey);

        String formattedDate = sla_date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Thread.sleep(30000);
        System.out.println("Formatted SLA Date: " + formattedDate);
        jiraIssueService.updateAssignee(businessKey, assignee);
        jiraIssueService.updateEndDate(businessKey, formattedDate);
    }
}
