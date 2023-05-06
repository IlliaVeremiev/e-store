package ua.illia.estore.injectors.impl;

import org.springframework.stereotype.Component;
import ua.illia.estore.injectors.EntityDataInjector;
import ua.illia.estore.model.management.Contractor;

import java.util.function.BiConsumer;

@Component
public class ContractorInjector extends EntityDataInjector<Contractor> {

    public ContractorInjector() {
        fieldInjectors.put("name", (BiConsumer<Contractor, String>) Contractor::setName);
    }
}
