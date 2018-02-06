package com.forsrc.sso.config;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

//@Configuration
//@EnableResourceServer
//@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
//@Order(-5)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                .antMatcher("/me").authorizeRequests()
            .and()
                .authorizeRequests()
                //.antMatchers("/api/**").hasRole("USER")
                .antMatchers(HttpMethod.GET,     "/api/**").access("#oauth2.hasScope('read')")
                .antMatchers(HttpMethod.OPTIONS, "/api/**").access("#oauth2.hasScope('read') or #oauth2.hasScope('write')")
                .antMatchers(HttpMethod.POST,    "/api/**").access("#oauth2.hasScope('write')")
                .antMatchers(HttpMethod.PUT,     "/api/**").access("#oauth2.hasScope('write')")
                .antMatchers(HttpMethod.PATCH,   "/api/**").access("#oauth2.hasScope('write')")
                .antMatchers(HttpMethod.DELETE,  "/api/**").access("#oauth2.hasScope('write')")
            .and()
                .requestMatchers()
                .antMatchers("/", "/login", "/logout", "/oauth/authorize", "/oauth/confirm_access", "/test")
            .and()
                .authorizeRequests()
                .antMatchers("/test", "/oauth/token")
                .permitAll()
            .and()
                .exceptionHandling()
                .accessDeniedHandler(new OAuth2AccessDeniedHandler())
            .and()
                .csrf()
                .csrfTokenRepository(this.getCSRFTokenRepository())
            .and()
                .addFilterAfter(this.createCSRFHeaderFilter(), CsrfFilter.class)
                ;
        http.authorizeRequests()
            .antMatchers("/mgmt/**")
            .permitAll()
            ;
    }

    private Filter createCSRFHeaderFilter() {
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
                        cookie.setPath("/");
                        response.addCookie(cookie);
                    }
                }
                filterChain.doFilter(request, response);
            }
        };
    }

    private CsrfTokenRepository getCSRFTokenRepository() {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setHeaderName("X-XSRF-TOKEN");
        return repository;
    }
}
