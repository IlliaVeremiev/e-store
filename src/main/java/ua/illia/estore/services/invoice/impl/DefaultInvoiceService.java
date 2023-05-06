package ua.illia.estore.services.invoice.impl;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ua.illia.estore.configuration.exceptions.BadRequestException;
import ua.illia.estore.dto.invoice.InvoiceCreateForm;
import ua.illia.estore.dto.invoice.InvoiceSearchForm;
import ua.illia.estore.dto.invoice.PurchaseInvoiceCreateForm;
import ua.illia.estore.injectors.impl.InvoiceInjector;
import ua.illia.estore.model.invoices.Invoice;
import ua.illia.estore.model.invoices.PurchaseInvoice;
import ua.illia.estore.model.invoices.data.InvoiceItem;
import ua.illia.estore.model.invoices.emuns.InvoiceStatus;
import ua.illia.estore.model.management.Bill;
import ua.illia.estore.model.management.enums.Currency;
import ua.illia.estore.model.management.enums.PaymentAccountOperationPurposeType;
import ua.illia.estore.model.product.Product;
import ua.illia.estore.model.security.Employee;
import ua.illia.estore.model.warehouse.Warehouse;
import ua.illia.estore.repositories.BillsRepository;
import ua.illia.estore.repositories.InvoiceRepository;
import ua.illia.estore.search.SpecificationList;
import ua.illia.estore.services.invoice.InvoiceService;
import ua.illia.estore.services.management.PaymentAccountService;
import ua.illia.estore.services.product.ProductService;
import ua.illia.estore.services.warehouse.WarehouseService;
import ua.illia.estore.utils.ServiceUtils;
import ua.illia.estore.validation.validators.impl.InvoiceValidator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Component
public class DefaultInvoiceService implements InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private InvoiceInjector invoiceInjector;

    @Autowired
    private InvoiceValidator invoiceValidator;

    @Autowired
    private ProductService productService;

    @Autowired
    private PaymentAccountService paymentAccountService;

    @Autowired
    private WarehouseService warehouseService;

    @Autowired
    private BillsRepository billsRepository;

    @Override
    @Transactional
    public Invoice create(InvoiceCreateForm form, Employee employee) {
        Invoice invoice;
        if (PurchaseInvoiceCreateForm.class == form.getClass()) {
            invoice = new PurchaseInvoice();
        } else {
            throw new NotImplementedException("Unable to create invoice with type " + form.getType());
        }

        invoiceInjector.inject(invoice, form);
        invoice.setCreatedBy(employee);
        invoice.setCreationDate(LocalDateTime.now());
        invoice.setStatus(InvoiceStatus.DRAFT);
        invoice.setCurrency(Currency.UAH);

        invoiceValidator.validate(invoice);
        return invoiceRepository.save(invoice);
    }

    @Override
    public Invoice getById(long id) {
        return invoiceRepository.findById(id)
                .orElseThrow(ServiceUtils.notFound("Invoice", "id", id));
    }

    @Override
    public Page<Invoice> search(InvoiceSearchForm form, Pageable pageable) {
        SpecificationList<Invoice> specification = new SpecificationList<>();
        if (form.getType() != null) {
            specification.add((r, q, c) -> c.equal(r.get("invoiceType"), form.getType().name()));
        }
        if (StringUtils.isNotEmpty(form.getQuery())) {
            specification.add((r, q, b) -> b.isTrue(b.function("@@", Boolean.class, b.function("to_tsvector", Map.class, r.get("name")), b.function("plainto_tsquery", Map.class, b.literal(form.getQuery())))));
        }
        return invoiceRepository.findAll(specification, pageable);
    }

    @Override
    public Invoice update(long id, InvoiceCreateForm form, Employee employee) {
        Invoice invoice = getById(id);
        invoiceInjector.inject(invoice, form);
        invoice.setUpdatedBy(employee);
        invoice.setLastUpdateDate(LocalDateTime.now());
        invoiceValidator.validate(invoice);
        return invoiceRepository.save(invoice);
    }

    @Override
    @Transactional
    public Invoice setStatus(long id, InvoiceStatus status, Employee employee) {
        Invoice invoice = getById(id);
        if (status == InvoiceStatus.DRAFT && invoice.getStatus() != InvoiceStatus.SENT) {
            throw new BadRequestException("Can't update status from: " + invoice.getStatus(), "invoice.status");
        }
        if (status == InvoiceStatus.SENT && (invoice.getStatus() != InvoiceStatus.DRAFT)) {
            throw new BadRequestException("Can't update status from: " + invoice.getStatus(), "invoice.status");

        }
        if (status == InvoiceStatus.SIGNED && (invoice.getStatus() != InvoiceStatus.SENT)) {
            throw new BadRequestException("Can't update status from: " + invoice.getStatus(), "invoice.status");

        }
        if (status == InvoiceStatus.PROCESSED && (invoice.getStatus() != InvoiceStatus.SIGNED)) {
            throw new BadRequestException("Can't update status from: " + invoice.getStatus(), "invoice.status");

        }
        invoice.setStatus(status);
        if (status == InvoiceStatus.PROCESSED) {
            processInvoice(invoice, employee);
        }
        return invoiceRepository.save(invoice);
    }

    @Override
    public List<Invoice> getWithProduct(Product product) {
        Specification<Invoice> specification = (r, q, b) -> b.equal(b.function("json_extract_path_text", String.class,
                r.get("items"), b.literal("productId")), product.getId());
        return invoiceRepository.findAll(specification);
    }

    private void processInvoice(Invoice invoice, Employee employee) {
        if (invoice.getClass().equals(PurchaseInvoice.class)) {
            PurchaseInvoice purchaseInvoice = (PurchaseInvoice) invoice;
            Warehouse warehouse = purchaseInvoice.getWarehouse();
            for (InvoiceItem item : invoice.getItems()) {
                warehouseService.addProduct(warehouse, productService.getById(item.getProductId()), item.getCount());
            }
            paymentAccountService.makeCredit(purchaseInvoice.getContractorPaymentAccount(), getInvoiceTotal(invoice),
                    PaymentAccountOperationPurposeType.INVOICE, invoice.getId().toString(), employee);

            Bill bill = new Bill();
            bill.setAmount(getInvoiceTotal(invoice));
            bill.setInvoice(invoice);
            bill.setContractor(purchaseInvoice.getContractor());
            bill.setCreationDate(LocalDateTime.now());
            bill.setCreatedBy(employee);
            billsRepository.save(bill);
        }
    }

    private BigDecimal getInvoiceTotal(Invoice invoice) {
        if (invoice.getClass() == PurchaseInvoice.class) {
            return invoice.getItems()
                    .stream()
                    .map(i -> i.getCount().multiply(i.getPrice()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        throw new NotImplementedException("Invoice total for type: " + invoice.getClass().getName() + " not implemented");
    }
}
