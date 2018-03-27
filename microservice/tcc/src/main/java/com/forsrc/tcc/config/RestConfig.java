package com.forsrc.tcc.config;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextListener;

@Configuration
public class RestConfig {

    @Value("${security.oauth2.client.access-token-uri}")
    private String accessTokenUri;

    @Bean("restTemplate")
    @Primary
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean("loadBalancedRestTemplate")
    @LoadBalanced
    public RestTemplate loadBalancedRestTemplate() {
        return new RestTemplate();
    }

    @Bean("loadBalancedOAuth2RestTemplate")
    @LoadBalanced
    public RestTemplate loadBalancedOAuth2RestTemplate(OAuth2ProtectedResourceDetails details) {
        return new OAuth2RestTemplate(details, new DefaultOAuth2ClientContext());
    }

    @Bean("oauth2RestTemplate")
    @Primary
    public OAuth2RestTemplate oauth2RestTemplate(OAuth2ProtectedResourceDetails details) {
        return new OAuth2RestTemplate(details, new DefaultOAuth2ClientContext());
    }

    @Bean("tccOAuth2RestTemplate")
    public OAuth2RestTemplate tccOAuth2RestTemplate() {

        ResourceOwnerPasswordResourceDetails resourceDetails = new ResourceOwnerPasswordResourceDetails();
        resourceDetails.setUsername("tcc");
        resourceDetails.setPassword("tcc");
        //resourceDetails.setAccessTokenUri("http://SPRINGBOOT-SSO-SERVER/sso/oauth/token");
        resourceDetails.setAccessTokenUri(accessTokenUri);
        //resourceDetails.setId("tcc");
        resourceDetails.setClientId("forsrc");
        resourceDetails.setClientSecret("forsrc");
        resourceDetails.setGrantType("password");
        resourceDetails.setScope(Arrays.asList("read", "write"));

        OAuth2RestTemplate tccOAuth2RestTemplate = new OAuth2RestTemplate(resourceDetails, new DefaultOAuth2ClientContext());
        //tccOAuth2RestTemplate.setMessageConverters(Arrays.asList(new MappingJackson2HttpMessageConverter()));
        tccOAuth2RestTemplate.setErrorHandler(new OAuth2ResponseErrorHandler(tccOAuth2RestTemplate));
        return tccOAuth2RestTemplate;
    }

    @Bean
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }

    public static class OAuth2ResponseErrorHandler implements ResponseErrorHandler {

        private OAuth2RestTemplate oAuth2RestTemplate;

        public OAuth2ResponseErrorHandler(OAuth2RestTemplate oAuth2RestTemplate) {
            this.oAuth2RestTemplate = oAuth2RestTemplate;
        }

        @Override
        public boolean hasError(ClientHttpResponse response) throws IOException {
            int statusCode = response.getStatusCode().value();
            return statusCode == HttpStatus.UNAUTHORIZED.value();
        }

        @Override
        public void handleError(ClientHttpResponse response) throws IOException {
            oAuth2RestTemplate.getOAuth2ClientContext().setAccessToken(null);
        }

    }
}
