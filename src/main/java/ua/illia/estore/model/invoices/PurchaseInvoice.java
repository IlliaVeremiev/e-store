package ua.illia.estore.model.invoices;


import lombok.Data;
import lombok.EqualsAndHashCode;
import ua.illia.estore.model.management.Contractor;
import ua.illia.estore.model.management.PaymentAccount;
import ua.illia.estore.model.warehouse.Warehouse;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Data
@Entity
@DiscriminatorValue("PURCHASE")
@EqualsAndHashCode(callSuper = true)
public class PurchaseInvoice extends Invoice {

    public PurchaseInvoice() {
        setInvoiceType("PURCHASE");
    }

    @ManyToOne
    private Contractor contractor;

    @ManyToOne
    private PaymentAccount contractorPaymentAccount;

    @ManyToOne
    private Warehouse warehouse;
}
