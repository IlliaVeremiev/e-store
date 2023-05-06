package ua.illia.estore.dto.storeproduct;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PriceBounds {

    private BigDecimal minPrice;

    private BigDecimal maxPrice;
}