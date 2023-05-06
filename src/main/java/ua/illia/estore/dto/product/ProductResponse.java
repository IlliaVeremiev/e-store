package ua.illia.estore.dto.product;

import lombok.Data;
import ua.illia.estore.dto.brands.BrandResponse;
import ua.illia.estore.dto.category.CategoryResponse;
import ua.illia.estore.dto.image.ImageResponse;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
public class ProductResponse {

    private long id;

    private String name;

    private Map<String, String> localizedName;

    private BrandResponse brand;

    private CategoryResponse category;

    private List<ImageResponse> images;

    private Map<String, Object> classificationParameters;

    private List<String> specification;

    private String description;

    private Map<String, String> localizedDescription;

    private ProductVariantResponse productVariant;

    private BigDecimal price;

    private String stockKeepingUnit;

    private String universalProductCode;

    private String manufacturerPartNumber;

    private BigDecimal weight;

    private Integer dimensionLength;

    private Integer dimensionWidth;

    private Integer dimensionHeight;

    private Integer warranty;

    private String countryOfOrigin;

    private String notes;

    private String modelName;

    private String manufacturerCode;

    private String internalCode;
}