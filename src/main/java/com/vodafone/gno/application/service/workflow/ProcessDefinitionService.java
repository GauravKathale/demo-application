package com.vodafone.gno.application.service.workflow;

import com.vodafone.gno.dto.ApiResponseDto;

import java.util.Map;

public interface ProcessDefinitionService {

    ApiResponseDto fetchProcessDefinitions();

    ApiResponseDto fetchHistoricProcessInstances(Map<String, Object> request);
}

