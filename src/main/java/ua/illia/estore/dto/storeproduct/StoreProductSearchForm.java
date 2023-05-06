package ua.illia.estore.dto.storeproduct;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class StoreProductSearchForm {

    private Long category;

    private List<Long> brands;

    private BigDecimal minPrice;

    private BigDecimal maxPrice;

    private String query;

    private List<Long> ids;

    private String sortOrder;

    private List<Boolean> inStock;
}
