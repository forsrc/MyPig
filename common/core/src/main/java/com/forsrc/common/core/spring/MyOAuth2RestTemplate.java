package com.forsrc.common.core.spring;

import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.resource.UserRedirectRequiredException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

public class MyOAuth2RestTemplate extends OAuth2RestTemplate implements OAuth2RestOperations {

    public MyOAuth2RestTemplate(OAuth2ProtectedResourceDetails resource) {
        super(resource, new DefaultOAuth2ClientContext());
    }

    public MyOAuth2RestTemplate(OAuth2ProtectedResourceDetails resource, OAuth2ClientContext context) {
        super(resource, context);
    }

    public synchronized OAuth2AccessToken getAccessToken() throws UserRedirectRequiredException {
        return super.getAccessToken();
    }
}
