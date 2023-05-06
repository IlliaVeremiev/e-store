package ua.illia.estore.model.product;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
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
import java.time.LocalDateTime;
import java.util.Map;

@Data
@Entity
@Table(name = "product_variant_items")
@TypeDefs(@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class))
@ToString(of = "id")
@NamedEntityGraphs(@NamedEntityGraph(name = ProductVariantItem.Graph.BASIC, attributeNodes = {
        @NamedAttributeNode("product"),
        @NamedAttributeNode("productVariant"),
        @NamedAttributeNode("createdBy"),
        @NamedAttributeNode("updatedBy")
}))
public class ProductVariantItem {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Graph {
        public static final String BASIC = "ProductVariantItem.Basic";
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_variant_items_pk_sequence_generator")
    @SequenceGenerator(name = "product_variant_items_pk_sequence_generator", sequenceName = "product_variant_items_pk_sequence", allocationSize = 1)
    private Long id;

    @OneToOne
    @JsonIgnore
    private Product product;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> parametersValues;

    @ManyToOne
    @JsonIgnore
    private ProductVariant productVariant;

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
}
