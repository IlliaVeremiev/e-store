package ua.illia.estore.configuration.camunda;

import org.camunda.bpm.engine.impl.interceptor.Session;
import org.camunda.bpm.engine.impl.interceptor.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CamundaIdentityProviderSessionFactory implements SessionFactory {

    @Autowired
    private CamundaWritableIdentityProvider camundaWritableIdentityProvider;

    @Override
    public Class<?> getSessionType() {
        return CamundaWritableIdentityProvider.class;
    }

    @Override
    public Session openSession() {
        return camundaWritableIdentityProvider;
    }
}
