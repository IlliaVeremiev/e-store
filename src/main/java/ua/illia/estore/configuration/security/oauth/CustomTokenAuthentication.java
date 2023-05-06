package ua.illia.estore.configuration.security.oauth;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ua.illia.estore.configuration.security.TypedUserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomTokenAuthentication implements Authentication {

    @Setter
    private TypedUserDetails principal;
    @Getter
    private final String uid;
    @Getter
    private final String passwordHash;

    public CustomTokenAuthentication(String uid, String passwordHash) {
        this.uid = uid;
        this.passwordHash = passwordHash;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return principal == null ? Collections.emptyList() : principal.getAuthorities();
    }

    @Override
    public Object getCredentials() {
        return passwordHash;
    }

    @Override
    public UserDetails getDetails() {
        return principal;
    }

    @Override
    public UserDetails getPrincipal() {
        return principal;
    }

    @Override
    public boolean isAuthenticated() {
        return principal != null;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        //Not used
    }

    @Override
    public String getName() {
        return null;
    }
}
