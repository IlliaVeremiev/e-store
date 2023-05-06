package ua.illia.estore.injectors.impl;

import org.springframework.stereotype.Component;
import ua.illia.estore.injectors.EntityDataInjector;
import ua.illia.estore.model.security.Group;
import ua.illia.estore.model.security.enums.Authority;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RoleInjector extends EntityDataInjector<Group> {

    public RoleInjector() {
        field("name", Group::setName);
        field("uid", Group::setUid);
        field("type", Group::setType);
        field("description", Group::setDescription);
        field("authorities", this::setAuthorities);
    }

    private void setAuthorities(Group group, List<String> authorities) {
        group.setAuthorities(authorities.stream().map(Authority::valueOf).collect(Collectors.toSet()));
    }
}
