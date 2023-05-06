package ua.illia.estore.injectors.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ua.illia.estore.injectors.EntityDataInjector;
import ua.illia.estore.model.security.Customer;

import java.util.function.BiConsumer;

@Component
public class CustomerInjector extends EntityDataInjector<Customer> {

    @Autowired
    private PasswordEncoder passwordEncoder;

    public CustomerInjector() {
        fieldInjectors.put("email", (BiConsumer<Customer, String>) Customer::setEmail);
        fieldInjectors.put("password", (BiConsumer<Customer, String>) this::setPassword);
        fieldInjectors.put("phone", (BiConsumer<Customer, String>) Customer::setPhoneNumber);
        fieldInjectors.put("firstName", (BiConsumer<Customer, String>) Customer::setFirstName);
        fieldInjectors.put("lastName", (BiConsumer<Customer, String>) Customer::setLastName);
    }

    private void setPassword(Customer customer, String password) {
        customer.setPassword(passwordEncoder.encode(password));
    }
}
