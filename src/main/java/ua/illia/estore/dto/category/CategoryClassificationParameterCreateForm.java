package ua.illia.estore.dto.category;

import lombok.Data;
import ua.illia.estore.model.product.enums.FilterPropertyType;

import java.util.Map;

@Data
public class CategoryClassificationParameterCreateForm {
    private Map<String, String> localizedName;
    private String key;
    private FilterPropertyType type;
    private String measure;
}
