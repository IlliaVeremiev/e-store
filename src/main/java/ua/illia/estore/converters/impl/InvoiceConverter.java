package ua.illia.estore.converters.impl;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.illia.estore.converters.EntityConverter;
import ua.illia.estore.dto.invoice.InvoiceResponse;
import ua.illia.estore.dto.invoice.PurchaseInvoiceResponse;
import ua.illia.estore.model.invoices.Invoice;
import ua.illia.estore.model.invoices.PurchaseInvoice;

import java.util.stream.Collectors;

@Component
public class InvoiceConverter implements EntityConverter<Invoice, InvoiceResponse> {

    @Autowired
    private InvoiceItemConverter invoiceItemConverter;

    private PurchaseInvoiceResponse purchaseInvoiceResponse(PurchaseInvoice invoice) {
        PurchaseInvoiceResponse dto = new PurchaseInvoiceResponse();
        dto.setInvoiceType(invoice.getInvoiceType());
        dto.setId(invoice.getId());
        dto.setName(invoice.getName());
        dto.setCreationDate(invoice.getCreationDate());
        dto.setDate(invoice.getDate());
        dto.setCreatedBy(invoice.getCreatedBy().getId());
        dto.setItems(invoice.getItems().stream().map(invoiceItemConverter::convert).collect(Collectors.toList()));
        dto.setStatus(invoice.getStatus());
        dto.setCurrency(invoice.getCurrency().getCode());
        dto.setContractor(invoice.getContractor().getId());
        dto.setContractorPaymentAccount(invoice.getContractorPaymentAccount().getId());
        dto.setWarehouse(invoice.getWarehouse().getId());
        return dto;
    }

    @Override
    public InvoiceResponse convert(Invoice invoice, InvoiceResponse dto) {
        if (invoice.getInvoiceType().equals("PURCHASE")) {
            return purchaseInvoiceResponse((PurchaseInvoice) invoice);
        }
        throw new NotImplementedException("Converter for " + invoice.getInvoiceType() + " not implemented");
    }

    @Override
    public InvoiceResponse convert(Invoice invoice) {
        return convert(invoice, new InvoiceResponse());
    }
}
