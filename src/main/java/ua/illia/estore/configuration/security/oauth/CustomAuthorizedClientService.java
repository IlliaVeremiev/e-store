package ua.illia.estore.configuration.security.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthorizedClientService implements OAuth2AuthorizedClientService {

    @Autowired
    private OAuthService oAuthService;

    @Override
    public <T extends OAuth2AuthorizedClient> T loadAuthorizedClient(String clientRegistrationId, String principalName) {
        return null;
    }

    @Override
    public void saveAuthorizedClient(OAuth2AuthorizedClient authorizedClient, Authentication principal) {
        this.oAuthService.oauthSuccessCallback(authorizedClient, principal);
    }

    @Override
    public void removeAuthorizedClient(String clientRegistrationId, String principalName) {
    }
}