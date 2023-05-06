package ua.illia.estore.services.management.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.illia.estore.configuration.exceptions.NotFoundException;
import ua.illia.estore.dto.customer.CustomerCreateForm;
import ua.illia.estore.dto.customer.CustomerOauthCreateForm;
import ua.illia.estore.dto.customer.CustomerSearchForm;
import ua.illia.estore.injectors.impl.CustomerInjector;
import ua.illia.estore.model.security.Customer;
import ua.illia.estore.repositories.CustomerRepository;
import ua.illia.estore.search.SpecificationList;
import ua.illia.estore.services.management.CustomerService;
import ua.illia.estore.utils.ServiceUtils;
import ua.illia.estore.validation.validators.impl.CustomerValidator;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Service
public class DefaultCustomerService implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerInjector customerInjector;

    @Autowired
    private CustomerValidator customerValidator;

    @Override
    public Customer create(CustomerCreateForm form) {
        Customer customer = new Customer();
        customerInjector.inject(customer, form);
        customer.setEnabled(true);
        customer.setRegistrationDate(LocalDateTime.now());
        customer.setUid(UUID.randomUUID().toString());
        customer.setAuthorizationToken(UUID.randomUUID().toString());

        customerValidator.validate(customer);
        return customerRepository.save(customer);
    }

    @Override
    public Customer create(CustomerOauthCreateForm form) {
        Customer customer = new Customer();

        customer.setEnabled(true);
        customer.setRegistrationDate(LocalDateTime.now());
        customer.setFirstName(form.getFirstName());
        customer.setLastName(form.getLastName());
        customer.setUid(form.getUid());
        customer.setEmail(form.getEmail());
        customer.setAuthorizationToken(UUID.randomUUID().toString());

        return customerRepository.save(customer);
    }

    @Override
    public Customer getById(long id) {
        return customerRepository.findById(id)
                .orElseThrow(ServiceUtils.notFound("Customer", "id", id));
    }

    @Override
    public Page<Customer> search(CustomerSearchForm form, Pageable pageable) {
        SpecificationList<Customer> specification = new SpecificationList<>();
        if (StringUtils.isNotEmpty(form.getQuery())) {
            specification.add((r, q, b) -> b.isTrue(b.function("@@", Boolean.class,
                    b.function("to_tsvector", Map.class, r.get("name")),
                    b.function("plainto_tsquery", Map.class, b.literal(form.getQuery()))
            )));
        }
        return customerRepository.findAll(specification, pageable);
    }

    @Override
    public Customer getByEmail(String email) {
        return customerRepository.findByEmail(email)
                .orElseThrow(ServiceUtils.notFound("Customer", "email", email));
    }

    @Override
    public Customer getByUid(String uid) {
        return customerRepository.findByUid(uid)
                .orElseThrow(() -> new NotFoundException("Can't fine customer with provided credentials", "customer.uid"));
    }
}
