package com.forsrc.sso.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextListener;

@Configuration
public class RestConfig {

    @Value("${security.oauth2.client.access-token-uri}")
    private String accessTokenUri;

    @Bean("restTemplate")
    @Primary
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean("loadBalancedRestTemplate")
    @LoadBalanced
    public RestTemplate loadBalancedRestTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
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
        tccOAuth2RestTemplate.setRetryBadAccessTokens(true);
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setOutputStreaming(false);
        tccOAuth2RestTemplate.setRequestFactory(requestFactory);
        return tccOAuth2RestTemplate;
    }
}
