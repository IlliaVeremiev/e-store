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
import ua.illia.estore.model.product.enums.FilterPropertyType;
import ua.illia.estore.model.security.Employee;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@Entity
@Table(name = "category_classification_parameters")
@TypeDefs(@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class))
public class CategoryClassificationParameter {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_classification_parameters_pk_sequence_generator")
    @SequenceGenerator(name = "category_classification_parameters_pk_sequence_generator", sequenceName = "category_classification_parameters_pk_sequence", allocationSize = 1)
    private long id;

    @ManyToOne
    private Category category;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Map<String, String> localizedName;

    private String key;

    @Enumerated(EnumType.STRING)
    private FilterPropertyType type;

    private String measure;

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
