package ua.illia.estore.dto.invoice;

import lombok.Data;
import ua.illia.estore.model.invoices.emuns.InvoiceType;

@Data
public class InvoiceSearchForm {

    private InvoiceType type;

    private String query;
}
