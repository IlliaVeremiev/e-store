package ua.illia.estore.validation.validators.impl;

import org.springframework.stereotype.Component;
import ua.illia.estore.model.store.Store;
import ua.illia.estore.validation.validators.EntityValidator;

@Component
public class StoreValidator extends EntityValidator<Store> {

    @Override
    public void validate(Store store) {
        notEmpty(store.getUid(), "uid");
        notEmpty(store.getName(), "name");
    }
}
