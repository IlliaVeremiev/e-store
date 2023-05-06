package ua.illia.estore.configuration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ua.illia.estore.model.security.Customer;
import ua.illia.estore.services.management.CustomerService;

@Component
public class CustomerDetailsService implements UserDetailsService {

    @Autowired
    private CustomerService customerService;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            Customer customer = customerService.getByEmail(username);
            return new CustomerUserDetails(customer);
        } catch (Exception e) {
            throw new UsernameNotFoundException("Username not found");
        }
    }
}
