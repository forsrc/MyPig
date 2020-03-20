package com.forsrc.ui.config;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;


//@Configuration
@EnableResourceServer
@Order(200)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
    	http
        .authorizeRequests()
        .antMatchers("/api/**").authenticated()
        .antMatchers(HttpMethod.GET, "/api/**").access("#oauth2.hasScope('read')")
        .antMatchers(HttpMethod.OPTIONS, "/api/**").access("#oauth2.hasScope('read') or #oauth2.hasScope('write')")
        .antMatchers(HttpMethod.POST, "/api/**").access("#oauth2.hasScope('write')")
        .antMatchers(HttpMethod.PUT, "/api/**").access("#oauth2.hasScope('write')")
        .antMatchers(HttpMethod.PATCH, "/api/**").access("#oauth2.hasScope('write')")
        .antMatchers(HttpMethod.DELETE, "/api/**").access("#oauth2.hasScope('write')")
         ;
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("ui-api");
    }
}