package com.vodafone.gno.worker;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.vodafone.kramax.bpm.client.ExternalTaskClient;
import org.vodafone.kramax.bpm.client.task.ExternalTask;
import org.vodafone.kramax.bpm.client.task.ExternalTaskService;

import jakarta.annotation.PostConstruct;

/**
 * External task worker that subscribes to the Camunda topic `validateTicketInfo`.
 *
 * Responsibilities:
 * - Subscribe to the external task topic on application start
 * - Read `ticket_description` variable from the process/external task
 * - Validate the presence of the description
 * - Complete the task and set `ticket_info_valid` and `ticket_info_invalid_reason` variables
 */
@Component
public class ValidateTicketInfo {

    private final ExternalTaskClient client;

    /**
     * Create an ExternalTaskClient using configured engine REST URL and a date format
     * compatible with ISO-8601 timestamps returned by the engine (for example +00:00).
     */
    public ValidateTicketInfo(
            @Value("${workflow.engine-rest.base-url:http://localhost:8080}") String engineBaseUrl,
            @Value("${workflow.external-task-client.date-format:yyyy-MM-dd'T'HH:mm:ss.SSSXXX}") String dateFormat) {
        this.client = ExternalTaskClient.create()
                .baseUrl(resolveEngineRestBaseUrl(engineBaseUrl))
                .dateFormat(dateFormat)
                .asyncResponseTimeout(10000)
                .build();
    }

    private static String resolveEngineRestBaseUrl(String engineBaseUrl) {
        String baseUrl = engineBaseUrl == null ? "http://localhost:8080" : engineBaseUrl.trim();
        return baseUrl.endsWith("/engine-rest") ? baseUrl : baseUrl + "/engine-rest";
    }

    /**
     * Subscribe to the `validateTicketInfo` topic after Spring constructs the bean.
     */
    @PostConstruct
    public void subscribe() {
        client.subscribe("validateTicketInfo")
                .lockDuration(1000)
                .handler(this::handle)
                .open();

        System.out.println("[ValidateTicketInfo] Subscribed to topic 'validateTicketInfo'");
    }

    /**
     * Handler invoked when an external task for the subscribed topic is received.
     * It validates the ticket description (from process variables) and completes the task.
     */
    private void handle(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        String businessKey = externalTask.getBusinessKey();
        System.out.println("[ValidateTicketInfo] Handling task for businessKey: " + businessKey);

        // Read the description from process/external task variables only
        String ticketDescription = externalTask.getVariable("ticket_description");

        // Basic validation: description must be present and not empty
        boolean valid = ticketDescription != null && !ticketDescription.trim().isEmpty();

        Map<String, Object> vars = new HashMap<>();
        if (valid) {
            vars.put("ticket_info_valid", true);
            vars.put("ticket_info_invalid_reason", null);
            System.out.println("[ValidateTicketInfo] Ticket info valid for " + businessKey);
        } else {
            vars.put("ticket_info_valid", false);
            vars.put("ticket_info_invalid_reason", "Missing or empty ticket description");
            System.out.println("[ValidateTicketInfo] Ticket info invalid for " + businessKey);
        }

        // Complete the external task and return the validation variables to the process
        externalTaskService.complete(externalTask, vars);
    }
}
