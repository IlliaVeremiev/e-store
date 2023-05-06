package ua.illia.estore.converters.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import ua.illia.estore.converters.EntityConverter;
import ua.illia.estore.dto.employee.EmployeeResponse;
import ua.illia.estore.model.security.Employee;
import ua.illia.estore.services.management.GroupService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class EmployeeConverter implements EntityConverter<Employee, EmployeeResponse> {

    @Autowired
    private GroupService groupService;

    @Autowired
    private PaymentAccountConverter paymentAccountConverter;

    @Override
    public <E extends EmployeeResponse> E convert(Employee employee, E dto) {
        dto.setId(employee.getId());
        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setPatronymic(employee.getPatronymic());
        dto.setDisplayName(employee.getFirstName() + " " + employee.getLastName());
        dto.setFullName(employee.getLastName() + " " + employee.getFirstName() + " " + employee.getPatronymic());
        dto.setDateOfBirth(employee.getDateOfBirth());
        dto.setRegistrationDate(employee.getRegistrationDate());
        dto.setEmail(employee.getEmail());
        dto.setPhoneNumber(employee.getPhoneNumber());
        List<GrantedAuthority> allAuthorities = new ArrayList<>();
        allAuthorities.add(() -> "ROLE_EMPLOYEE");
        allAuthorities.addAll(groupService.getEmployeeAuthorities(employee));
        dto.setAuthorities(allAuthorities);
        dto.setPaymentAccounts(employee.getPaymentAccounts().stream().map(paymentAccountConverter::paymentAccountResponse).collect(Collectors.toList()));
        return dto;
    }

    @Override
    public EmployeeResponse convert(Employee employee) {
        return convert(employee, new EmployeeResponse());
    }
}
