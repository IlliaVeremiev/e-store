package ua.illia.estore.validation.validators.impl;

import org.springframework.stereotype.Component;
import ua.illia.estore.model.security.Employee;
import ua.illia.estore.validation.validators.EntityValidator;

@Component
public class EmployeeValidator extends EntityValidator<Employee> {

    @Override
    public void validate(Employee employee) {
        isEmail(employee.getEmail(), "employee.email");
        notEmpty(employee.getPassword(), "employee.password");
    }
}
