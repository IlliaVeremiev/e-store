package ua.illia.estore.dto.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ua.illia.estore.dto.cities.CityResponse;
import ua.illia.estore.dto.customer.CustomerResponse;
import ua.illia.estore.dto.employee.EmployeeResponse;
import ua.illia.estore.model.orders.enums.DeliveryType;
import ua.illia.estore.model.orders.enums.OrderStatus;
import ua.illia.estore.model.orders.enums.PaymentMethod;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
public class OrderResponse {

    private long id;

    private String uid;

    private List<OrderItemResponse> orderItems;

    private CustomerResponse customer;

    private String firstName;

    private String lastName;

    private String patronymic;

    private String phoneNumber;

    private String email;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime creationDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime statusUpdatedDate;

    private EmployeeResponse statusUpdatedBy;

    private DeliveryType deliveryMethod;

    private CityResponse deliveryCity;

    private Map<String, String> deliveryInformation;

    private PaymentMethod paymentMethod;

    private BigDecimal totalPrice;

    private String deliveryFirstName;

    private String deliveryLastName;

    private String deliveryPatronymic;

    private String deliveryPhoneNumber;
}
