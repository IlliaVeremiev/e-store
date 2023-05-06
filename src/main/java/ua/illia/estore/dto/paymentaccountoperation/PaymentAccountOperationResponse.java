package ua.illia.estore.dto.paymentaccountoperation;

import lombok.Data;
import ua.illia.estore.model.management.enums.PaymentAccountOperationPurposeType;
import ua.illia.estore.model.management.enums.PaymentAccountOperationType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentAccountOperationResponse {

    private Long id;

    private PaymentAccountOperationType operationType;

    private PaymentAccountOperationPurposeType purposeType;

    private String businessKey;

    private BigDecimal amount;

    private BigDecimal resultBalance;

    private LocalDateTime creationDate;

    private Long createdBy;
}
