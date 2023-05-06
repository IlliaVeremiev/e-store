package ua.illia.estore.validation.validators.impl;

import org.springframework.stereotype.Component;
import ua.illia.estore.model.management.Contractor;
import ua.illia.estore.validation.validators.EntityValidator;

@Component
public class ContractorValidator extends EntityValidator<Contractor> {

    @Override
    public void validate(Contractor contractor) {
        notEmpty(contractor.getName(), "name");
        maxLength(contractor.getName(), 64, "name");

    }
}
