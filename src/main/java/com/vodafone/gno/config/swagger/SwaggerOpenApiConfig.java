package com.vodafone.gno.config.swagger;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerOpenApiConfig {

    // Add new demo endpoints here as they are introduced.
    private static final String[] DEMO_API_PATHS = {
            "/api/jira/issues/status/details",
            "/api/jira/mock/tasks/jira/*/complete-missing-details"
    };

    @Bean
    public GroupedOpenApi technicalApis() {
        return GroupedOpenApi.builder()
                .group("technical-apis")
                .pathsToMatch("/**")
                .build();
    }

    @Bean
    public GroupedOpenApi demoApis() {
        return GroupedOpenApi.builder()
                .group("demo-apis")
                .pathsToMatch(DEMO_API_PATHS)
                .build();
    }
}
