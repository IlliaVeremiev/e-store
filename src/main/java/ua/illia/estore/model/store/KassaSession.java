package ua.illia.estore.model.store;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import ua.illia.estore.model.security.Employee;
import ua.illia.estore.model.store.enums.KassaSessionState;

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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "kassa_sessions")
public class KassaSession {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "kassa_sessions_pk_sequence_generator")
    @SequenceGenerator(name = "kassa_sessions_pk_sequence_generator", sequenceName = "kassa_sessions_pk_sequence", allocationSize = 1)
    private Long id;

    @Column(unique = true)
    private String uuid;

    @ManyToOne
    private Employee employee;

    @ManyToOne
    private Store store;

    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private KassaSessionState state;

    private LocalDateTime openDateTime;

    private BigDecimal openCacheAmount;

    private LocalDateTime closeDateTime;

    private BigDecimal closeCacheAmount;

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
