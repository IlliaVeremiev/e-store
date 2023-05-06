package ua.illia.estore.dto.category;

import lombok.Data;
import ua.illia.estore.model.product.enums.FilterPropertyType;

@Data
public class CategoryClassificationParameterResponse {

    private long id;

    private String name;

    private String key;

    private FilterPropertyType type;

    private String measure;
}
