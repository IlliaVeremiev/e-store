package ua.illia.estore.validation.validators.impl;

import org.springframework.stereotype.Component;
import ua.illia.estore.model.security.Employee;
import ua.illia.estore.validation.validators.EntityValidator;

@Component
public class UserValidator extends EntityValidator<Employee> {

    @Override
    public void validate(Employee employee) {
        maxLength(employee.getEmail(), 320, "email");
        notEmpty(employee.getFirstName(), "firstName");
        maxLength(employee.getFirstName(), 64, "firstName");
        notEmpty(employee.getLastName(), "lastName");
        maxLength(employee.getLastName(), 64, "lastName");
        maxLength(employee.getPatronymic(), 64, "patronymic");
        maxLength(employee.getLockReason(), 256, "lockReason");
        maxLength(employee.getPhoneNumber(), 16, "phoneNumber");
    }
}
