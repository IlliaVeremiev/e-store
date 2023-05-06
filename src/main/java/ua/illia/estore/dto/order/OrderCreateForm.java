package ua.illia.estore.dto.order;

import lombok.Data;
import ua.illia.estore.model.orders.enums.DeliveryType;
import ua.illia.estore.model.orders.enums.PaymentMethod;

import javax.validation.constraints.NotNull;
import java.util.Map;

@Data
public class OrderCreateForm {

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    private String patronymic;

    @NotNull
    private String phoneNumber;

    @NotNull
    private String email;

    private Map<Long, Long> orderItems;

    @NotNull
    private DeliveryType deliveryMethod;

    @NotNull
    private Map<String, String> deliveryInformation;

    @NotNull
    private long deliveryCity;

    @NotNull
    private PaymentMethod paymentMethod;

    @NotNull
    private String deliveryFirstName;

    @NotNull
    private String deliveryLastName;

    private String deliveryPatronymic;

    @NotNull
    private String deliveryPhoneNumber;
}
