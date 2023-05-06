package ua.illia.estore.configuration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ua.illia.estore.configuration.security.oauth.CustomAuthorizationRedirectFilter;
import ua.illia.estore.configuration.security.oauth.CustomAuthorizationRequestResolver;
import ua.illia.estore.configuration.security.oauth.CustomAuthorizedClientService;
import ua.illia.estore.configuration.security.oauth.CustomOAuthTokenAuthenticationFilter;
import ua.illia.estore.configuration.security.oauth.CustomStatelessAuthorizationRequestRepository;
import ua.illia.estore.configuration.security.oauth.OAuthService;
import ua.illia.estore.services.management.CustomerService;
import ua.illia.estore.services.management.EmployeeService;
import ua.illia.estore.services.management.GroupService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private PasswordEncoder publicTokenHashEncoder;

    @Autowired
    private OAuthService authService;

    @Autowired
    private GroupService groupService;

    public CustomOAuthTokenAuthenticationFilter customOAuthTokenAuthenticationFilter() {
        return new CustomOAuthTokenAuthenticationFilter(customerService, employeeService, publicTokenHashEncoder, authService, groupService);
    }

    @Autowired
    private UserDetailsService employeeDetailsService;

    @Autowired
    private OAuthService oauthService;

    @Autowired
    private CustomAuthorizedClientService customAuthorizedClientService;

    @Autowired
    private CustomAuthorizationRequestResolver customAuthorizationRequestResolver;

    @Autowired
    private CustomStatelessAuthorizationRequestRepository customStatelessAuthorizationRequestRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomAuthorizationRedirectFilter customAuthorizationRedirectFilter;

    @Value("${spring.profiles.active:prod}")
    private String activeProfile;

    @Value("${application.cors.allowedOrigins}")
    private String corsAllowedOrigins;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors();

        // TODO Temporary solution, should be implemented
        http.csrf().disable();

        http.authorizeRequests()
                .antMatchers("/actuator/**").permitAll()
                .antMatchers("/camunda/**").permitAll()
                .antMatchers("/**").permitAll();

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        if (!"dev".equals(activeProfile)) {
            http.requiresChannel()
                    .requestMatchers(r -> r.getHeader(Headers.X_FORWARDED_PROTO) != null)
                    .requiresSecure();
        }

        http.oauth2Login(config -> {
                    config.authorizationEndpoint(subconfig -> {
                        subconfig.baseUri(OAuthService.AUTHORIZATION_BASE_URL);
                        subconfig.authorizationRequestResolver(this.customAuthorizationRequestResolver);
                        subconfig.authorizationRequestRepository(this.customStatelessAuthorizationRequestRepository);
                    });
                    config.authorizedClientService(this.customAuthorizedClientService);
                    config.successHandler(this.oauthService::oauthSuccessResponse);
                    config.failureHandler(this.oauthService::oauthFailureResponse);
                })

                .addFilterBefore(customAuthorizationRedirectFilter, OAuth2AuthorizationRequestRedirectFilter.class)
                .exceptionHandling(config -> {
                    config.accessDeniedHandler(accessDeniedHandler);
                    config.authenticationEntryPoint(authenticationEntryPoint);
                });
        http.addFilterAfter(customOAuthTokenAuthenticationFilter(), OAuth2AuthorizationRequestRedirectFilter.class);
    }

    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        String[] allowedOrigins = corsAllowedOrigins.trim().split("\\s*,\\s*");
        String[] methods = {"GET", "POST", "PUT", "DELETE", "OPTIONS"};
        registry.addMapping("/**")
                .allowedOrigins(allowedOrigins)
                .allowedMethods(methods)
                .allowCredentials(true);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        DaoAuthenticationProvider employeeAuthenticationProvider = new DaoAuthenticationProvider();
        employeeAuthenticationProvider.setUserDetailsService(employeeDetailsService);
        employeeAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        auth.authenticationProvider(employeeAuthenticationProvider);
    }

    @Bean
    public DaoAuthenticationProvider customerAuthenticationProvider(UserDetailsService customerDetailsService) {
        DaoAuthenticationProvider customerAuthenticationProvider = new DaoAuthenticationProvider();
        customerAuthenticationProvider.setUserDetailsService(customerDetailsService);
        customerAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        return customerAuthenticationProvider;
    }

    @Bean
    public DaoAuthenticationProvider employeeAuthenticationProvider() {
        DaoAuthenticationProvider customerAuthenticationProvider = new DaoAuthenticationProvider();
        customerAuthenticationProvider.setUserDetailsService(employeeDetailsService);
        customerAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        return customerAuthenticationProvider;
    }

    /**
     * TODO move swagger related stuff
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v2/api-docs", "/swagger-resources/**", "/configuration/ui", "/configuration/security", "/swagger-ui.html");
    }
}