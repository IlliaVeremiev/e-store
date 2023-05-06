package ua.illia.estore.dto.invoice;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
@JsonSubTypes(@JsonSubTypes.Type(value = PurchaseInvoiceCreateForm.class, name = "PURCHASE"))
public abstract class InvoiceCreateForm {

    private String type;

    private String name;

    private LocalDate date;

    private List<InvoiceItemCreateForm> items;
}
