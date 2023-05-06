package ua.illia.estore.configuration.security;

import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

public class CustomMethodSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {

    private Object filterObject;

    private Object returnObject;

    private Object target;

    public CustomMethodSecurityExpressionRoot(Authentication authentication) {
        super(authentication);
    }

    @Override
    public void setFilterObject(Object filterObject) {
        this.filterObject = filterObject;
    }

    @Override
    public Object getFilterObject() {
        return this.filterObject;
    }

    @Override
    public void setReturnObject(Object returnObject) {
        this.returnObject = returnObject;
    }

    @Override
    public Object getReturnObject() {
        return this.returnObject;
    }

    /**
     * Sets the "this" property for use in expressions. Typically this will be the "this"
     * property of the {@code JoinPoint} representing the method invocation which is being
     * protected.
     *
     * @param target the target object on which the method in is being invoked.
     */
    void setThis(Object target) {
        this.target = target;
    }

    @Override
    public Object getThis() {
        return this.target;
    }

    /**
     * TODO: should avoid it in future ro change it in other way. Method should not just check the type of user,
     * exact type of user should be passed all time or should not be passed at all. Should be implemented via application type.
     * All checks should be based on roles and authorities
     */
    public boolean isEmployee() {
        if ("anonymousUser".equals(getPrincipal())) {
            return false;
        }
        TypedUserDetails userDetails = (TypedUserDetails) getPrincipal();
        return userDetails != null && userDetails.getUserType().equals("EMPLOYEE");
    }
}
