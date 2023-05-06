package ua.illia.estore.configuration.security.oauth;

import lombok.SneakyThrows;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * https://github.com/spring-projects/spring-security/issues/6638#issuecomment-917376174
 */
@Component
public class CustomAuthorizationRedirectFilter extends OAuth2AuthorizationRequestRedirectFilter {

    @SneakyThrows
    public CustomAuthorizationRedirectFilter(OAuth2AuthorizationRequestResolver authorizationRequestResolver,
                                             AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository,
                                             OAuthService oAuthService) {
        super(authorizationRequestResolver);
        super.setAuthorizationRequestRepository(authorizationRequestRepository);
        // Reflection hack to overwrite the parent's redirect strategy
        RedirectStrategy customStrategy = oAuthService::oauthRedirectResponse;
        Field field = OAuth2AuthorizationRequestRedirectFilter.class.getDeclaredField("authorizationRedirectStrategy");
        field.setAccessible(true);
        field.set(this, customStrategy);
    }
}
