package ua.illia.estore.services.process;

import com.google.common.collect.ImmutableMap;
import org.camunda.bpm.engine.runtime.ProcessInstance;

public interface BusinessProcessService {

    ProcessInstance start(String name, String businessKey, ImmutableMap<String, Object> variables);

    String processMessage(String processId, String businessKey, String message);
}
