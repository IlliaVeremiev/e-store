package ua.illia.estore.dto.paymentaccount;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentAccountResponse {

    private Long id;

    private String name;

    private BigDecimal balance;

    private String currency;

    private String ownerName;
}
