package ua.illia.estore.dto.product;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
public class WarehouseProductResponse extends ProductResponse {

    private BigDecimal count;
}
