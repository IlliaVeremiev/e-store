package ua.illia.estore.dto.contractor;

import lombok.Data;
import ua.illia.estore.dto.paymentaccount.PaymentAccountResponse;

import java.util.List;

@Data
public class ContractorResponse {

    private Long id;

    private String name;

    private List<PaymentAccountResponse> paymentAccounts;
}
