package ua.illia.estore.configuration.camunda;

import org.camunda.bpm.engine.AuthorizationService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.impl.ServiceImpl;
import org.camunda.bpm.engine.impl.cfg.AbstractProcessEnginePlugin;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.identity.ReadOnlyIdentityProvider;
import org.camunda.bpm.engine.impl.identity.WritableIdentityProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomIdentityProviderPlugin extends AbstractProcessEnginePlugin {

    @Autowired
    private CamundaIdentityProviderSessionFactory camundaIdentityProviderSessionFactory;

    @Override
    public void postInit(ProcessEngineConfigurationImpl processEngineConfiguration) {
        processEngineConfiguration.getSessionFactories().put(WritableIdentityProvider.class, camundaIdentityProviderSessionFactory);
        processEngineConfiguration.getSessionFactories().put(ReadOnlyIdentityProvider.class, camundaIdentityProviderSessionFactory);
    }

    @Override
    public void postProcessEngineBuild(ProcessEngine processEngine) {
        AuthorizationService authorizationService = processEngine.getAuthorizationService();
        if (authorizationService.createAuthorizationQuery().groupIdIn("camunda-admin").count() == 0) {
            ((ServiceImpl) processEngine.getIdentityService()).getCommandExecutor().execute(new CamundaInitDefaultAuthoritiesCmd());
        }
    }
}
