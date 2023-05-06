package ua.illia.estore.dto.invoice;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PurchaseInvoiceCreateForm extends InvoiceCreateForm {

    private long warehouse;

    private long contractor;

    private long contractorPaymentAccount;
}
