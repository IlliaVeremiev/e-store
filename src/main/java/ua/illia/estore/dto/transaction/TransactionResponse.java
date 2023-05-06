package ua.illia.estore.dto.transaction;

import lombok.Data;
import ua.illia.estore.model.management.enums.TransactionStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class TransactionResponse {

    private long id;

    private BigDecimal debitedAmount;

    private String debitedCurrency;

    private BigDecimal creditedAmount;

    private String creditedCurrency;

    private BigDecimal commission;

    private BigDecimal exchangeRate;

    private TransactionStatus status;

    private String fromPayableUid;

    private String fromName;

    private String toPayableUid;

    private String toName;

    private String description;

    private String image;

    private LocalDate date;

    private LocalDateTime creationDate;

    private long createdBy;
}
