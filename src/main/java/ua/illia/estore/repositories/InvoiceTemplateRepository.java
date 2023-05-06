package ua.illia.estore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.illia.estore.model.invoices.InvoiceTemplate;

import java.util.Optional;

public interface InvoiceTemplateRepository extends JpaRepository<InvoiceTemplate, Long> {

    Optional<InvoiceTemplate> getByUid(String uid);
}
