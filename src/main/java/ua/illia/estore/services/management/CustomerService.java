package ua.illia.estore.services.management;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.illia.estore.dto.customer.CustomerCreateForm;
import ua.illia.estore.dto.customer.CustomerOauthCreateForm;
import ua.illia.estore.dto.customer.CustomerSearchForm;
import ua.illia.estore.model.security.Customer;

public interface CustomerService {

    Customer create(CustomerCreateForm form);

    Customer create(CustomerOauthCreateForm form);

    Customer getById(long id);

    Page<Customer> search(CustomerSearchForm form, Pageable pageable);

    Customer getByEmail(String email);

    Customer getByUid(String uid);
}
