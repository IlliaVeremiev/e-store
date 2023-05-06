package ua.illia.estore.model.security;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import ua.illia.estore.model.security.enums.StoreAuthority;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Entity
@Table(name = "store_roles")
public class StoreRole {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "groups_pk_sequence_generator")
    @SequenceGenerator(name = "groups_pk_sequence_generator", sequenceName = "groups_pk_sequence", allocationSize = 1)
    private long id;

    @Column(unique = true)
    private String name;

    private String description;

    @ElementCollection(targetClass = StoreAuthority.class, fetch = FetchType.EAGER)
    @Column(name = "store_authority")
    @JoinTable(name = "store_role_authorities", joinColumns = @JoinColumn(name = "store_role_id"))
    @Enumerated(EnumType.STRING)
    private Set<StoreAuthority> authorities;

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
