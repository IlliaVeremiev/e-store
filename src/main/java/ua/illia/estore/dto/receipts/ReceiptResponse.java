package ua.illia.estore.dto.receipts;

import lombok.Data;
import ua.illia.estore.dto.employee.EmployeeResponse;
import ua.illia.estore.dto.store.StoreResponse;
import ua.illia.estore.model.store.enums.StorePaymentType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ReceiptResponse {

    private Long id;

    private Long kassaSessionId;

    private String uuid;

    private int number;

    private List<ReceiptItemResponse> items;

    private StoreResponse store;

    private EmployeeResponse employee;

    private LocalDateTime creationDate;

    private BigDecimal total;

    private StorePaymentType paymentType;
}
