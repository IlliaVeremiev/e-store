package ua.illia.estore.dto.receipts;

import lombok.Data;

@Data
public class ReceiptItemCreateForm {

    private long productId;

    private long count;
}
