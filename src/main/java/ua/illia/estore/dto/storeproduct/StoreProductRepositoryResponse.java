package ua.illia.estore.dto.storeproduct;

import lombok.Data;
import ua.illia.estore.model.product.Product;

import java.math.BigDecimal;

@Data
public class StoreProductRepositoryResponse {

    private Product product;

    private BigDecimal count;
}
