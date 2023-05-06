package ua.illia.estore.dto.invoice;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ua.illia.estore.model.invoices.emuns.InvoiceStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class InvoiceResponse {

    private String invoiceType;

    private long id;

    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime creationDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    private long createdBy;

    private List<InvoiceItemResponse> items;

    private InvoiceStatus status;

    private String currency;
}
