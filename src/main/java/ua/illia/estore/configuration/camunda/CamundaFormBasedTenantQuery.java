package ua.illia.estore.configuration.camunda;

import org.camunda.bpm.engine.identity.Tenant;
import org.camunda.bpm.engine.impl.Page;
import org.camunda.bpm.engine.impl.TenantQueryImpl;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;

import java.util.Collections;
import java.util.List;

public class CamundaFormBasedTenantQuery extends TenantQueryImpl {

    @Override
    public long executeCount(CommandContext commandContext) {
        return 0;
    }

    @Override
    public List<Tenant> executeList(CommandContext commandContext, Page page) {
        return Collections.emptyList();
    }

    @Override
    public boolean isRetryable() {
        return false;
    }
}
