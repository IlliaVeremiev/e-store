package ua.illia.estore.configuration.camunda;

import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.impl.Page;
import org.camunda.bpm.engine.impl.UserQueryImpl;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;
import ua.illia.estore.model.security.Employee;
import ua.illia.estore.services.management.EmployeeService;

import java.util.List;

public class CamundaFormBasedUserQuery extends UserQueryImpl {

    private final EmployeeService employeeService;
    private org.springframework.data.domain.Page<Employee> result;

    public CamundaFormBasedUserQuery(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Override
    public long executeCount(CommandContext commandContext) {
        if (result == null) {
            result = employeeService.searchAll(this);
        }
        return result.getTotalElements();
    }

    @Override
    public List<User> executeList(CommandContext commandContext, Page page) {
        if (result == null) {
            result = employeeService.searchAll(this);
        }
        return result.map(CamundaUser::new).map(a -> (User) a).getContent();
    }

    @Override
    public boolean isRetryable() {
        return false;
    }
}
