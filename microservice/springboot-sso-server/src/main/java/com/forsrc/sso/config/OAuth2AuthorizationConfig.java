package com.forsrc.sso.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.security.oauth2.authserver.AuthorizationServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.transaction.annotation.Transactional;

import com.forsrc.sso.filter.MyOncePerRequestFilter;

import javax.sql.DataSource;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableAuthorizationServer
@AutoConfigureAfter(LoginConfig.class)
@EnableConfigurationProperties({AuthorizationServerProperties.class})
public class OAuth2AuthorizationConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthorizationServerProperties authorizationServerProperties;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Bean
    public JdbcTokenStore tokenStore() {
        return new MyJdbcTokenStore(dataSource);
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

    @Bean
    @Primary
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices defaultTokenServices = new MyTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        defaultTokenServices.setSupportRefreshToken(true);
        return defaultTokenServices;
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        // @formatter:off
        security
                .passwordEncoder(passwordEncoder)
                //.tokenKeyAccess(authorizationServerProperties.getTokenKeyAccess())
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()")
                .allowFormAuthenticationForClients();
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
        // @formatter:offs
 
        clients.jdbc(dataSource)
                .passwordEncoder(passwordEncoder)
                .withClient("forsrc")
                .authorizedGrantTypes("authorization_code", "client_credentials", 
                        "refresh_token","password", "implicit")
                //.authorities("ROLE_USER", "ROLE_ADMIN")
                .resourceIds("forsrc,sso,ui,tcc,user")
                .secret("forsrc")
                .scopes("forsrc", "read", "write")
                //
                .accessTokenValiditySeconds((int) TimeUnit.HOURS.toSeconds(1))
                //.accessTokenValiditySeconds(60)
                .autoApprove(true)
                ;

        // @formatter:on

    }

    //@Configuration
    @Order(Ordered.LOWEST_PRECEDENCE - 20)
    protected static class AuthenticationManagerConfiguration extends GlobalAuthenticationConfigurerAdapter {

        @Autowired
        private BCryptPasswordEncoder passwordEncoder;
        @Autowired
        private DataSource dataSource;

        @Override
        public void init(AuthenticationManagerBuilder auth) throws Exception {
            // @formatter:off
            auth.jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(passwordEncoder)
                .withUser("forsrc@gmail.com")
                .password(passwordEncoder.encode("forsrc"))
                .roles("ADMIN", "USER");
            auth.jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(passwordEncoder)
                .withUser("user")
                .password(passwordEncoder.encode("password"))
                .roles("USER");
            auth.jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(passwordEncoder)
                .withUser("tcc")
                .password(passwordEncoder.encode("tcc"))
                .roles("TCC");
            auth.jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(passwordEncoder)
                .withUser("test")
                .password(passwordEncoder.encode("test"))
                .roles("TEST");
            // @formatter:on
        }
    }

    static class MyTokenServices extends DefaultTokenServices {

        private TokenStore tokenStore;

        @Override
        @Transactional
        public synchronized OAuth2AccessToken createAccessToken(OAuth2Authentication authentication)
                throws AuthenticationException {
            try {
                return super.createAccessToken(authentication);
            } catch (Exception e) {
                OAuth2AccessToken existingAccessToken = tokenStore.getAccessToken(authentication);
                if (existingAccessToken != null) {
                    tokenStore.removeAccessToken(existingAccessToken);
                }
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e1) {
                }
                return super.createAccessToken(authentication);
            }

        }

        @Override
        @Transactional(noRollbackFor = {InvalidTokenException.class, InvalidGrantException.class})
        public synchronized OAuth2AccessToken refreshAccessToken(String refreshTokenValue, TokenRequest tokenRequest)
                throws AuthenticationException {
            return super.refreshAccessToken(refreshTokenValue, tokenRequest);
        }

        @Override
        public void setTokenStore(TokenStore tokenStore) {
            super.setTokenStore(tokenStore);
            this.tokenStore = tokenStore;
        }
    }

    static class MyJdbcTokenStore extends JdbcTokenStore {
        private static final Logger LOG = LoggerFactory.getLogger(MyJdbcTokenStore.class);

        public MyJdbcTokenStore(DataSource dataSource) {
            super(dataSource);
        }

        @Override
        public OAuth2AccessToken readAccessToken(String tokenValue) {
            OAuth2AccessToken accessToken = null;

            try {
                accessToken = new DefaultOAuth2AccessToken(tokenValue);
            } catch (EmptyResultDataAccessException e) {
                if (LOG.isInfoEnabled()) {
                    LOG.info("Failed to find access token for token " + tokenValue);
                }
            } catch (IllegalArgumentException e) {
                LOG.warn("Failed to deserialize access token for " + tokenValue, e);
                removeAccessToken(tokenValue);
            }

            return accessToken;
        }
    }
}
