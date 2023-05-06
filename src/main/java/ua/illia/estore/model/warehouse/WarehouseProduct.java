package ua.illia.estore.model.warehouse;

import lombok.Data;
import ua.illia.estore.model.product.Product;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "warehouse_products")
public class WarehouseProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "warehouse_products_pk_sequence_generator")
    @SequenceGenerator(name = "warehouse_products_pk_sequence_generator", sequenceName = "warehouse_products_pk_sequence", allocationSize = 1)
    private Long id;

    @ManyToOne
    private Warehouse warehouse;

    private BigDecimal count;

    @ManyToOne
    private Product product;
}
