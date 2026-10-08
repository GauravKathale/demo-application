package com.vodafone.gno.controller.workflow;

import com.vodafone.gno.application.service.workflow.ProcessDefinitionService;
import com.vodafone.gno.dto.ApiResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/engine-rest")
@RequiredArgsConstructor
public class ProcessDefinitionController {

    private final ProcessDefinitionService processDefinitionService;

    @GetMapping("/process-definition")
    public ResponseEntity<ApiResponseDto> getProcessDefinitions() {
        return ResponseEntity.ok(processDefinitionService.fetchProcessDefinitions());
    }

    @PostMapping("/history/process-instance")
    public ResponseEntity<ApiResponseDto> getHistoricProcessInstances(@RequestBody Map<String, Object> request) {
        return ResponseEntity.ok(processDefinitionService.fetchHistoricProcessInstances(request));
    }
}

