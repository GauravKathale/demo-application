package com.vodafone.gno.delegate;

import org.springframework.stereotype.Component;
import org.vodafone.kramax.bpm.engine.delegate.DelegateExecution;
import org.vodafone.kramax.bpm.engine.delegate.JavaDelegate;

@Component("sout")
public class SoutDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("========== Sout Delegate Execution ==========");
        System.out.println("Process Instance ID: " + execution.getProcessInstanceId());
        System.out.println("Activity ID: " + execution.getCurrentActivityId());
        System.out.println("Activity Name: " + execution.getCurrentActivityName());
        System.out.println("Variables: " + execution.getVariables());
        System.out.println("========== End of Sout Delegate ==========");
    }
}

