package ua.illia.estore.services.invoice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.illia.estore.dto.invoice.InvoiceCreateForm;
import ua.illia.estore.dto.invoice.InvoiceSearchForm;
import ua.illia.estore.model.invoices.Invoice;
import ua.illia.estore.model.invoices.emuns.InvoiceStatus;
import ua.illia.estore.model.product.Product;
import ua.illia.estore.model.security.Employee;

import java.util.List;

public interface InvoiceService {
    Invoice create(InvoiceCreateForm form, Employee employee);

    Invoice getById(long id);

    Page<Invoice> search(InvoiceSearchForm form, Pageable pageable);

    Invoice update(long id, InvoiceCreateForm form, Employee employee);

    Invoice setStatus(long id, InvoiceStatus status, Employee employee);

    List<Invoice> getWithProduct(Product product);
}
