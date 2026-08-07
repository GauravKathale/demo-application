package com.vodafone.gno.controller.jira;

import com.vodafone.gno.dto.ApiResponseDto;
import com.vodafone.gno.application.service.jira.JiraIssueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jira")
@RequiredArgsConstructor
public class JiraIssueController {

    private final JiraIssueService jiraService;

    @GetMapping("/issues/recent")
    public ResponseEntity<ApiResponseDto> getRecentIssues() {
        return ResponseEntity.ok(jiraService.fetchIssuesLast30Days());
    }


    @PostMapping("/issues/{issueId}/comment")
    public ResponseEntity<ApiResponseDto> addComment(
            @PathVariable String issueId,
            @RequestBody String comment) {

        return ResponseEntity.ok(
                jiraService.addCommentToIssue(issueId, comment)
        );
    }

    @PutMapping("/issues/{issueId}/summary")
    public ResponseEntity<ApiResponseDto> updateSummary(
            @PathVariable String issueId,
            @RequestBody String summary) {

        return ResponseEntity.ok(
                jiraService.updateIssueSummary(issueId, summary)
        );
    }


    @GetMapping("/issues/{issueId}")
    public ResponseEntity<ApiResponseDto> getIssueById(
            @PathVariable String issueId) {

        return ResponseEntity.ok(
                jiraService.getIssueById(issueId)
        );
    }
    @GetMapping("/issues/{issueId}/transitions")
    public ResponseEntity<ApiResponseDto> getIssueTransitions(
            @PathVariable String issueId) {

        return ResponseEntity.ok(
                jiraService.getIssueTransitions(issueId)
        );
    }
    @PostMapping("/issues/{issueId}/status/{transitionId}")
    public ResponseEntity<ApiResponseDto> changeIssueStatus(
            @PathVariable String issueId,
            @PathVariable String transitionId) {

        return ResponseEntity.ok(
                jiraService.changeIssueStatus(issueId, transitionId)
        );
    }

    //  New issues created in last 30 minutes
    @GetMapping("/issues/recent/30-mins")
    public ResponseEntity<ApiResponseDto> getRecentIssues30Minutes() {
        return ResponseEntity.ok(jiraService.fetchIssuesLast30Minutes());
    }

    //  Get assignee
    @GetMapping("/issues/{issueId}/assignee")
    public ResponseEntity<ApiResponseDto> getAssignee(@PathVariable String issueId) {
        return ResponseEntity.ok(jiraService.getIssueAssignee(issueId));
    }

    // Get issue status
    @GetMapping("/issues/{issueId}/status")
    public ResponseEntity<ApiResponseDto> getStatus(@PathVariable String issueId) {
        return ResponseEntity.ok(jiraService.getIssueStatus(issueId));
    }

    // Update assignee only
    @PutMapping("/issues/{issueId}/assignee/{accountId}")
    public ResponseEntity<ApiResponseDto> updateAssignee(
            @PathVariable String issueId,
            @PathVariable String accountId) {

        return ResponseEntity.ok(
                jiraService.updateAssignee(issueId, accountId)
        );
    }

    // Update end date only
    @PutMapping("/issues/{issueId}/due-date")
    public ResponseEntity<ApiResponseDto> updateDueDate(
            @PathVariable String issueId,
            @RequestBody String dueDate) {

        return ResponseEntity.ok(
                jiraService.updateEndDate(issueId, dueDate)
        );
    }

    //  update both assignee & end date
    @PutMapping("/issues/{issueId}/update")
    public ResponseEntity<ApiResponseDto> updateAssigneeAndEndDate(
            @PathVariable String issueId,
            @RequestParam(required = false) String assigneeAccountId,
            @RequestParam(required = false) String dueDate) {

        return ResponseEntity.ok(
                jiraService.updateIssueFields(issueId, assigneeAccountId, dueDate)
        );
    }

    @GetMapping("/issues/status")
    public ResponseEntity<ApiResponseDto> getIssuesByStatus(
            @RequestParam String status) {

        return ResponseEntity.ok(
                jiraService.fetchIssuesByStatus(status)
        );
    }

    @GetMapping("/issues/status/details")
    public ResponseEntity<ApiResponseDto> getIssuesBasicDetailsByStatus(
            @RequestParam String status) {

        return ResponseEntity.ok(
                jiraService.fetchIssueBasicDetailsByStatus(status)
        );
    }

}