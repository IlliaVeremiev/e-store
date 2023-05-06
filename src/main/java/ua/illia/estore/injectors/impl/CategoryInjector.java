package ua.illia.estore.injectors.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.illia.estore.injectors.EntityDataInjector;
import ua.illia.estore.model.product.Category;
import ua.illia.estore.services.product.CategoryService;

import java.util.Map;
import java.util.function.BiConsumer;

@Component
public class CategoryInjector extends EntityDataInjector<Category> {

    @Autowired
    private CategoryService categoryService;

    public CategoryInjector() {
        fieldInjectors.put("localizedName", (BiConsumer<Category, Map<String, String>>) Category::setLocalizedName);
        fieldInjectors.put("uid", (BiConsumer<Category, String>) Category::setUid);
        fieldInjectors.put("parentId", (BiConsumer<Category, Number>) this::setParent);
        fieldInjectors.put("folder", (BiConsumer<Category, Boolean>) Category::setFolder);

        //TODO should be processed in service and removed from here exactly --> fieldInjectors.put("classificationParameters", (BiConsumer<Category, List<Map<String, String>>>) (a, b) -> {
    }

    private void setParent(Category category, Number id) {
        if (id == null) {
            return;
        }
        Category parent = categoryService.getById(id.longValue());
        category.setParent(parent);
    }
}
