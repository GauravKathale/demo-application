package com.vodafone.gno.application.serviceImpl.workflow;

import com.vodafone.gno.application.service.workflow.ProcessDefinitionService;
import com.vodafone.gno.dto.ApiResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProcessDefinitionServiceImpl implements ProcessDefinitionService {

    private final RestTemplate restTemplate;

    @Value("${workflow.engine-rest.base-url:http://localhost:8080}")
    private String engineRestBaseUrl;

    @Override
    public ApiResponseDto fetchProcessDefinitions() {
        try {
            String url = engineRestBaseUrl + "/engine-rest/process-definition";

            ResponseEntity<Object> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    Object.class
            );

            return new ApiResponseDto(
                    true,
                    "Process definitions fetched successfully",
                    response.getBody()
            );
        } catch (Exception e) {
            return new ApiResponseDto(
                    false,
                    "Failed to fetch process definitions: " + e.getMessage()
            );
        }
    }

    @Override
    public ApiResponseDto fetchHistoricProcessInstances(Map<String, Object> request) {
        try {
            String url = engineRestBaseUrl + "/engine-rest/history/process-instance";

            ResponseEntity<Object> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    new HttpEntity<>(request),
                    Object.class
            );

            return new ApiResponseDto(
                    true,
                    "Historic process instances fetched successfully",
                    response.getBody()
            );
        } catch (Exception e) {
            return new ApiResponseDto(
                    false,
                    "Failed to fetch historic process instances: " + e.getMessage()
            );
        }
    }
}

