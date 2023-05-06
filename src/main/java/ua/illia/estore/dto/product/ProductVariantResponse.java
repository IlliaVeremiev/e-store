package ua.illia.estore.dto.product;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ProductVariantResponse {

    private long id;

    private Map<String, String> parametersName;

    private List<ProductVariantItemResponse> productVariantItems;
}
