package ua.illia.estore.configuration.camunda;

import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.identity.GroupQuery;
import org.camunda.bpm.engine.identity.NativeUserQuery;
import org.camunda.bpm.engine.identity.Tenant;
import org.camunda.bpm.engine.identity.TenantQuery;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.identity.UserQuery;
import org.camunda.bpm.engine.impl.identity.ReadOnlyIdentityProvider;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.illia.estore.model.security.Employee;
import ua.illia.estore.services.management.EmployeeService;
import ua.illia.estore.services.management.GroupService;

@Service("identityProvider")
public class CustomIdentityProvider implements ReadOnlyIdentityProvider {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private GroupService groupService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User findUserById(String userId) {
        return new CamundaUser(employeeService.getByEmail(userId));
    }

    @Override
    public UserQuery createUserQuery() {
        return new CamundaFormBasedUserQuery(employeeService);
    }

    @Override
    public UserQuery createUserQuery(CommandContext commandContext) {
        return new CamundaFormBasedUserQuery(employeeService);
    }

    @Override
    public NativeUserQuery createNativeUserQuery() {
        return null;
    }

    @Override
    public boolean checkPassword(String userId, String password) {
        Employee employee = employeeService.getByEmail(userId);
        return passwordEncoder.matches(password, employee.getPassword());
    }

    @Override
    public Group findGroupById(String groupId) {
        return new CamundaGroup(groupService.getByName(groupId));
    }

    @Override
    public GroupQuery createGroupQuery() {
        return new CamundaFormBasedGroupQuery(groupService);
    }

    @Override
    public GroupQuery createGroupQuery(CommandContext commandContext) {
        return new CamundaFormBasedGroupQuery(groupService);
    }

    @Override
    public Tenant findTenantById(String tenantId) {
        return null;
    }

    @Override
    public TenantQuery createTenantQuery() {
        return new CamundaFormBasedTenantQuery();
    }

    @Override
    public TenantQuery createTenantQuery(CommandContext commandContext) {
        return new CamundaFormBasedTenantQuery();
    }

    @Override
    public void flush() {
    }

    @Override
    public void close() {
    }
}
