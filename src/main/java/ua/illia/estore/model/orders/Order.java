package ua.illia.estore.model.orders;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import ua.illia.estore.model.location.City;
import ua.illia.estore.model.orders.data.OrderItem;
import ua.illia.estore.model.orders.enums.DeliveryType;
import ua.illia.estore.model.orders.enums.OrderStatus;
import ua.illia.estore.model.orders.enums.PaymentMethod;
import ua.illia.estore.model.security.Customer;
import ua.illia.estore.model.security.Employee;

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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Entity
@Table(name = "orders")
@TypeDefs(@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class))
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orders_pk_sequence_generator")
    @SequenceGenerator(name = "orders_pk_sequence_generator", sequenceName = "orders_pk_sequence", allocationSize = 1)
    private long id;

    @Column(unique = true)
    private String uid;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private List<OrderItem> items;

    @ManyToOne
    private Customer customer;

    private String firstName;

    private String lastName;

    private String patronymic;

    private String phoneNumber;

    private String email;

    private String deliveryFirstName;

    private String deliveryLastName;

    private String deliveryPatronymic;

    private String deliveryPhoneNumber;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime statusUpdatedDate;

    @ManyToOne
    private Employee statusUpdatedBy;

    @Enumerated(EnumType.STRING)
    private DeliveryType deliveryMethod;

    @ManyToOne
    private City deliveryCity;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Map<String, String> deliveryInformation;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime creationDate;
}
