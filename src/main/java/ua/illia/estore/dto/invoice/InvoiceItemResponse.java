package ua.illia.estore.dto.invoice;

import lombok.Data;
import ua.illia.estore.dto.product.ProductResponse;

import java.math.BigDecimal;

@Data
public class InvoiceItemResponse {

    private ProductResponse product;

    private BigDecimal count;

    private BigDecimal price;
}
