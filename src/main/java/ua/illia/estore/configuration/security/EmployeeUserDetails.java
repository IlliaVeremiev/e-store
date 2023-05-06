package ua.illia.estore.configuration.security;

import org.springframework.security.core.GrantedAuthority;
import ua.illia.estore.model.security.Employee;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class EmployeeUserDetails implements TypedUserDetails<Employee> {

    private final Employee employee;
    private final List<GrantedAuthority> authorities;

    public EmployeeUserDetails(Employee employee, List<GrantedAuthority> authorities) {
        this.employee = employee;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> auth = new ArrayList<>(authorities);
        auth.add(() -> "ROLE_EMPLOYEE");
        return auth;
    }

    @Override
    public String getPassword() {
        return employee.getPassword();
    }

    @Override
    public String getUsername() {
        return employee.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !employee.isLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return employee.isEnabled();
    }

    @Override
    public Employee getUser() {
        return employee;
    }

    @Override
    public String getUserType() {
        return "EMPLOYEE";
    }
}
