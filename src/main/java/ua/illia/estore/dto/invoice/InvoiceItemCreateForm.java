package ua.illia.estore.dto.invoice;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class InvoiceItemCreateForm {

    private long product;

    private BigDecimal count;

    private BigDecimal price;
}
