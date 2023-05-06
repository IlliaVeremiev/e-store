package ua.illia.estore.model.management;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import ua.illia.estore.model.management.enums.PaymentAccountOperationPurposeType;
import ua.illia.estore.model.management.enums.PaymentAccountOperationType;
import ua.illia.estore.model.security.Employee;

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
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "payment_account_operations")
public class PaymentAccountOperation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payment_account_operations_pk_sequence_generator")
    @SequenceGenerator(name = "payment_account_operations_pk_sequence_generator", sequenceName = "payment_account_operations_pk_sequence", allocationSize = 1)
    private Long id;

    @ManyToOne
    private PaymentAccount paymentAccount;

    @Enumerated(EnumType.STRING)
    private PaymentAccountOperationType operationType;

    @Enumerated(EnumType.STRING)
    private PaymentAccountOperationPurposeType purposeType;

    private String businessKey;

    private BigDecimal amount;

    private BigDecimal resultBalance;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime creationDate;

    @ManyToOne
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    private Employee createdBy;
}
