package ua.illia.estore.configuration.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public interface TypedUserDetails<T> extends UserDetails {

    String EMPLOYEE_TYPE = "EMPLOYEE";
    String CUSTOMER_TYPE = "CUSTOMER";

    T getUser();

    String getUserType();

    default boolean hasAllAuthorities(List<String> requiredAuthorities) {
        Set<String> userAuthorities = getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
        return userAuthorities.containsAll(requiredAuthorities);
    }
}
