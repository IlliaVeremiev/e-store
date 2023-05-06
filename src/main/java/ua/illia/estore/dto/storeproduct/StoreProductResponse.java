package ua.illia.estore.dto.storeproduct;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ua.illia.estore.dto.product.ProductResponse;

@Data
@EqualsAndHashCode(callSuper = true)
public class StoreProductResponse extends ProductResponse {

    private boolean available;
}
