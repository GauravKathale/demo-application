package com.vodafone.gno.exception;

import com.vodafone.gno.dto.ApiResponseDto;
import com.vodafone.gno.exception.jira.JiraIntegrationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles Jira-specific business exceptions
     */
    @ExceptionHandler(JiraIntegrationException.class)
    public ResponseEntity<ApiResponseDto> handleJiraIntegrationException(
            JiraIntegrationException ex) {

        log.error("Jira integration error: {}", ex.getMessage(), ex);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponseDto(
                        false,
                        ex.getMessage()
                ));
    }

    /**
     * Handles illegal arguments / validation failures
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponseDto> handleIllegalArgumentException(
            IllegalArgumentException ex) {

        log.warn("Invalid request received: {}", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponseDto(
                        false,
                        ex.getMessage()
                ));
    }

    /**
     * Catch-all handler for unexpected errors
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDto> handleGenericException(
            Exception ex) {

        log.error("Unhandled exception occurred", ex);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponseDto(
                        false,
                        "An unexpected error occurred. Please contact support."
                ));
    }
}