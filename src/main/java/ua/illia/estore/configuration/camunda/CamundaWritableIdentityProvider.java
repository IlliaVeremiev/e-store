package ua.illia.estore.configuration.camunda;

import org.camunda.bpm.engine.AuthorizationService;
import org.camunda.bpm.engine.authorization.Groups;
import org.camunda.bpm.engine.authorization.Resource;
import org.camunda.bpm.engine.authorization.Resources;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.identity.GroupQuery;
import org.camunda.bpm.engine.identity.NativeUserQuery;
import org.camunda.bpm.engine.identity.Tenant;
import org.camunda.bpm.engine.identity.TenantQuery;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.identity.UserQuery;
import org.camunda.bpm.engine.impl.cfg.auth.ResourceAuthorizationProvider;
import org.camunda.bpm.engine.impl.context.Context;
import org.camunda.bpm.engine.impl.identity.IdentityOperationResult;
import org.camunda.bpm.engine.impl.identity.ReadOnlyIdentityProvider;
import org.camunda.bpm.engine.impl.identity.WritableIdentityProvider;
import org.camunda.bpm.engine.impl.identity.db.DbIdentityServiceProvider;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;
import org.camunda.bpm.engine.impl.persistence.entity.AuthorizationEntity;
import org.camunda.bpm.engine.impl.persistence.entity.AuthorizationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ua.illia.estore.model.security.Employee;
import ua.illia.estore.services.management.EmployeeService;
import ua.illia.estore.services.management.GroupService;

import java.util.concurrent.Callable;

import static org.camunda.bpm.engine.authorization.Authorization.ANY;
import static org.camunda.bpm.engine.authorization.Authorization.AUTH_TYPE_GRANT;
import static org.camunda.bpm.engine.authorization.Permissions.ALL;

@Component
public class CamundaWritableIdentityProvider implements WritableIdentityProvider, ReadOnlyIdentityProvider {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private DbIdentityServiceProvider dbIdentityServiceProvider = new DbIdentityServiceProvider();

    @Override
    public User createNewUser(String userId) {
        Employee employee = new Employee();
        employee.setEmail(userId);
        return new CamundaUser(employee);
    }

    @Override
    public IdentityOperationResult saveUser(User user) {
        return new IdentityOperationResult(user, IdentityOperationResult.OPERATION_UPDATE);
    }

    @Override
    public IdentityOperationResult deleteUser(String userId) {
        return new IdentityOperationResult(userId, IdentityOperationResult.OPERATION_DELETE);
    }

    @Override
    public IdentityOperationResult unlockUser(String userId) {
        return new IdentityOperationResult(userId, IdentityOperationResult.OPERATION_UNLOCK);
    }

    @Override
    public Group createNewGroup(String groupId) {
        ua.illia.estore.model.security.Group group = new ua.illia.estore.model.security.Group();
        group.setUid(groupId);
        return new CamundaGroup(group);
    }

    @Override
    public IdentityOperationResult saveGroup(Group group) {
        dbIdentityServiceProvider.saveDefaultAuthorizations(Context.getProcessEngineConfiguration()
                .getResourceAuthorizationProvider().newGroup(group));
        return new IdentityOperationResult(group, IdentityOperationResult.OPERATION_UPDATE);
    }

    @Override
    public IdentityOperationResult deleteGroup(String groupId) {
        return new IdentityOperationResult(groupId, IdentityOperationResult.OPERATION_DELETE);
    }

    @Override
    public Tenant createNewTenant(String tenantId) {
        return null;
    }

    @Override
    public IdentityOperationResult saveTenant(Tenant tenant) {
        return null;
    }

    @Override
    public IdentityOperationResult deleteTenant(String tenantId) {
        return null;
    }

    @Override
    public IdentityOperationResult createMembership(String userId, String groupId) {
        return dbIdentityServiceProvider.createMembership(userId, groupId);
    }

    @Override
    public IdentityOperationResult deleteMembership(String userId, String groupId) {
        return dbIdentityServiceProvider.deleteMembership(userId, groupId);
    }

    @Override
    public IdentityOperationResult createTenantUserMembership(String tenantId, String userId) {
        return null;
    }

    @Override
    public IdentityOperationResult createTenantGroupMembership(String tenantId, String groupId) {
        return null;
    }

    @Override
    public IdentityOperationResult deleteTenantUserMembership(String tenantId, String userId) {
        return null;
    }

    @Override
    public IdentityOperationResult deleteTenantGroupMembership(String tenantId, String groupId) {
        return null;
    }

    @Override
    public void flush() {
        dbIdentityServiceProvider.flush();
    }

    @Override
    public void close() {
        dbIdentityServiceProvider.close();
    }

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


    /**
     * Copied from SetupResource
     */
    public void createInitialAuthorizations(String groupId) {
        saveDefaultAuthorizations(getResourceAuthorizationProvider().newGroup(new CamundaGroup(groupService.getByUid(groupId))));
        for (Resource resource : Resources.values()) {
            if (getAuthorizationService().createAuthorizationQuery().groupIdIn(Groups.CAMUNDA_ADMIN).resourceType(resource).resourceId(ANY).count() == 0) {
                AuthorizationEntity userAdminAuth = new AuthorizationEntity(AUTH_TYPE_GRANT);
                userAdminAuth.setGroupId(Groups.CAMUNDA_ADMIN);
                userAdminAuth.setResource(resource);
                userAdminAuth.setResourceId(ANY);
                userAdminAuth.addPermission(ALL);
                getAuthorizationService().saveAuthorization(userAdminAuth);
            }
        }
    }

    protected ResourceAuthorizationProvider getResourceAuthorizationProvider() {
        return Context.getProcessEngineConfiguration()
                .getResourceAuthorizationProvider();
    }

    protected AuthorizationService getAuthorizationService() {
        return Context.getProcessEngineConfiguration()
                .getAuthorizationService();
    }

    protected AuthorizationManager getAuthorizationManager() {
        return getSession(AuthorizationManager.class);
    }

    protected <T> T getSession(Class<T> sessionClass) {
        return Context.getCommandContext().getSession(sessionClass);
    }

    public void saveDefaultAuthorizations(final AuthorizationEntity[] authorizations) {
        if (authorizations != null && authorizations.length > 0) {
            Context.getCommandContext().runWithoutAuthorization(new Callable<Void>() {
                public Void call() {
                    AuthorizationManager authorizationManager = getAuthorizationManager();
                    for (AuthorizationEntity authorization : authorizations) {

                        if (authorization.getId() == null) {
                            authorizationManager.insert(authorization);
                        } else {
                            authorizationManager.update(authorization);
                        }

                    }
                    return null;
                }
            });
        }
    }
}
