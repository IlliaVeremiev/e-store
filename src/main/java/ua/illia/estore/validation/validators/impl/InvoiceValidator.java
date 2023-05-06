package ua.illia.estore.validation.validators.impl;

import org.springframework.stereotype.Component;
import ua.illia.estore.model.invoices.Invoice;
import ua.illia.estore.validation.validators.EntityValidator;

@Component
public class InvoiceValidator extends EntityValidator<Invoice> {

    @Override
    public void validate(Invoice invoice) {
        notEmpty(invoice.getName(), "invoice.name");
    }
}
