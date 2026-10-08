package com.vodafone.gno.delegate;

import org.springframework.stereotype.Component;
import org.vodafone.kramax.bpm.engine.delegate.DelegateExecution;
import org.vodafone.kramax.bpm.engine.delegate.JavaDelegate;

@Component
public class NotificationDelegate implements JavaDelegate {

    private static final String GLOBAL_NOTIFICATION_ERROR_CODE = "ERR_GLOBAL_NOTIFICATION";

    @Override
    public void execute(DelegateExecution execution) {
        System.out.println("[NotificationDelegate] Sending failure notification for businessKey: "
                + execution.getProcessBusinessKey());

        // Keep a standard error code in process context for downstream tracking/logging.
        execution.setVariable("global_notification_error_code", GLOBAL_NOTIFICATION_ERROR_CODE);
    }
}

