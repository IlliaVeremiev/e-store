package ua.illia.estore.model.product;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@Entity
@Table(name = "categories")
@TypeDefs(@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class))
@NamedEntityGraphs(@NamedEntityGraph(name = Category.Graph.BASIC, attributeNodes = {
        @NamedAttributeNode("image"),
        @NamedAttributeNode("icon")
}))
public class Category {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Graph {
        public static final String BASIC = "Category.Basic";
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "categories_pk_sequence_generator")
    @SequenceGenerator(name = "categories_pk_sequence_generator", sequenceName = "categories_pk_sequence", allocationSize = 1)
    private long id;

    @Column(unique = true)
    private String uid;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Map<String, String> localizedName;

    @ManyToOne
    private Image image;

    @ManyToOne
    private Image icon;

    @ManyToOne
    @JsonIgnore
    private Category parent;

    private boolean folder;

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
