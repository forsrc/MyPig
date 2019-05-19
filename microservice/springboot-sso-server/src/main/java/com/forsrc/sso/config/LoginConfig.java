package com.forsrc.sso.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;

@Configuration
@Order(-20)
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true, proxyTargetClass = true)
public class LoginConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

//    @Autowired
//    FindByIndexNameSessionRepository findByIndexNameSessionRepository;
//
//    @Bean
//    SpringSessionBackedSessionRegistry sessionRegistry() {
//        return new SpringSessionBackedSessionRegistry(findByIndexNameSessionRepository);
//    }

    @Bean
    public SessionRegistry sessionRegistry() {
        SessionRegistry sessionRegistry = new SessionRegistryImpl();
        return sessionRegistry;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
            .requestMatchers()
                .antMatchers("/", "/login", "/logout", "/test/**", "/who/**", "/oauth/authorize", "/oauth/confirm_access")
            .and()
                .authorizeRequests()
                .antMatchers("/oauth/token", "/actuator/**", "/static/**")
                .permitAll()
                .anyRequest()
                .authenticated()
            .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
            .and()
                .logout()
                .deleteCookies("JSESSIONID", "SESSION", "SESSIONID")
                .invalidateHttpSession(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            .and()
               .sessionManagement()
               .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
               .maximumSessions(1)
               .maxSessionsPreventsLogin(false)
                .sessionRegistry(sessionRegistry())
               .expiredUrl("/login?expired")
            .and()
            .and()
                .csrf()
                .ignoringAntMatchers("/actuator/**", "/static/**")
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
        ;

        // @formatter:on
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(PasswordEncoderConfig.PASSWORD_ENCODER)
                .usersByUsernameQuery("select username,password,enabled from users where username = ?")
                .authoritiesByUsernameQuery("select username,authority from authorities where username = ?")
        ;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        // /oauth/token
        auth
                .userDetailsService(userDetailsService())
                .passwordEncoder(PasswordEncoderConfig.PASSWORD_ENCODER);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/actuator/**", "/static/**", "/ui/**");
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    AuthenticationEntryPoint authenticationEntryPoint() {
        return new AuthenticationEntryPoint() {

            @Override
            public void commence(HttpServletRequest request, HttpServletResponse response,
                                 org.springframework.security.core.AuthenticationException authException)
                    throws IOException, ServletException {
                String requestedBy = request.getHeader("X-Requested-By");
                if (requestedBy == null || requestedBy.isEmpty()) {
                    HttpServletResponse httpResponse = (HttpServletResponse) response;
                    httpResponse.addHeader("WWW-Authenticate", "Basic realm=Cascade Realm");
                    httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
                } else {
                    HttpServletResponse httpResponse = (HttpServletResponse) response;
                    httpResponse.addHeader("WWW-Authenticate", "Application driven");
                    httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
                }
            }
        };
    }

    @Bean
    Filter csrfHeaderFilter() {
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain filterChain) throws ServletException, IOException {
                CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
                if (csrf != null) {
                    Cookie cookie = WebUtils.getCookie(request, "XSRF-TOKEN");
                    String token = csrf.getToken();
                    if (cookie == null || token != null && !token.equals(cookie.getValue())) {
                        cookie = new Cookie("XSRF-TOKEN", token);
                        cookie.setPath("/sso");
                        response.addCookie(cookie);
                    }
                }
                filterChain.doFilter(request, response);
            }
        };
    }

    @Bean
    CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setHeaderName("XSRF-TOKEN");
        return repository;
    }
}
