package ua.illia.estore.model.invoices.data;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class InvoiceItem {

    private long productId;

    private BigDecimal count;

    private BigDecimal price;
}
