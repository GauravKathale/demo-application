package com.vodafone.gno.controller.jira;

import com.vodafone.gno.dto.ApiResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vodafone.kramax.bpm.engine.TaskService;
import org.vodafone.kramax.bpm.engine.task.Task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/jira/mock")
public class MockUserTaskController {

    private static final String MISSING_DETAILS_TASK_ID = "Activity_1vxeg6n";

    private final TaskService taskService;

    public MockUserTaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping({"/tasks/jira/{jiraId}/complete-missing-details", "/tasks/jira/{jiraId}/complete-missing-detail"})
    public ResponseEntity<ApiResponseDto> completeMissingDetailsTask(@PathVariable String jiraId) {
        List<Task> tasks = taskService.createTaskQuery()
                .processInstanceBusinessKey(jiraId)
                .taskDefinitionKey(MISSING_DETAILS_TASK_ID)
                .list();

        if (tasks == null || tasks.isEmpty()) {
            return ResponseEntity.ok(new ApiResponseDto(
                    false,
                    "No active 'Provide Missing Ticket Details' task found for Jira id: " + jiraId
            ));
        }

        List<String> completedTaskIds = new ArrayList<>();
        List<String> processInstanceIds = new ArrayList<>();

        for (Task task : tasks) {
            Map<String, Object> completionVariables = new HashMap<>();
            completionVariables.put("missing_details_user_confirmed", true);
            completionVariables.put("missing_details_completed_at", LocalDateTime.now().toString());
            completionVariables.put("missing_details_completed_by", "mock-jira-api");

            taskService.complete(task.getId(), completionVariables);
            completedTaskIds.add(task.getId());
            processInstanceIds.add(task.getProcessInstanceId());
        }

        Map<String, Object> data = new HashMap<>();
        data.put("jiraId", jiraId);
        data.put("completedCount", completedTaskIds.size());
        data.put("taskIds", completedTaskIds);
        data.put("processInstanceIds", processInstanceIds);
        data.put("nextStep", "Retrieve Ticket Details");

        return ResponseEntity.ok(new ApiResponseDto(
                true,
                "User task(s) completed successfully for Jira id",
                data
        ));
    }
}


