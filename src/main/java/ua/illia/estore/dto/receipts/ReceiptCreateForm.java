package ua.illia.estore.dto.receipts;

import lombok.Data;
import ua.illia.estore.model.store.enums.StorePaymentType;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ReceiptCreateForm {

    private String uuid;

    private int number;

    private long kassaSessionId;

    private List<ReceiptItemCreateForm> items;

    private long storeId;

    private long employeeId;

    private LocalDateTime creationDate;

    private StorePaymentType paymentType;
}
