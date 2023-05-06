package ua.illia.estore.dto.storeproduct;

import lombok.Data;
import ua.illia.estore.model.product.Category;

@Data
public class CategoryMatch {

    private Category category;

    private boolean matches;
}
