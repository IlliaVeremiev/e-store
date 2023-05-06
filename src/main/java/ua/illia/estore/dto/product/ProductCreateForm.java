package ua.illia.estore.dto.product;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
public class ProductCreateForm {

    @NotEmpty
    private Map<String, String> localizedName;

    private Long brand;

    private Long category;

    private List<MultipartFile> images;

    private List<String> specification;

    private Map<String, String> localizedDescription;

    private BigDecimal price;

    private Map<String, Object> classificationParameters;

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
