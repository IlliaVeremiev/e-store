package ua.illia.estore.configuration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ua.illia.estore.model.security.Employee;
import ua.illia.estore.model.security.Group;
import ua.illia.estore.services.management.EmployeeService;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class EmployeeDetailsService implements UserDetailsService {
    @Autowired
    private EmployeeService employeeService;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            Employee employee = employeeService.getByEmail(username);
            List<GrantedAuthority> authorities = employee.getGroups()
                    .stream()
                    .map(Group::getAuthorities)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());
            return new EmployeeUserDetails(employee, authorities);
        } catch (Exception e) {
            throw new UsernameNotFoundException("Username not found");
        }
    }
}
