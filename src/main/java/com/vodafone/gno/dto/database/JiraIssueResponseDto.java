package com.vodafone.gno.dto.database;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class JiraIssueResponseDto {

    private String issueKey;
    private String projectKey;
    private String summary;
    private String status;
    private String priority;
    private String assignee;

    private String cognitoProcessId;
    private String cognitoTaskId;

    private LocalDateTime createdAtJira;
    private LocalDateTime createdAt;
}