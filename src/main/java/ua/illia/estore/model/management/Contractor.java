package ua.illia.estore.model.management;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import ua.illia.estore.model.interfaces.Accountable;
import ua.illia.estore.model.security.Employee;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "contractors")
public class Contractor implements Accountable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stakeholders_pk_sequence_generator")
    @SequenceGenerator(name = "stakeholders_pk_sequence_generator", sequenceName = "stakeholders_pk_sequence", allocationSize = 1)
    private Long id;

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payable_unit_id_sequence_generator")
    @SequenceGenerator(name = "payable_unit_id_sequence_generator", sequenceName = "payable_unit_id_sequence", allocationSize = 1)
    private String payableUid;

    @Column(unique = true)
    private String name;

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

    @OneToMany
    @JoinColumn(name = "ownerPayableUid", referencedColumnName = "payableUid")
    private List<PaymentAccount> paymentAccounts;
}
