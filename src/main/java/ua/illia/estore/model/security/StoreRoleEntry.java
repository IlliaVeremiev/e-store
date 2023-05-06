package ua.illia.estore.model.security;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import ua.illia.estore.model.store.Store;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "store_role_entries")
public class StoreRoleEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "group_entries_pk_sequence_generator")
    @SequenceGenerator(name = "group_entries_pk_sequence_generator", sequenceName = "group_entries_pk_sequence", allocationSize = 1)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    private Store store;

    @ManyToOne(fetch = FetchType.EAGER)
    private StoreRole role;

    @ManyToOne(fetch = FetchType.EAGER)
    private Employee employee;

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
