package ua.illia.estore.configuration.security;

import org.springframework.security.core.GrantedAuthority;
import ua.illia.estore.model.security.Customer;

import java.util.Collection;
import java.util.Collections;

public class CustomerUserDetails implements TypedUserDetails<Customer> {
    private final Customer customer;
    private final Collection<GrantedAuthority> authorities = Collections.singleton(() -> "ROLE_CUSTOMER");

    public CustomerUserDetails(Customer customer) {
        this.customer = customer;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return customer.getPassword();
    }

    @Override
    public String getUsername() {
        return String.valueOf(customer.getId());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !customer.isLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return customer.isEnabled();
    }

    @Override
    public Customer getUser() {
        return customer;
    }

    @Override
    public String getUserType() {
        return "CUSTOMER";
    }
}
