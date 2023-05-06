package ua.illia.estore.db.migration;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ua.illia.estore.configuration.exceptions.ApplicationInitializationException;
import ua.illia.estore.dto.employee.EmployeeCreateForm;
import ua.illia.estore.dto.role.GroupCreateForm;
import ua.illia.estore.model.security.Employee;
import ua.illia.estore.model.security.Group;
import ua.illia.estore.model.security.enums.Authority;
import ua.illia.estore.services.management.EmployeeService;
import ua.illia.estore.services.management.GroupService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

@Component
public class V10002__Insert_Admin_employee extends BaseJavaMigration {

    @Lazy
    @Autowired
    private EmployeeService employeeService;

    @Lazy
    @Autowired
    private GroupService groupService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${estore.default-employee.email}")
    private String defaultEmployeeEmail;

    @Value("${estore.default-employee.password}")
    private String defaultEmployeePassword;

    @Override
    @Transactional
    public void migrate(Context context) throws Exception {
        if (defaultEmployeeEmail == null || defaultEmployeePassword == null) {
            throw new ApplicationInitializationException("For first app initialization, properties 'estore.default-employee.email' and 'estore.default-employee.password' required");
        }
        Group admin = createAdminGroup();
        Group camundaAdmin = createCamundaAdminGroup();

        EmployeeCreateForm form = new EmployeeCreateForm();
        form.setEmail(defaultEmployeeEmail);
        form.setFirstName("Admin");
        form.setLastName("Admin");
        form.setPhoneNumber("0000000000");
        form.setDateOfBirth(LocalDate.now());
        Employee employee = employeeService.create(form);
        employee.setPassword(passwordEncoder.encode(defaultEmployeePassword));
        employee.setGroups(new ArrayList<>(Arrays.asList(admin, camundaAdmin)));
        employeeService.save(employee);
    }

    private Group createCamundaAdminGroup() {
        GroupCreateForm form = new GroupCreateForm();
        form.setName("Camunda Admins");
        form.setType("CAMUNDA");
        form.setUid("camunda-admin");
        form.setAuthorities(Collections.emptyList());
        Group group = groupService.create(form);
        group.setSystem(true);
        return groupService.save(group);
    }

    private Group createAdminGroup() {
        GroupCreateForm form = new GroupCreateForm();
        form.setName("Admins");
        form.setType("SYSTEM");
        form.setUid("admin");
        form.setAuthorities(Arrays.asList(Authority.values()).stream().map(Authority::toString).collect(Collectors.toList()));
        return groupService.create(form);
    }
}
