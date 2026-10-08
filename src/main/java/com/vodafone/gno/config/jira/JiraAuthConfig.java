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

//        String auth = jiraConfig.getEmail() + ":" + jiraConfig.getApiToken();
//        String encodedAuth = Base64.getEncoder()
//                .encodeToString(auth.getBytes(StandardCharsets.UTF_8));
        String encodedAuth = "a2F0aGFsZS5nYXVyYXYzQGdtYWlsLmNvbTpBVEFUVDN4RmZHRjB3bXBWVDZjMFlIVllQU25OWWpMcmx5NWlBaEZZNkgyd0xTdzZuMmVGZEtZbTBUUTNteXdVRWtSOGQ0dTRGUk1rU2dDdU8tc1k1VXlldmltNFp5SFFDS1ZjdEg4WG5vMUlBSFhBMzFYeWF0Xy1wWnM1TnNyUnUxVW9yODM4SjBISTZDR2JzQjRtRFlZZk5QRFdZMG5HS2NLTnRnaW8tNDhVTF94cTA4cnJVSUU9N0UyNjYzODY=";
        headers.set("Authorization", "Basic " + encodedAuth);
        return headers;
    }
}
