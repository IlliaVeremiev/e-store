package ua.illia.estore.processes.impl.cities;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.illia.estore.services.location.CitiesService;

@Component("synchronizeCitiesDelegateAction")
public class SynchronizeCitiesDelegateAction implements JavaDelegate {

    @Autowired
    private CitiesService citiesService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        citiesService.synchronizeCities();
    }
}
