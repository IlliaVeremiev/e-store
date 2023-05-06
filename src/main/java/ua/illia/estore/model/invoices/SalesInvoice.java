package ua.illia.estore.model.invoices;


import lombok.Data;
import lombok.EqualsAndHashCode;
import ua.illia.estore.model.management.Contractor;
import ua.illia.estore.model.warehouse.Warehouse;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Data
@Entity
@DiscriminatorValue("SALES")
@EqualsAndHashCode(callSuper = true)
public class SalesInvoice extends Invoice {

    @ManyToOne
    private Contractor contractor;

    @ManyToOne
    private Warehouse warehouse;
}
