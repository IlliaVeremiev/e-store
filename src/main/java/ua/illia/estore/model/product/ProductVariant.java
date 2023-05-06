package ua.illia.estore.model.product;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Data;
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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Entity
@Table(name = "product_variants")
@TypeDefs(@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class))
public class ProductVariant {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_variants_pk_sequence_generator")
    @SequenceGenerator(name = "product_variants_pk_sequence_generator", sequenceName = "product_variants_pk_sequence", allocationSize = 1)
    private Long id;


    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Map<String, Map<String, String>> localizedParametersName;

    @OneToMany(mappedBy = "productVariant")
    private List<ProductVariantItem> productVariantItems;

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
