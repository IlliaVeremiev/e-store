package ua.illia.estore.dto.invoice;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PurchaseInvoiceResponse extends InvoiceResponse {

    private long contractor;

    private long contractorPaymentAccount;

    private long warehouse;
}
