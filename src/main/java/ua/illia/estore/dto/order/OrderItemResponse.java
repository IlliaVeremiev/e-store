package ua.illia.estore.dto.order;

import lombok.Data;
import ua.illia.estore.dto.product.ProductResponse;

import java.math.BigDecimal;

@Data
public class OrderItemResponse {

    private ProductResponse product;

    private BigDecimal price;

    private long count;

    private BigDecimal totalPrice;
}
