package ua.illia.estore.services.management.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.illia.estore.configuration.camunda.CamundaFormBasedUserQuery;
import ua.illia.estore.configuration.exceptions.NotFoundException;
import ua.illia.estore.dto.employee.EmployeeCreateForm;
import ua.illia.estore.dto.employee.EmployeeSearchForm;
import ua.illia.estore.injectors.impl.EmployeeInjector;
import ua.illia.estore.model.security.Employee;
import ua.illia.estore.repositories.EmployeeRepository;
import ua.illia.estore.search.SpecificationList;
import ua.illia.estore.services.management.EmployeeService;
import ua.illia.estore.utils.ServiceUtils;
import ua.illia.estore.validation.validators.impl.EmployeeValidator;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class DefaultEmployeeService implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeInjector employeeInjector;

    @Autowired
    private EmployeeValidator employeeValidator;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Employee create(EmployeeCreateForm form) {
        Employee employee = new Employee();
        employeeInjector.inject(employee, form);
        employee.setPassword(passwordEncoder.encode(form.getDateOfBirth().toString()));
        employee.setEnabled(true);
        employee.setPayableUid(UUID.randomUUID().toString());
        employee.setUid(UUID.randomUUID().toString());
        employee.setAuthorizationToken(UUID.randomUUID().toString());
        employeeValidator.validate(employee);
        return employeeRepository.save(employee);
    }

    @Override
    public Employee getById(long id) {
        return employeeRepository.findById(id)
                .orElseThrow(ServiceUtils.notFound("Employee", "id", id));
    }

    @Override
    public Page<Employee> search(EmployeeSearchForm form, Pageable pageable) {
        SpecificationList<Employee> specification = new SpecificationList<>();
        if (StringUtils.isNotEmpty(form.getQuery())) {
            specification.add((r, q, b) -> b.isTrue(b.function("@@", Boolean.class, b.function("concat", String.class,
                    b.function("to_tsvector", Map.class, r.get("firstName")),
                    b.function("to_tsvector", Map.class, r.get("lastName")),
                    b.function("to_tsvector", Map.class, r.get("patronymic")),
                    b.function("to_tsvector", Map.class, r.get("email")),
                    b.function("to_tsvector", Map.class, r.get("phoneNumber"))
            ), b.function("plainto_tsquery", Map.class, b.literal(form.getQuery())))));
        }
        return employeeRepository.findAll(specification, pageable);
    }

    @Override
    public Employee update(long id, Map<String, Object> data) {
        Employee employee = getById(id);
        employeeInjector.inject(employee, data);
        employeeValidator.validate(employee);
        return employeeRepository.save(employee);
    }

    @Override
    public Employee getByEmail(String email) {
        return employeeRepository.findByEmail(email)
                .orElseThrow(ServiceUtils.notFound("Employee", "email", email));
    }

    @Override
    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    public List<Employee> getAllByRole(String roleName) {
        return employeeRepository.findAll();
    }

    @Override
    public Employee getByUid(String uid) {
        return employeeRepository.findByUid(uid)
                .orElseThrow(() -> new NotFoundException("Employee with uid: " + uid + " not found", "employee.uid"));
    }

    @Override
    public Page<Employee> searchAll(CamundaFormBasedUserQuery form) {
        SpecificationList<Employee> specification = new SpecificationList<>();
        if (form.getEmail() != null) {
            specification.add((r, q, b) -> b.like(r.get("email"), form.getEmail()));
        }
        if (form.getGroupId() != null) {
            specification.add((r, q, b) -> b.equal(r.join("groups").get("uid"), form.getGroupId()));
        }
        return employeeRepository.findAll(specification, PageRequest.of(0, Integer.MAX_VALUE));
    }

    @Override
    public long getEmployeesCount() {
        return employeeRepository.count();
    }
}
