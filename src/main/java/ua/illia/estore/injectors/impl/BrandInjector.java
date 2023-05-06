package ua.illia.estore.injectors.impl;

import org.springframework.stereotype.Component;
import ua.illia.estore.injectors.EntityDataInjector;
import ua.illia.estore.model.product.Brand;

import java.util.function.BiConsumer;

@Component
public class BrandInjector extends EntityDataInjector<Brand> {

    public BrandInjector() {
        fieldInjectors.put("name", (BiConsumer<Brand, String>) Brand::setName);
        fieldInjectors.put("uid", (BiConsumer<Brand, String>) Brand::setUid);
    }
}
