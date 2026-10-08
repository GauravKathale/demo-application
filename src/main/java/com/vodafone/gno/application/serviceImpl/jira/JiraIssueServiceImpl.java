package com.vodafone.gno.application.serviceImpl.jira;

import com.vodafone.gno.application.service.jira.JiraIssueService;
import com.vodafone.gno.config.jira.JiraAuthConfig;
import com.vodafone.gno.config.jira.JiraConfig;
import com.vodafone.gno.dto.ApiResponseDto;
import com.vodafone.gno.dto.jira.JiraIssueBasicDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.vodafone.kramax.bpm.engine.RuntimeService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JiraIssueServiceImpl implements JiraIssueService {

    @Autowired
    private RuntimeService runtimeService;

    private final JiraConfig jiraConfig;
    private final JiraAuthConfig jiraAuthConfig;
    private final RestTemplate restTemplate;

    // LAST 30 DAYS
    @Override
    public ApiResponseDto fetchIssuesLast30Days() {
        return fetchIssuesByJql(
                "created >= -30d ORDER BY created DESC",
                "Jira issues (last 30 days) fetched successfully"
        );
    }

    // LAST 30 MINUTES (FIXED)
    @Override
    public ApiResponseDto fetchIssuesLast30Minutes() {
        return fetchIssuesByJql(
                "created >= -30m ORDER BY created DESC",
                "Jira issues (last 30 minutes) fetched successfully"
        );
    }

    //  COMMON SEARCH METHOD (REUSED)
    private ApiResponseDto fetchIssuesByJql(String jql, String successMessage) {
        try {
            String url = jiraConfig.getBaseUrl() + "/rest/api/3/search/jql";

            Map<String, Object> body = new HashMap<>();
            body.put("jql", jql);
            body.put("maxResults", 50);

            HttpEntity<Map<String, Object>> entity =
                    new HttpEntity<>(body, jiraAuthConfig.jiraHeaders());

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    String.class
            );
            System.out.println(response.getBody());

            return new ApiResponseDto(true, successMessage, response.getBody());

        } catch (Exception e) {
            return new ApiResponseDto(false, "Error fetching Jira issues: " + e.getMessage());
        }
    }

    // ADD COMMENT
    @Override
    public ApiResponseDto addCommentToIssue(String issueIdOrKey, String comment) {
        try {
            String url = jiraConfig.getBaseUrl()
                    + "/rest/api/3/issue/" + issueIdOrKey + "/comment";

            Map<String, Object> textNode = new HashMap<>();
            textNode.put("type", "text");
            textNode.put("text", comment);

            Map<String, Object> paragraphNode = new HashMap<>();
            paragraphNode.put("type", "paragraph");
            paragraphNode.put("content", List.of(textNode));

            Map<String, Object> bodyNode = new HashMap<>();
            bodyNode.put("type", "doc");
            bodyNode.put("version", 1);
            bodyNode.put("content", List.of(paragraphNode));

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("body", bodyNode);

            HttpEntity<Map<String, Object>> entity =
                    new HttpEntity<>(requestBody, jiraAuthConfig.jiraHeaders());

            restTemplate.exchange(url, HttpMethod.POST, entity, Void.class);

            return new ApiResponseDto(true, "Comment added successfully");

        } catch (Exception e) {
            return new ApiResponseDto(false, "Failed to add comment: " + e.getMessage());
        }
    }

    //  UPDATE SUMMARY
    @Override
    public ApiResponseDto updateIssueSummary(String issueIdOrKey, String summary) {
        Map<String, Object> fields = new HashMap<>();
        fields.put("summary", summary);
        return updateIssueFieldsCommon(issueIdOrKey, fields);
    }

    // GET ISSUE BY ID
    @Override
    public ApiResponseDto getIssueById(String issueIdOrKey) {
        try {
            String url = jiraConfig.getBaseUrl()
                    + "/rest/api/3/issue/" + issueIdOrKey;

            HttpEntity<Void> entity =
                    new HttpEntity<>(jiraAuthConfig.jiraHeaders());

            ResponseEntity<Object> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    Object.class
            );

            return new ApiResponseDto(true, "Issue fetched successfully", response.getBody());

        } catch (Exception e) {
            return new ApiResponseDto(false, "Failed to fetch issue: " + e.getMessage());
        }
    }

    //  GET ASSIGNEE
    @Override
    public ApiResponseDto getIssueAssignee(String issueIdOrKey) {
        ApiResponseDto issue = getIssueById(issueIdOrKey);
        if (!issue.isSuccess()) return issue;

        Map<?, ?> response = (Map<?, ?>) issue.getData();
        Map<?, ?> fields = (Map<?, ?>) response.get("fields");

        return new ApiResponseDto(true, "Assignee fetched", fields.get("assignee"));
    }

    // GET STATUS
    @Override
    public ApiResponseDto getIssueStatus(String issueIdOrKey) {
        ApiResponseDto issue = getIssueById(issueIdOrKey);
        if (!issue.isSuccess()) return issue;

        Map<?, ?> response = (Map<?, ?>) issue.getData();
        Map<?, ?> fields = (Map<?, ?>) response.get("fields");

        return new ApiResponseDto(true, "Issue status fetched", fields.get("status"));
    }

    //  TRANSITIONS
    @Override
    public ApiResponseDto getIssueTransitions(String issueIdOrKey) {
        try {
            String url = jiraConfig.getBaseUrl()
                    + "/rest/api/3/issue/" + issueIdOrKey + "/transitions";

            HttpEntity<Void> entity = new HttpEntity<>(jiraAuthConfig.jiraHeaders());

            ResponseEntity<Object> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, Object.class
            );

            return new ApiResponseDto(true, "Issue transitions fetched", response.getBody());

        } catch (Exception e) {
            return new ApiResponseDto(false, "Failed to fetch transitions: " + e.getMessage());
        }
    }

    //  CHANGE STATUS
    @Override
    public ApiResponseDto changeIssueStatus(String issueIdOrKey, String transitionId) {
        try {
            String url = jiraConfig.getBaseUrl()
                    + "/rest/api/3/issue/" + issueIdOrKey + "/transitions";

            Map<String, Object> transition = new HashMap<>();
            transition.put("id", transitionId);

            Map<String, Object> body = new HashMap<>();
            body.put("transition", transition);

            HttpEntity<Map<String, Object>> entity =
                    new HttpEntity<>(body, jiraAuthConfig.jiraHeaders());

            restTemplate.exchange(url, HttpMethod.POST, entity, Void.class);

            return new ApiResponseDto(true, "Issue status changed successfully");

        } catch (Exception e) {
            return new ApiResponseDto(false, "Failed to change status: " + e.getMessage());
        }
    }

    // COMMON UPDATE METHOD
    private ApiResponseDto updateIssueFieldsCommon(String issueIdOrKey, Map<String, Object> fields) {
        try {
            String url = jiraConfig.getBaseUrl()
                    + "/rest/api/3/issue/" + issueIdOrKey;

            Map<String, Object> body = new HashMap<>();
            body.put("fields", fields);

            HttpEntity<Map<String, Object>> entity =
                    new HttpEntity<>(body, jiraAuthConfig.jiraHeaders());

            restTemplate.exchange(url, HttpMethod.PUT, entity, Void.class);

            return new ApiResponseDto(true, "Issue updated successfully");

        } catch (Exception e) {
            return new ApiResponseDto(false, e.getMessage());
        }
    }

    // UPDATE ASSIGNEE
    @Override
    public ApiResponseDto updateAssignee(String issueIdOrKey, String accountId) {
        Map<String, Object> assignee = new HashMap<>();
        assignee.put("id", accountId);

        Map<String, Object> fields = new HashMap<>();
        fields.put("assignee", assignee);

        return updateIssueFieldsCommon(issueIdOrKey, fields);
    }

    //  UPDATE END DATE
    @Override
    public ApiResponseDto updateEndDate(String issueIdOrKey, String dueDate) {
        Map<String, Object> fields = new HashMap<>();
        fields.put("duedate", dueDate); // yyyy-MM-dd

        return updateIssueFieldsCommon(issueIdOrKey, fields);
    }

    //  UPDATE BOTH
    @Override
    public ApiResponseDto updateIssueFields(String issueIdOrKey, String assigneeAccountId, String dueDate) {
        Map<String, Object> fields = new HashMap<>();

        if (assigneeAccountId != null) {
            Map<String, Object> assignee = new HashMap<>();
            assignee.put("id", assigneeAccountId);
            fields.put("assignee", assignee);
        }

        if (dueDate != null) {
            fields.put("duedate", dueDate);
        }

        if (fields.isEmpty()) {
            return new ApiResponseDto(false, "No fields to update");
        }

        return updateIssueFieldsCommon(issueIdOrKey, fields);
    }

    @Override
    public ApiResponseDto fetchIssuesByStatus(String statusName) {
        return fetchIssuesByJql(
                "status = \"" + statusName + "\" ORDER BY created DESC",
                "Issues with status '" + statusName + "' fetched successfully"
        );
    }

    @Override
    public ApiResponseDto fetchIssueIdsPastOneYear() {
        try {
            String url = jiraConfig.getBaseUrl() + "/rest/api/3/search/jql";

            Map<String, Object> body = new HashMap<>();
            body.put("jql", "created >= -365d ORDER BY created DESC");
            body.put("maxResults", 100);

            HttpEntity<Map<String, Object>> entity =
                    new HttpEntity<>(body, jiraAuthConfig.jiraHeaders());

            ResponseEntity<Map> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    Map.class
            );

            List<Map<String, Object>> issues = response.getBody() != null
                    ? (List<Map<String, Object>>) response.getBody().get("issues")
                    : new ArrayList<>();

            List<String> issueIds = new ArrayList<>();
            if (issues != null) {
                for (Map<String, Object> issue : issues) {
                    Object issueId = issue.get("id");
                    if (issueId != null) {
                        issueIds.add(issueId.toString());
                    }
                }
            }

            return new ApiResponseDto(
                    true,
                    "Issue IDs from the last one year fetched successfully",
                    issueIds
            );

        } catch (Exception e) {
            return new ApiResponseDto(
                    false,
                    "Failed to fetch issue IDs: " + e.getMessage()
            );
        }
    }

    private String getPlainDescription(Object descriptionObj) {
        if (!(descriptionObj instanceof Map)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        Map<String, Object> desc = (Map<String, Object>) descriptionObj;
        List<Map<String, Object>> blocks =
                (List<Map<String, Object>>) desc.get("content");
        if (blocks == null) return "";
        for (Map<String, Object> block : blocks) {
            List<Map<String, Object>> contents =
                    (List<Map<String, Object>>) block.get("content");
            if (contents == null) continue;
            for (Map<String, Object> item : contents) {
                if ("text".equals(item.get("type"))) {
                    sb.append(item.get("text"));
                }
                if ("hardBreak".equals(item.get("type"))) {
                    sb.append("\n");
                }
            }
            sb.append("\n");
        }
        return sb.toString().trim();
    }

    @Override
    public ApiResponseDto fetchIssueDetailsById(String issueIdOrKey) {
        try {
            ApiResponseDto issueResponse = getIssueById(issueIdOrKey);
            if (!issueResponse.isSuccess()) {
                return issueResponse;
            }

            Map<String, Object> issue = (Map<String, Object>) issueResponse.getData();
            Map<String, Object> fields = issue == null
                    ? new HashMap<>()
                    : (Map<String, Object>) issue.getOrDefault("fields", new HashMap<>());

            Map<String, Object> assignee = (Map<String, Object>) fields.get("assignee");
            String description = getPlainDescription(fields.get("description"));

            Map<String, Object> details = new HashMap<>();
            details.put("id", issue == null ? null : issue.get("id"));
            details.put("key", issue == null ? null : issue.get("key"));
            details.put("summary", fields.get("summary"));
            details.put("description", description);
            details.put("assigneeName", assignee != null ? assignee.get("displayName") : null);
            details.put("assigneeEmail", assignee != null ? assignee.get("emailAddress") : null);
            details.put("dueDate", fields.get("duedate"));
            details.put("status", fields.get("status"));

            return new ApiResponseDto(true, "Issue details fetched successfully", details);
        } catch (Exception e) {
            return new ApiResponseDto(false, "Failed to fetch issue details: " + e.getMessage());
        }
    }

    @Override
    public ApiResponseDto fetchIssueBasicDetailsByStatus(String statusName) {
        try {
            String url = jiraConfig.getBaseUrl() + "/rest/api/3/search/jql";
            Map<String, Object> body = new HashMap<>();
            body.put(
                    "jql",
                    "status = \"" + statusName + "\" ORDER BY created DESC"
            );
            body.put(
                    "fields",
                    List.of("summary", "description", "assignee", "duedate")
            );
            body.put("maxResults", 50);
            HttpEntity<Map<String, Object>> entity =
                    new HttpEntity<>(body, jiraAuthConfig.jiraHeaders());
            ResponseEntity<Map> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    Map.class
            );
            List<Map<String, Object>> issues =
                    (List<Map<String, Object>>) response.getBody().get("issues");
            List<JiraIssueBasicDto> result = new ArrayList<>();
            for (Map<String, Object> issue : issues) {
                Map<String, Object> fields =
                        (Map<String, Object>) issue.get("fields");
                Map<String, Object> assignee =
                        (Map<String, Object>) fields.get("assignee");
                String assigneeEmail = assignee != null
                        ? (String) assignee.get("emailAddress")
                        : "";
                String assigneeName = assignee != null
                        ? (String) assignee.get("displayName")
                        : null;
                String issueId = (String) issue.get("id");
                String issueKey = (String) issue.get("key");

                String description = getPlainDescription(fields.get("description"));
                System.out.println("IssueId: " + issueId);
                System.out.println("Description: " + description);
                Map<String, Object> variables = new HashMap<>();
                variables.put("ticket_description", description);

                runtimeService.startProcessInstanceByMessage(
                        "initiate_ticket_mgmt",
                        issueId,
                        variables
                );
                JiraIssueBasicDto dto = new JiraIssueBasicDto(
                        issueId,
                        issueKey,
                        (String) fields.get("summary"),
                        description,
                        assigneeName,
                        assigneeEmail,
                        (String) fields.get("duedate")
                );
                result.add(dto);
            }
            return new ApiResponseDto(
                    true,
                    "Issues fetched with required details for status: " + statusName,
                    result
            );
        } catch (Exception e) {
            return new ApiResponseDto(
                    false,
                    "Failed to fetch issues: " + e.getMessage()
            );
        }
    }

}
