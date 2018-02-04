package com.forsrc.sso.config;

import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.authserver.AuthorizationServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.web.bind.annotation.SessionAttributes;

@Configuration
@EnableAuthorizationServer
//@SessionAttributes("authorizationRequest")
@EnableConfigurationProperties({ AuthorizationServerProperties.class })
public class OAuth2AuthorizationConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    AuthorizationServerProperties authorizationServerProperties;

    @Autowired
    private DataSource dataSource;

    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Bean
    public JdbcTokenStore tokenStore() {
        return new JdbcTokenStore(dataSource);
    }

    @Bean
    protected AuthorizationCodeServices authorizationCodeServices() {
        return new JdbcAuthorizationCodeServices(dataSource);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security)
            throws Exception {
        security.passwordEncoder(passwordEncoder)
                .tokenKeyAccess(authorizationServerProperties.getTokenKeyAccess())
                //.checkTokenAccess("isAuthenticated()")
            ;
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints)
            throws Exception {
        // @formatter:off
        endpoints.authorizationCodeServices(authorizationCodeServices())
                 .authenticationManager(authenticationManager)
                 .tokenStore(tokenStore())
                 .approvalStoreDisabled()
                 ;
        // @formatter:off
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients)
            throws Exception {
        // @formatter:off
        clients.jdbc(dataSource)
                .passwordEncoder(passwordEncoder)
                .withClient("forsrc")
                .authorizedGrantTypes("authorization_code", "client_credentials", 
                        "refresh_token","password", "implicit")
                .authorities("ROLE_USER", "ROLE_ADMIN")
                .resourceIds("forsrc")
                .scopes("read", "write")
                .secret("forsrc")
                .accessTokenValiditySeconds((int) TimeUnit.HOURS.toSeconds(1));
        // @formatter:on

    }


    @Configuration
    @Order(Ordered.LOWEST_PRECEDENCE - 20)
    protected static class AuthenticationManagerConfiguration extends
            GlobalAuthenticationConfigurerAdapter {

        @Autowired
        private DataSource dataSource;

        @Override
        public void init(AuthenticationManagerBuilder auth) throws Exception {
            // @formatter:off
            auth.jdbcAuthentication()
                .dataSource(dataSource)
                .withUser("forsrc@gmail.com")
                .password("forsrc")
                .roles("ADMIN");
            auth.jdbcAuthentication()
                .dataSource(dataSource)
                .withUser("user")
                .password("password")
                .roles("USER");
            // @formatter:on
        }
    }
}
