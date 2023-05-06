package ua.illia.estore.model.product;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import ua.illia.estore.model.product.data.ProductImage;
import ua.illia.estore.model.security.Employee;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@Entity
@Table(name = "products")
@TypeDefs(@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class))
@NamedEntityGraphs(@NamedEntityGraph(name = Product.Graph.BASIC, attributeNodes = {
        @NamedAttributeNode("brand"),
        @NamedAttributeNode(value = "category", subgraph = Category.Graph.BASIC),
        @NamedAttributeNode(value = "productVariantItem", subgraph = ProductVariantItem.Graph.BASIC)
}))
public class Product {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Graph {
        public static final String BASIC = "Product.Basic";
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "products_pk_sequence_generator")
    @SequenceGenerator(name = "products_pk_sequence_generator", sequenceName = "products_pk_sequence", allocationSize = 1)
    private long id;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Map<String, String> localizedName;

    @ManyToOne
    private Brand brand;

    @ManyToOne
    private Category category;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private List<ProductImage> images = new ArrayList<>();

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> classificationParameters;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private List<String> specification;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Map<String, String> localizedDescription;

    private BigDecimal price;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime creationDate;

    @ManyToOne
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    private Employee createdBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastUpdateDate;

    @ManyToOne
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    private Employee updatedBy;

    /**
     * A unique product identifier. F.e. black charger 25W B31A is one sku and white charger 25W B31A is another
     */
    private String stockKeepingUnit;

    /**
     * A unique barcode used to identify the product
     */
    private String universalProductCode;

    /**
     * A unique identifier used by the manufacturer to identify the product.
     */
    private String manufacturerPartNumber;

    private boolean active;

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

    @OneToOne(mappedBy = "product")
    private ProductVariantItem productVariantItem;
}
