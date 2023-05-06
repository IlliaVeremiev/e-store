package ua.illia.estore.model.management.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum TransactionStatus {
    CREATED,
    PROCESSED,
    CANCELLED,
    ROLLED_BACK
}
