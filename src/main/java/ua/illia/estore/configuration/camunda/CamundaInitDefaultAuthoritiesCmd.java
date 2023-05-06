package ua.illia.estore.configuration.camunda;

import org.camunda.bpm.engine.impl.identity.WritableIdentityProvider;
import org.camunda.bpm.engine.impl.interceptor.Command;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;

public class CamundaInitDefaultAuthoritiesCmd implements Command<Void> {

    @Override
    public Void execute(CommandContext commandContext) {
        WritableIdentityProvider identityProvider = commandContext
                .getWritableIdentityProvider();
        ((CamundaWritableIdentityProvider) identityProvider).createInitialAuthorizations("camunda-admin");
        return null;
    }
}
