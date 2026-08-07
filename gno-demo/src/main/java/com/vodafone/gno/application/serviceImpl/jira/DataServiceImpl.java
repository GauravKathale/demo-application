package com.vodafone.gno.application.serviceImpl.jira;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vodafone.gno.application.service.jira.DataService;
import com.vodafone.gno.dto.ApiResponseDto;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class DataServiceImpl implements DataService {

    private static final String FILE_NAME = "sample-jira-data.json";

    private final ObjectMapper objectMapper;
    private final Random random = new Random();

    public DataServiceImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public ApiResponseDto fetchRandomData() {

        try {
            List<Map<String, Object>> dataList = loadData();

            if (dataList == null || dataList.isEmpty()) {
                return new ApiResponseDto(false, "Data not available");
            }
//            Map<String, Object> randomData=
//                    dataList.get(random.nextInt(dataList.size()));
            Map<String, Object> randomData =
                    dataList.get(ThreadLocalRandom.current().nextInt(dataList.size()));

            return new ApiResponseDto(true, "Data is Fetched", randomData);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching data");
        }

    }

    private List<Map<String, Object>> loadData() {
        ClassPathResource resource = new ClassPathResource(FILE_NAME);

        if (!resource.exists()) {
            throw new RuntimeException("File " + FILE_NAME + " not Found");
        }

        try (InputStream inputStream = resource.getInputStream()) {

            return objectMapper.readValue(
                    inputStream,
                    new TypeReference<List<Map<String, Object>>>() {
                    }
            );

        } catch (StreamReadException e) {
            throw new RuntimeException(e);
        } catch (DatabindException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
