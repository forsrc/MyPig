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
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

@Configuration
@EnableAuthorizationServer
@EnableConfigurationProperties({ AuthorizationServerProperties.class })
@Order(-10)
public class OAuth2AuthorizationConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthorizationServerProperties authorizationServerProperties;

    @Autowired
    private DataSource dataSource;

    @Bean
    public JdbcTokenStore tokenStore() {
        return new JdbcTokenStore(dataSource);
    }

    @Bean
    public ApprovalStore approvalStore() throws Exception {
        TokenApprovalStore store = new TokenApprovalStore();
        store.setTokenStore(tokenStore());
        return store;
    }


    @Bean
    public ClientDetailsService clientDetails() {
        return new JdbcClientDetailsService(dataSource);
    }

    @Bean
    protected AuthorizationCodeServices authorizationCodeServices() {
        return new JdbcAuthorizationCodeServices(dataSource);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        // @formatter:off
        security.passwordEncoder(PasswordEncoderConfig.PASSWORD_ENCODER)
                .tokenKeyAccess(authorizationServerProperties.getTokenKeyAccess())
                .checkTokenAccess("isAuthenticated()")
                ;
        // @formatter:on
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // @formatter:off
        endpoints.authorizationCodeServices(authorizationCodeServices())
                 .authenticationManager(authenticationManager)
                 .tokenStore(tokenStore())
                 ;
        // @formatter:off
    }


    @Override
    public void configure(ClientDetailsServiceConfigurer clients)
            throws Exception {
        // @formatter:off
        clients.jdbc(dataSource)
                .passwordEncoder(PasswordEncoderConfig.PASSWORD_ENCODER)
                ;
 
        clients.jdbc(dataSource)
                .passwordEncoder(PasswordEncoderConfig.PASSWORD_ENCODER)
                .withClient("forsrc")
                .authorizedGrantTypes("authorization_code", "client_credentials", 
                        "refresh_token","password", "implicit")
                //.authorities("ROLE_USER", "ROLE_ADMIN")
                .resourceIds("forsrc")
                .secret("forsrc")
                .scopes("forsrc", "read", "write")
                .accessTokenValiditySeconds((int) TimeUnit.HOURS.toSeconds(1))
                .autoApprove(true)
                ;

        // @formatter:on

    }

    //@Configuration
    @Order(Ordered.LOWEST_PRECEDENCE - 20)
    protected static class AuthenticationManagerConfiguration extends GlobalAuthenticationConfigurerAdapter {

        @Autowired
        private DataSource dataSource;

        @Override
        public void init(AuthenticationManagerBuilder auth) throws Exception {
            // @formatter:off
            auth.jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(PasswordEncoderConfig.PASSWORD_ENCODER)
                .withUser("forsrc@gmail.com")
                .password(PasswordEncoderConfig.PASSWORD_ENCODER.encode("forsrc"))
                .roles("ADMIN", "USER");
            auth.jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(PasswordEncoderConfig.PASSWORD_ENCODER)
                .withUser("user")
                .password(PasswordEncoderConfig.PASSWORD_ENCODER.encode("password"))
                .roles("USER");
            auth.jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(PasswordEncoderConfig.PASSWORD_ENCODER)
                .withUser("tcc")
                .password(PasswordEncoderConfig.PASSWORD_ENCODER.encode("tcc"))
                .roles("TCC");
            auth.jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(PasswordEncoderConfig.PASSWORD_ENCODER)
                .withUser("test")
                .password(PasswordEncoderConfig.PASSWORD_ENCODER.encode("test"))
                .roles("TEST");
            // @formatter:on
        }
    }
}
