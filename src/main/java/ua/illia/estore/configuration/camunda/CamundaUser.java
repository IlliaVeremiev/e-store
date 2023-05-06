package ua.illia.estore.configuration.camunda;

import org.camunda.bpm.engine.identity.User;
import ua.illia.estore.model.security.Employee;

public class CamundaUser implements User {

    private final Employee employee;

    public CamundaUser(Employee employee) {
        this.employee = employee;
    }

    @Override
    public String getId() {
        return employee.getEmail();
    }

    @Override
    public void setId(String id) {
    }

    @Override
    public String getFirstName() {
        return employee.getFirstName();
    }

    @Override
    public void setFirstName(String firstName) {
    }

    @Override
    public void setLastName(String lastName) {
    }

    @Override
    public String getLastName() {
        return employee.getLastName();
    }

    @Override
    public void setEmail(String email) {
    }

    @Override
    public String getEmail() {
        return employee.getEmail();
    }

    @Override
    public String getPassword() {
        return employee.getPassword();
    }

    @Override
    public void setPassword(String password) {
    }
}
