package ua.illia.estore.configuration.security;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.core.Authentication;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {

    @Autowired
    private SecurityPermissionEvaluator securityPermissionEvaluator;

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler() {
            @Override
            protected MethodSecurityExpressionOperations createSecurityExpressionRoot(Authentication authentication, MethodInvocation invocation) {
                CustomMethodSecurityExpressionRoot root = new CustomMethodSecurityExpressionRoot(authentication);
                root.setThis(invocation.getThis());
                root.setPermissionEvaluator(getPermissionEvaluator());
                root.setTrustResolver(getTrustResolver());
                root.setRoleHierarchy(getRoleHierarchy());
                root.setDefaultRolePrefix(getDefaultRolePrefix());
                return root;
            }
        };
        expressionHandler.setPermissionEvaluator(securityPermissionEvaluator);
        return expressionHandler;
    }
}
