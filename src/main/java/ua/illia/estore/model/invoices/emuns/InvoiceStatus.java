package ua.illia.estore.model.invoices.emuns;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum InvoiceStatus {
    DRAFT,
    SENT,
    SIGNED,
    PROCESSED
}
