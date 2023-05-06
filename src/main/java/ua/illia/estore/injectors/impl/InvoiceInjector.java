package ua.illia.estore.injectors.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.illia.estore.dto.invoice.InvoiceItemCreateForm;
import ua.illia.estore.injectors.EntityDataInjector;
import ua.illia.estore.model.invoices.Invoice;
import ua.illia.estore.model.invoices.PurchaseInvoice;
import ua.illia.estore.model.invoices.data.InvoiceItem;
import ua.illia.estore.model.management.enums.Currency;
import ua.illia.estore.services.invoice.ContractorService;
import ua.illia.estore.services.management.PaymentAccountService;
import ua.illia.estore.services.warehouse.WarehouseService;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Component
public class InvoiceInjector extends EntityDataInjector<Invoice> {

    @Autowired
    private WarehouseService warehouseService;

    @Autowired
    private ContractorService contractorService;

    @Autowired
    private PaymentAccountService paymentAccountService;

    public InvoiceInjector() {
        field("name", Invoice::setName);
        field("date", this::setDate);
        field("items", this::setItems);
        field("currency", this::setCurrency);
        field("warehouse", this::setWarehouse);
        field("contractor", this::setContractor);
        field("contractorPaymentAccount", this::setContractorPaymentAccount);
        field("type", (a, b) -> {
        });
    }

    private void setDate(Invoice invoice, Object date) {
        invoice.setDate(LocalDate.parse(date.toString()));
    }

    private void setItems(Invoice invoice, List<InvoiceItemCreateForm> items) {
        invoice.setItems(items.stream().map(map -> {
            InvoiceItem item = new InvoiceItem();
            item.setCount(map.getCount());
            item.setPrice(map.getPrice());
            item.setProductId(map.getProduct());
            return item;
        }).collect(Collectors.toList()));
    }

    private void setCurrency(Invoice invoice, String currencyCode) {
        invoice.setCurrency(Currency.valueOf(currencyCode.toUpperCase(Locale.ROOT)));
    }

    private void setWarehouse(Invoice invoice, Object warehouseId) {
        if (invoice.getInvoiceType().equals("PURCHASE")) {
            ((PurchaseInvoice) invoice).setWarehouse(warehouseService.getById(Long.parseLong(warehouseId.toString())));
        }
    }

    private void setContractor(Invoice invoice, Object contractorId) {
        if (invoice.getInvoiceType().equals("PURCHASE")) {
            ((PurchaseInvoice) invoice).setContractor(contractorService.getById(Long.parseLong(contractorId.toString())));
        }
    }

    private void setContractorPaymentAccount(Invoice invoice, Object paymentAccountId) {
        if (invoice.getInvoiceType().equals("PURCHASE")) {
            ((PurchaseInvoice) invoice).setContractorPaymentAccount(paymentAccountService.getById(Long.parseLong(paymentAccountId.toString())));
        }
    }
}
