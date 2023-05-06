package ua.illia.estore.validation.validators.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.illia.estore.configuration.exceptions.NotFoundException;
import ua.illia.estore.model.security.Customer;
import ua.illia.estore.services.management.CustomerService;
import ua.illia.estore.validation.validators.EntityValidator;

@Component
public class CustomerValidator extends EntityValidator<Customer> {

    @Autowired
    private CustomerService customerService;

    @Override
    public void validate(Customer customer) {
        isEmail(customer.getEmail(), "email");
        try {
            customerService.getByEmail(customer.getEmail());
            validationError("Account for email: " + customer.getEmail() + " already used", "customer.email");
        } catch (NotFoundException ignored) {
            // It's good flow when this exception thrown
        }
    }
}
