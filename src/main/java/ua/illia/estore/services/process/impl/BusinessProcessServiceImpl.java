package ua.illia.estore.services.process.impl;

import com.google.common.collect.ImmutableMap;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ua.illia.estore.configuration.security.EmployeeUserDetails;
import ua.illia.estore.services.process.BusinessProcessService;

import java.util.List;

@Service
public class BusinessProcessServiceImpl implements BusinessProcessService {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Override
    public ProcessInstance start(String name, String businessKey, ImmutableMap<String, Object> variables) {
        return runtimeService.startProcessInstanceByKey(name, businessKey, variables);
    }

    @Override
    public String processMessage(String processId, String businessKey, String message) {
        runtimeService.createMessageCorrelation(message)
                .processInstanceBusinessKey(businessKey)
                .processInstanceId(processId)
                .correlate();
        if ("customerCalledBack".equals(message)) {
            List<Task> tasks = taskService.createTaskQuery().processInstanceId(processId).active().list();
            if (tasks.size() != 1) {
                throw new RuntimeException(tasks.toString());
            }
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            EmployeeUserDetails details = (EmployeeUserDetails) authentication.getDetails();
            taskService.setAssignee(tasks.get(0).getId(), details.getUsername());
            return tasks.get(0).getId();
        }
        return null;
    }
}
