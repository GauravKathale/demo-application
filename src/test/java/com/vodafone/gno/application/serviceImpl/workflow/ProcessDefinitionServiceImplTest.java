package com.vodafone.gno.application.serviceImpl.workflow;

import com.vodafone.gno.dto.ApiResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class ProcessDefinitionServiceImplTest {

    private ProcessDefinitionServiceImpl service;
    private MockRestServiceServer server;

    @BeforeEach
    void setUp() {
        RestTemplate restTemplate = new RestTemplate();
        server = MockRestServiceServer.bindTo(restTemplate).build();
        service = new ProcessDefinitionServiceImpl(restTemplate);
        ReflectionTestUtils.setField(service, "engineRestBaseUrl", "http://localhost:8080");
    }

    @Test
    void fetchProcessDefinitionsReturnsApiResponseWithRemotePayload() {
        server.expect(requestTo("http://localhost:8080/engine-rest/process-definition"))
                .andExpect(method(GET))
                .andRespond(withSuccess("[{\"id\":\"ticketWorkflow\"}]", APPLICATION_JSON));

        ApiResponseDto response = service.fetchProcessDefinitions();

        assertTrue(response.isSuccess());
        assertEquals("Process definitions fetched successfully", response.getMessage());
        assertNotNull(response.getData());
        server.verify();
    }

    @Test
    void fetchHistoricProcessInstancesReturnsApiResponseWithRemotePayload() {
        server.expect(requestTo("http://localhost:8080/engine-rest/history/process-instance"))
                .andExpect(method(POST))
                .andExpect(content().json("{\"processDefinitionKey\":\"monitor_ticket\"}"))
                .andRespond(withSuccess("[{\"id\":\"monitor_ticket:1:abc\"}]", APPLICATION_JSON));

        ApiResponseDto response = service.fetchHistoricProcessInstances(Map.of("processDefinitionKey", "monitor_ticket"));

        assertTrue(response.isSuccess());
        assertEquals("Historic process instances fetched successfully", response.getMessage());
        assertNotNull(response.getData());
        server.verify();
    }
}

