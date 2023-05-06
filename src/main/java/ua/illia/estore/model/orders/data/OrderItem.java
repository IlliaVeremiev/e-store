package ua.illia.estore.model.orders.data;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItem {

    private long productId;

    private BigDecimal price;

    private Long count;
}
