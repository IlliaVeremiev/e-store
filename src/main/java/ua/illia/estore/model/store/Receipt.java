package ua.illia.estore.model.store;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import ua.illia.estore.model.security.Employee;
import ua.illia.estore.model.store.data.ReceiptItem;
import ua.illia.estore.model.store.enums.StorePaymentType;

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
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "receipts")
@TypeDefs(@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class))
public class Receipt {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "receipts_pk_sequence_generator")
    @SequenceGenerator(name = "receipts_pk_sequence_generator", sequenceName = "receipts_pk_sequence", allocationSize = 1)
    private Long id;

    @Column(unique = true)
    private String uuid;

    private int number;

    @ManyToOne
    private KassaSession kassaSession;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private List<ReceiptItem> items;

    @ManyToOne
    private Store store;

    @ManyToOne
    private Employee employee;

    private LocalDateTime creationDate;

    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    private StorePaymentType paymentType;
}
