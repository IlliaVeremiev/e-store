package ua.illia.estore.converters.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ua.illia.estore.configuration.Constants;
import ua.illia.estore.converters.EntityConverter;
import ua.illia.estore.dto.customer.CustomerResponse;
import ua.illia.estore.model.security.Customer;

@Component
public class CustomerConverter implements EntityConverter<Customer, CustomerResponse> {

    @Value("${spring.profiles.active:" + Constants.Env.DEMO + "}")
    private String activeProfile;

    @Override
    public <E extends CustomerResponse> E convert(Customer customer, E dto) {
        dto.setId(customer.getId());
        dto.setRegistrationDate(customer.getRegistrationDate());
        dto.setLocked(customer.isLocked());
        dto.setLockReason(customer.getLockReason());
        dto.setEnabled(customer.isEnabled());
        if (Constants.Env.DEMO.equals(activeProfile)) {
            dto.setEmail("customer@example.com");
            dto.setPhoneNumber("+12 345 678 90");
            dto.setFirstName("John");
            dto.setLastName("Doe");
            dto.setPatronymic("Johnson");
        } else {
            dto.setEmail(customer.getEmail());
            dto.setPhoneNumber(customer.getPhoneNumber());
            dto.setFirstName(customer.getFirstName());
            dto.setLastName(customer.getLastName());
            dto.setPatronymic(customer.getPatronymic());
        }
        return dto;
    }

    @Override
    public CustomerResponse convert(Customer customer) {
        return convert(customer, new CustomerResponse());
    }
}
