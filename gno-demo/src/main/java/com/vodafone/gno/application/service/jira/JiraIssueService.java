package com.vodafone.gno.application.service.jira;

import com.vodafone.gno.dto.ApiResponseDto;

public interface JiraIssueService {

    ApiResponseDto fetchIssuesLast30Days();

    ApiResponseDto fetchIssuesLast30Minutes();

    ApiResponseDto addCommentToIssue(String issueIdOrKey, String comment);

    ApiResponseDto updateIssueSummary(String issueIdOrKey, String summary);

    ApiResponseDto getIssueById(String issueIdOrKey);

    ApiResponseDto getIssueAssignee(String issueIdOrKey);

    ApiResponseDto getIssueStatus(String issueIdOrKey);

    ApiResponseDto updateAssignee(String issueIdOrKey, String accountId);

    ApiResponseDto updateEndDate(String issueIdOrKey, String dueDate);

    ApiResponseDto updateIssueFields(
            String issueIdOrKey,
            String assigneeAccountId,
            String dueDate
    );

    ApiResponseDto getIssueTransitions(String issueIdOrKey);

    ApiResponseDto changeIssueStatus(String issueIdOrKey, String transitionId);

    ApiResponseDto fetchIssuesByStatus(String status);

    ApiResponseDto fetchIssueBasicDetailsByStatus(String statusName);
}