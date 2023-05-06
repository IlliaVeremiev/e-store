package ua.illia.estore.dto.transaction;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class TransactionCreateForm {

    private long from;

    private long to;

    private BigDecimal fromOutcomeAmount;

    private BigDecimal toIncomeAmount;

    private String description;

    private BigDecimal commission;

    private BigDecimal exchangeRate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
}
