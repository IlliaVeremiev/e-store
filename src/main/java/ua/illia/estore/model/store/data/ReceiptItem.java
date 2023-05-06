package ua.illia.estore.model.store.data;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ReceiptItem {

    private long productId;

    private long count;

    private BigDecimal basePrice;

    private String discount;

    private BigDecimal totalPrice;
}
