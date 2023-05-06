package ua.illia.estore.configuration.security;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class SecurityPermissionEvaluator implements PermissionEvaluator {

    /**
     * PreAuthorize("hasPermission(targetDomainObject, permission)")
     */
    @Override
    public boolean hasPermission(Authentication auth, Object targetDomainObject, Object permission) {
        if ((auth == null) || (targetDomainObject == null)) {
            return false;
        }
        String targetType = targetDomainObject.getClass().getSimpleName().toUpperCase();
        return hasPrivilege(auth, targetType, permission.toString().toUpperCase());
    }

    /**
     * PreAuthorize("hasPermission(targetId, targetType, permission)")
     */
    @Override
    public boolean hasPermission(Authentication auth, Serializable targetId, String targetType, Object permission) {
        if ((auth == null) || (targetType == null) || !(permission instanceof String)) {
            return false;
        }
        return hasPrivilege(auth, targetType.toUpperCase(), permission.toString().toUpperCase());
    }

    private boolean hasPrivilege(Authentication auth, String targetType, String permission) {
        for (GrantedAuthority grantedAuth : auth.getAuthorities()) {
            if (grantedAuth.getAuthority().startsWith(targetType)
                    && grantedAuth.getAuthority().contains(permission)) {
                return true;
            }
        }
        return false;
    }
}
