package ua.illia.estore.dto.product;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ProductSearchForm {

    private String category;

    private List<Long> brands;

    private String query;

    private Map<String, String> classification;
}
