package com.vodafone.gno.delegate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.vodafone.kramax.bpm.engine.RuntimeService;
import org.vodafone.kramax.bpm.engine.delegate.DelegateExecution;
import org.vodafone.kramax.bpm.engine.delegate.JavaDelegate;

import java.util.Map;

@Component
public class DummyDelegate implements JavaDelegate{

    @Autowired
    private RuntimeService runtimeService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("+++++++++++++++++++++++++++++++++++++++Value for Dummy Variable is" + execution.getVariable("dummy_variable"));
//        runtimeService.createMessageCorrelation("some_msg").processInstanceBusinessKey("some_bk").correlate();
        Map<String, Object> map = new java.util.HashMap<>(Map.of("desc", "Name: GK | Age: 30 | Location: India"));
        map.put("ticket_description", "some text with sla 3 in it\nAssignee Area: Ethernet\nNetwork Element Type: Router");
//		runtimeService.createProcessInstanceByKey("test_script").setVariables(map).businessKey("some_BK").execute();
        runtimeService.startProcessInstanceByMessage("initiate_ticket_mgmt", "dummy_JIRA_ID", map);
    }
}