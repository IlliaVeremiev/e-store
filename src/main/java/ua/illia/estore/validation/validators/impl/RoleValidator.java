package ua.illia.estore.validation.validators.impl;

import org.springframework.stereotype.Component;
import ua.illia.estore.model.security.Group;
import ua.illia.estore.validation.validators.EntityValidator;

@Component
public class RoleValidator extends EntityValidator<Group> {

    @Override
    public void validate(Group group) {
        notEmpty(group.getName(), "name");
    }
}
