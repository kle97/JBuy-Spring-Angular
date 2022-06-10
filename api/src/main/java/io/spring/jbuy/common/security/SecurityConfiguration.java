package io.spring.jbuy.common.security;

import io.spring.jbuy.common.configuration.SystemProperties;
import io.spring.jbuy.features.authentication.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.savedrequest.NullRequestCache;

@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true, // @Secured enabled
        jsr250Enabled = true,  // @RolesAllowed enabled
        prePostEnabled = true  // @PreAuthorized and @PostAuthorized enabled
)
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Value("${spring.sql.init.platform}")
    private String initPlatform;

    private final CustomUserDetailsService customUserDetailsService;
    private final Http401UnauthorizedEntryPoint http401UnauthorizedEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final PasswordEncoder passwordEncoder;
    private final SystemProperties systemProperties;

    // Expose authentication manager bean for manual authentication
    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    // register custom UserDetailsService and PasswordEncoder
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder);
    }

    // configure to ignore specific endpoints (not used in production)
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .mvcMatchers(systemProperties.getWebIgnoreEndpoints());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        // Set Content Security Policy to allow only particular script source(s) and resource(s)
        // style-src 'unsafe-inline' for Angular to work properly
        if (!initPlatform.equals("h2")) {
            http.headers().contentSecurityPolicy("default-src 'self'; " +
                                                         "style-src 'self' 'unsafe-inline' https://fonts.googleapis.com; " +
                                                         "font-src 'self' https://fonts.gstatic.com");
        }

        // Enable CORS and enable CSRF token requirement for every request
        http.cors().and()
                .csrf()
                // set cookie csrf as csrf token repository instead of the default using SessionId
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());

        // Require TLS for all requests
        if (systemProperties.isTls() || systemProperties.isCookieSecure()) {
            http.requiresChannel().anyRequest().requiresSecure();
        }

        // Disable saving http session when failure authentication
        http.requestCache()
                .requestCache(new NullRequestCache());

        // enable basic authentication
        http.httpBasic()
                // Set Http 401 "unauthorized" entry point
                .authenticationEntryPoint(http401UnauthorizedEntryPoint);

        // Set custom access denied handler instead of the default 403 "access denied" entry point
        http.exceptionHandling().accessDeniedHandler(customAccessDeniedHandler);

        // enable logout functionality without redirect upon successful logging out
        http.logout()
                .logoutUrl("/api/v1/auth/logout")
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.NO_CONTENT));

        // Allow only same origin in a frame (default: deny, other option are allow-from and sameOrigin)
        // Used only for h2-console
        if (initPlatform.equals("h2")) {
            http.headers().frameOptions().sameOrigin();
            http.csrf().disable();
        }

        // Disable form login and http basic
//        http.formLogin().disable();
//        http.httpBasic().disable();

        // @formatter:on
    }
}
