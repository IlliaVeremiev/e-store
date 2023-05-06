package ua.illia.estore.model.location;

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Entity
@Table(name = "cities")
@TypeDefs(@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class))
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cities_pk_sequence_generator")
    @SequenceGenerator(name = "cities_pk_sequence_generator", sequenceName = "cities_pk_sequence", allocationSize = 1)
    private long id;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Map<String, String> localizedName;

    @Column(unique = true)
    private String npRef;

    private String npArea;

    private String npCityID;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Map<String, String> npAreaDescriptionLocalized;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Map<String, String> npSettlementTypeDescriptionLocalized;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private List<Map<String, Object>> novaPoshtaWarehouses;

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
