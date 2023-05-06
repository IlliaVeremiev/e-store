package ua.illia.estore.services.management;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.illia.estore.configuration.camunda.CamundaFormBasedUserQuery;
import ua.illia.estore.dto.employee.EmployeeCreateForm;
import ua.illia.estore.dto.employee.EmployeeSearchForm;
import ua.illia.estore.model.security.Employee;

import java.util.List;
import java.util.Map;

public interface EmployeeService {

    Employee create(EmployeeCreateForm form);

    Employee getById(long id);

    Page<Employee> search(EmployeeSearchForm form, Pageable pageable);

    Employee update(long id, Map<String, Object> data);

    Employee getByEmail(String email);

    Employee save(Employee employee);

    List<Employee> getAllByRole(String roleName);

    Employee getByUid(String uid);

    Page<Employee> searchAll(CamundaFormBasedUserQuery camundaFormBasedUserQuery);

    long getEmployeesCount();
}
