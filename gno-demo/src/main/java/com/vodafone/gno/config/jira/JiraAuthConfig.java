package com.vodafone.gno.config.jira;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Configuration
@RequiredArgsConstructor
public class JiraAuthConfig {

    private final JiraConfig jiraConfig;

    public HttpHeaders jiraHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String auth = jiraConfig.getEmail() + ":" + jiraConfig.getApiToken();
        String encodedAuth = Base64.getEncoder()
                .encodeToString(auth.getBytes(StandardCharsets.UTF_8));

        headers.set("Authorization", "Basic " + encodedAuth);
        return headers;
    }
}
