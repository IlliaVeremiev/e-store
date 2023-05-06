package ua.illia.estore.validation.validators.impl;

import org.springframework.stereotype.Component;
import ua.illia.estore.model.product.Product;
import ua.illia.estore.validation.validators.EntityValidator;

import java.util.Map;

@Component
public class ProductValidator extends EntityValidator<Product> {

    @Override
    public void validate(Product product) {
        notNull(product.getLocalizedName(), "localizedName");
        isTrue(product.getLocalizedName().size() != 0, "Should be provided at least one name", "localizedName");
        for (Map.Entry<String, String> entry : product.getLocalizedName().entrySet()) {
            maxLength(entry.getValue(), 256, "name");
        }
        if (product.getPrice() == null) {
            validationError("Price is mandatory field", "price");
        }
        fitBigDecimal(product.getPrice(), 19, 2, "price");
        notNull(product.getBrand(), "brand");
        notNull(product.getCategory(), "category");
    }
}
