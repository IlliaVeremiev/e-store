package ua.illia.estore.validation.validators.impl;

import org.springframework.stereotype.Component;
import ua.illia.estore.model.product.Brand;
import ua.illia.estore.validation.validators.EntityValidator;

@Component
public class BrandValidator extends EntityValidator<Brand> {

    @Override
    public void validate(Brand brand) {
        notEmpty(brand.getName(), "name");
        maxLength(brand.getName(), 64, "name");
    }
}
