package ua.illia.estore.services.invoice.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.illia.estore.dto.contractor.ContractorCreateForm;
import ua.illia.estore.dto.contractor.ContractorSearchForm;
import ua.illia.estore.injectors.impl.ContractorInjector;
import ua.illia.estore.model.management.Contractor;
import ua.illia.estore.model.security.Employee;
import ua.illia.estore.repositories.ContractorRepository;
import ua.illia.estore.search.SpecificationList;
import ua.illia.estore.services.invoice.ContractorService;
import ua.illia.estore.services.management.PaymentAccountService;
import ua.illia.estore.utils.ServiceUtils;
import ua.illia.estore.validation.validators.impl.ContractorValidator;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Service
public class DefaultContractorService implements ContractorService {

    @Autowired
    private ContractorRepository contractorRepository;

    @Autowired
    private ContractorInjector contractorInjector;

    @Autowired
    private ContractorValidator contractorValidator;

    @Autowired
    private PaymentAccountService paymentAccountService;

    @Value("${config.contractor.defaultCurrencyCode}")
    private String defaultContractorCurrencyCode;

    @Override
    public Contractor create(ContractorCreateForm form, Employee employee) {
        Contractor contractor = new Contractor();
        contractorInjector.inject(contractor, form);
        contractor.setPayableUid(UUID.randomUUID().toString());
        contractor.setCreatedBy(employee);
        contractor.setCreationDate(LocalDateTime.now());
        contractorValidator.validate(contractor);
        contractorRepository.save(contractor);
        return contractor;
    }

    @Override
    public Contractor getById(long id) {
        return contractorRepository.findById(id).orElseThrow(ServiceUtils.notFound("Contractor", "id", id));
    }

    @Override
    public Page<Contractor> search(ContractorSearchForm form, Pageable pageable) {
        SpecificationList<Contractor> specification = new SpecificationList<>();
        if (StringUtils.isNotEmpty(form.getQuery())) {
            specification.add((r, q, b) -> b.isTrue(b.function("@@", Boolean.class, b.function("to_tsvector", Map.class, r.get("name")), b.function("plainto_tsquery", Map.class, b.literal(form.getQuery())))));
        }
        return contractorRepository.findAll(specification, pageable);
    }

    @Override
    public Contractor save(Contractor contractor) {
        return contractorRepository.save(contractor);
    }
}
