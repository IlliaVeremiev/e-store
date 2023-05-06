package ua.illia.estore.dto.product;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class ProductVariantItemResponse {

    private long id;

    private long productId;

    private Map<String, Object> parametersValues;
}
