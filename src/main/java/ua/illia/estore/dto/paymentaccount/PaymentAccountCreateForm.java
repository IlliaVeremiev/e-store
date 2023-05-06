package ua.illia.estore.dto.paymentaccount;

import lombok.Data;
import ua.illia.estore.model.management.enums.Currency;

@Data
public class PaymentAccountCreateForm {

    private String name;

    private Currency currency;

    private Long ownerId;
}
