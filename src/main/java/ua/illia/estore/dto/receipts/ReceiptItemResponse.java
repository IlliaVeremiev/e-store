package ua.illia.estore.dto.receipts;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ReceiptItemResponse {

    private long productId;

    private long count;

    private BigDecimal basePrice;

    private String discount;

    private BigDecimal totalPrice;
}
