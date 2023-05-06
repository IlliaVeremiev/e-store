package ua.illia.estore.converters.impl;

import org.springframework.stereotype.Component;
import ua.illia.estore.dto.role.GroupResponse;
import ua.illia.estore.model.security.Group;
import ua.illia.estore.model.security.enums.Authority;

import java.util.stream.Collectors;

@Component
public class GroupConverter {

    public GroupResponse roleResponse(Group group) {
        GroupResponse response = new GroupResponse();
        response.setId(group.getId());
        response.setName(group.getName());
        response.setDescription(group.getDescription());
        response.setAuthorities(group.getAuthorities().stream().map(Authority::getAuthority).collect(Collectors.toList()));
        response.setUid(group.getUid());
        response.setSystem(group.isSystem());
        response.setType(group.getType());
        return response;
    }
}
