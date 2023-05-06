package ua.illia.estore.configuration.camunda;

import org.camunda.bpm.engine.impl.GroupQueryImpl;
import org.camunda.bpm.engine.impl.Page;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;
import ua.illia.estore.model.security.Group;
import ua.illia.estore.services.management.GroupService;

import java.util.List;
import java.util.stream.Collectors;

public class CamundaFormBasedGroupQuery extends GroupQueryImpl {

    private final GroupService groupService;
    private List<Group> result;

    public CamundaFormBasedGroupQuery(GroupService groupService) {
        this.groupService = groupService;
    }

    @Override
    public long executeCount(CommandContext commandContext) {
        if (result == null) {
            result = groupService.searchAll(this);
        }
        return result.size();
    }

    @Override
    public List<org.camunda.bpm.engine.identity.Group> executeList(CommandContext commandContext, Page page) {
        if (result == null) {
            result = groupService.searchAll(this);
        }
        return result.stream().map(CamundaGroup::new).map(a -> (org.camunda.bpm.engine.identity.Group) a).collect(Collectors.toList());
    }

    @Override
    public boolean isRetryable() {
        return false;
    }
}
