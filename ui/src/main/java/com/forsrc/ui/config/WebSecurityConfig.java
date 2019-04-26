package com.forsrc.ui.config;

import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.context.request.RequestContextListener;

@Configuration
@EnableOAuth2Sso
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Bean
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/**")
            .authorizeRequests()
            .antMatchers("/", "/login")
            .permitAll()
            .anyRequest()
            .authenticated()
            .and()
            .csrf()
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
            ;
    }

}
