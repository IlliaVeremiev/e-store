package ua.illia.estore.model.management;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import ua.illia.estore.model.management.enums.Currency;
import ua.illia.estore.model.management.enums.TransactionStatus;
import ua.illia.estore.model.product.Image;
import ua.illia.estore.model.security.Employee;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_pk_sequence_generator")
    @SequenceGenerator(name = "transaction_pk_sequence_generator", sequenceName = "transaction_pk_sequence", allocationSize = 1)
    private Long id;

    private BigDecimal fromOutcomeAmount;

    @Enumerated(EnumType.STRING)
    private Currency fromCurrency;

    private BigDecimal toIncomeAmount;

    @Enumerated(EnumType.STRING)
    private Currency toCurrency;

    private BigDecimal commission;

    private BigDecimal exchangeRate;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    private String senderPayableUid;

    @ManyToOne
    private PaymentAccount senderPaymentAccount;

    private String receiverPayableUid;

    @ManyToOne
    private PaymentAccount receiverPaymentAccount;

    private String description;

    @OneToOne
    private Image image;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

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
