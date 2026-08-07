package com.vodafone.gno.dto.jira;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JiraIssueBasicDto {

    private String id;
    private String key;
    private String summary;
    private Object description;
    private String assigneeName;
    private String assigneeEmail;

    private String dueDate;
}