package ua.illia.estore.injectors.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.illia.estore.injectors.EntityDataInjector;
import ua.illia.estore.model.security.Employee;
import ua.illia.estore.services.management.GroupService;
import ua.illia.estore.services.store.StoreRoleService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class EmployeeInjector extends EntityDataInjector<Employee> {

    @Autowired
    private GroupService groupService;

    @Autowired
    private StoreRoleService storeRoleService;

    public EmployeeInjector() {
        field("firstName", Employee::setFirstName);
        field("lastName", Employee::setLastName);
        field("patronymic", Employee::setPatronymic);
        field("dateOfBirth", this::setDateOfBirth);
        field("email", Employee::setEmail);
        field("phoneNumber", Employee::setPhoneNumber);
        field("locked", Employee::setLocked);
        field("lockReason", Employee::setLockReason);
        field("roles", this::setRoles);
        field("storeRoles", this::setStoreRoles);
        field("password", Employee::setPassword);
    }

    private void setDateOfBirth(Employee employee, Object dateOfBirth) {
        employee.setDateOfBirth(LocalDate.parse(dateOfBirth.toString()));
    }

    private void setRoles(Employee employee, List<Object> rolesIds) {
        List<Long> rolesNumberIds = rolesIds.stream().map(roleId -> Long.valueOf(roleId.toString())).collect(Collectors.toList());
        employee.setGroups(groupService.getRolesByIds(rolesNumberIds));
    }

    private void setStoreRoles(Employee employee, List<Object> rolesIds) {
        List<Long> rolesNumberIds = rolesIds.stream().map(roleId -> Long.valueOf(roleId.toString())).collect(Collectors.toList());
        employee.setStoreRoles(storeRoleService.getRolesByIds(rolesNumberIds));
    }
}
