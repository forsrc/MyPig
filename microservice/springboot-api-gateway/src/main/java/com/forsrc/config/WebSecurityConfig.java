package com.forsrc.config;

import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
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
//            .requestMatchers()
//            .antMatchers("/test/**")
//            .and()
            .authorizeRequests()
            .antMatchers("/login", "logout", "/oauth/**", "/app/sso/**", "/https/sso/**", "/sso/**", "/static/**")
            .permitAll()
            .anyRequest()
            .authenticated()
            .and()
            .csrf()
            .disable()
            ;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // Filters will not get executed for the resources
        web.ignoring()
                .antMatchers(
                        "/",
                        "/oauth/**",
                        "/static/**",
                        "/public/**")
                ;
    }

}
