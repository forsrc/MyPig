package com.forsrc.sso.config;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2SsoCustomConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

@Configuration
public class LoginConfig extends WebSecurityConfigurerAdapter {
    
    private static final String CSRF_COOKIE_NAME = "XSRF-TOKEN";
    private static final String CSRF_ANGULAR_HEADER_NAME = "X-XSRF-TOKEN";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
                .formLogin()
                    .loginPage("/login")
                    .permitAll()
                 .and()
                    .logout()
                    .deleteCookies("JSESSIONID")
                    .invalidateHttpSession(true)
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/login?logout")
                    .permitAll()
                .and()
                    .requestMatchers()
                    .antMatchers("/", "/login", "/logout", "/oauth/authorize", "/oauth/confirm_access", "/test")
                .and()
                    .authorizeRequests()
                    .antMatchers("/test", "/oauth/token")
                    .permitAll()
                .and()
                    .authorizeRequests()
                    .antMatchers("/**/*.js", "/**/*.html")
                    .permitAll()
                    .anyRequest()
                    .authenticated()
                .anyRequest()
                    .authenticated()
                    .antMatchers(HttpMethod.GET, "/api/user/**", "/api/ui/**")
                        .access("#oauth2.hasScope('read')")
                    .antMatchers(HttpMethod.OPTIONS, "/api/user/**", "/api/ui/**")
                        .access("#oauth2.hasScope('read')")
                    .antMatchers(HttpMethod.POST, "/api/user/**", "/api/ui/**")
                        .access("#oauth2.hasScope('write')")
                    .antMatchers(HttpMethod.PUT, "/api/user/**", "/api/ui/**")
                        .access("#oauth2.hasScope('write')")
                    .antMatchers(HttpMethod.PATCH, "/api/user/**", "/api/ui/**")
                        .access("#oauth2.hasScope('write')")
                    .antMatchers(HttpMethod.DELETE, "/api/user/**", "/api/ui/**")
                        .access("#oauth2.hasScope('write')")
//                .and()
//                    .csrf().csrfTokenRepository(this.getCSRFTokenRepository())
//                .and()
//                    .addFilterAfter(this.createCSRFHeaderFilter(), CsrfFilter.class)
                .and()
                    .csrf()
                    .ignoringAntMatchers("/test", "/oauth/token", "/**/oauth/token", "/sso/oauth/token")
                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .and()
                    .authorizeRequests()
                    .anyRequest()
                    .authenticated();
                    ;

        // @formatter:on
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
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
    

    private Filter createCSRFHeaderFilter() {
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request,
                    HttpServletResponse response, FilterChain filterChain)
                    throws ServletException, IOException {
                CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class
                        .getName());
                if (csrf != null) {
                    Cookie cookie = WebUtils.getCookie(request, CSRF_COOKIE_NAME);
                    String token = csrf.getToken();
                    if (cookie == null || token != null
                            && !token.equals(cookie.getValue())) {
                        cookie = new Cookie(CSRF_COOKIE_NAME, token);
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
        repository.setHeaderName(CSRF_ANGULAR_HEADER_NAME);
        return repository;
    }
}
