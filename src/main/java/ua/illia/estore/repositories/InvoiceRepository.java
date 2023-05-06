package ua.illia.estore.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.PagingAndSortingRepository;
import ua.illia.estore.model.invoices.Invoice;

import java.util.List;

public interface InvoiceRepository extends PagingAndSortingRepository<Invoice, Long> {

    Page<Invoice> findAll(Specification<Invoice> specification, Pageable pageable);

    List<Invoice> findAll(Specification<Invoice> specification);
}
