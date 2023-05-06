package ua.illia.estore.configuration.camunda;

import ua.illia.estore.model.security.Group;

public class CamundaGroup implements org.camunda.bpm.engine.identity.Group {

    private final Group group;

    public CamundaGroup(Group group) {
        this.group = group;
    }

    @Override
    public String getId() {
        return group.getUid();
    }

    @Override
    public void setId(String id) {
    }

    @Override
    public String getName() {
        return group.getName();
    }

    @Override
    public void setName(String name) {
    }

    @Override
    public String getType() {
        return group.getType();
    }

    @Override
    public void setType(String string) {
    }
}
