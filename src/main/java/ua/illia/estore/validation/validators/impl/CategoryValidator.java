package ua.illia.estore.validation.validators.impl;

import org.springframework.stereotype.Component;
import ua.illia.estore.model.product.Category;
import ua.illia.estore.validation.validators.EntityValidator;

import java.util.Map;

@Component
public class CategoryValidator extends EntityValidator<Category> {

    @Override
    public void validate(Category category) {
        notNull(category.getLocalizedName(), "localizedName");
        isTrue(category.getLocalizedName().size() != 0, "Should be provided at least one name", "localizedName");
        for (Map.Entry<String, String> entry : category.getLocalizedName().entrySet()) {
            maxLength(entry.getValue(), 64, "name");
        }
        notEmpty(category.getUid(), "uid");
        if (category.getParent() != null) {
            isTrue(category.getParent().isFolder(), "Parent category should be folder", "parent");
        }
    }
}
