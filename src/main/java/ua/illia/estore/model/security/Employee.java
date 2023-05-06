package ua.illia.estore.model.security;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import ua.illia.estore.model.interfaces.Accountable;
import ua.illia.estore.model.management.PaymentAccount;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Data
@Entity
@Table(name = "employees")
public class Employee implements Accountable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employees_pk_sequence_generator")
    @SequenceGenerator(name = "employees_pk_sequence_generator", sequenceName = "employees_pk_sequence", allocationSize = 1)
    private Long id;

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payable_unit_id_sequence_generator")
    @SequenceGenerator(name = "payable_unit_id_sequence_generator", sequenceName = "payable_unit_id_sequence", allocationSize = 1)
    private String payableUid;

    private String firstName;

    private String lastName;

    private String patronymic;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime registrationDate;

    @Column(unique = true)
    private String email;

    private String phoneNumber;

    private boolean locked;

    private String lockReason;

    @ManyToMany
    @JoinTable(name = "employee_groups",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id"))
    @ToString.Exclude
    private List<Group> groups;

    @OneToMany(mappedBy = "employee")
    @LazyCollection(LazyCollectionOption.TRUE)
    @ToString.Exclude
    private List<StoreRoleEntry> storeRoles;

    @JsonIgnore
    private String password;

    private boolean enabled;

    @Column(unique = true)
    private String uid;

    private String authorizationToken;

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
