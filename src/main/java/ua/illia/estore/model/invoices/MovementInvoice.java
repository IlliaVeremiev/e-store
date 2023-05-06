package ua.illia.estore.model.invoices;


import lombok.Data;
import lombok.EqualsAndHashCode;
import ua.illia.estore.model.warehouse.Warehouse;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Data
@Entity(name = "MovementInvoice")
@DiscriminatorValue("MOVEMENT")
@EqualsAndHashCode(callSuper = true)
public class MovementInvoice extends Invoice {

    @ManyToOne
    private Warehouse fromWarehouse;

    @ManyToOne
    private Warehouse toWarehouse;
}
