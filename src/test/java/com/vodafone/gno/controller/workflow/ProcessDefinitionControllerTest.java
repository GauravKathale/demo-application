package com.vodafone.gno.controller.workflow;

import com.vodafone.gno.application.service.workflow.ProcessDefinitionService;
import com.vodafone.gno.dto.ApiResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProcessDefinitionControllerTest {

    @Test
    void getProcessDefinitionsDelegatesToService() {
        ProcessDefinitionService service = new ProcessDefinitionService() {
            @Override
            public ApiResponseDto fetchProcessDefinitions() {
                return new ApiResponseDto(
                        true,
                        "Process definitions fetched successfully",
                        List.of(Map.of("id", "ticketWorkflow"))
                );
            }

            @Override
            public ApiResponseDto fetchHistoricProcessInstances(Map<String, Object> request) {
                return new ApiResponseDto(true, "unused", request);
            }
        };
        ProcessDefinitionController controller = new ProcessDefinitionController(service);

        ResponseEntity<ApiResponseDto> response = controller.getProcessDefinitions();

        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Process definitions fetched successfully", response.getBody().getMessage());
    }

    @Test
    void getHistoricProcessInstancesDelegatesToService() {
        AtomicReference<Map<String, Object>> capturedRequest = new AtomicReference<>();
        ProcessDefinitionService service = new ProcessDefinitionService() {
            @Override
            public ApiResponseDto fetchProcessDefinitions() {
                return new ApiResponseDto(true, "unused");
            }

            @Override
            public ApiResponseDto fetchHistoricProcessInstances(Map<String, Object> request) {
                capturedRequest.set(request);
                return new ApiResponseDto(
                        true,
                        "Historic process instances fetched successfully",
                        List.of(Map.of("id", "monitor_ticket:1:abc"))
                );
            }
        };
        ProcessDefinitionController controller = new ProcessDefinitionController(service);
        Map<String, Object> request = Map.of("processDefinitionKey", "monitor_ticket");

        ResponseEntity<ApiResponseDto> response = controller.getHistoricProcessInstances(request);

        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Historic process instances fetched successfully", response.getBody().getMessage());
        assertEquals(request, capturedRequest.get());
    }
}

