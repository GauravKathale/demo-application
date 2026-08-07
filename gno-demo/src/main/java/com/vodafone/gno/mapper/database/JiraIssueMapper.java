package com.vodafone.gno.mapper.database;

import com.vodafone.gno.dto.database.JiraIssueResponseDto;
import com.vodafone.gno.model.entity.JiraIssueEntity;

public class JiraIssueMapper {

    public static JiraIssueResponseDto toDto(JiraIssueEntity entity) {
        return JiraIssueResponseDto.builder()
                .issueKey(entity.getIssueKey())
                .projectKey(entity.getProjectKey())
                .summary(entity.getSummary())
                .status(entity.getStatus())
                .priority(entity.getPriority())
                .assignee(entity.getAssignee())
                .cognitoProcessId(entity.getCognitoProcessId())
                .cognitoTaskId(entity.getCognitoTaskId())
                .createdAtJira(entity.getCreatedAtJira())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
