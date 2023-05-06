package ua.illia.estore.services.invoice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.illia.estore.dto.contractor.ContractorCreateForm;
import ua.illia.estore.dto.contractor.ContractorSearchForm;
import ua.illia.estore.model.management.Contractor;
import ua.illia.estore.model.security.Employee;

public interface ContractorService {

    Contractor create(ContractorCreateForm form, Employee employee);

    Contractor getById(long id);

    Page<Contractor> search(ContractorSearchForm form, Pageable pageable);

    Contractor save(Contractor contractor);
}
