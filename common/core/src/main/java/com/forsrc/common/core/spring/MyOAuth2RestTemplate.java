package com.forsrc.common.core.spring;

import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.resource.UserRedirectRequiredException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

public class MyOAuth2RestTemplate extends OAuth2RestTemplate implements OAuth2RestOperations {

    private OAuth2ClientContext context;

    public MyOAuth2RestTemplate(OAuth2ProtectedResourceDetails resource) {
        super(resource, new DefaultOAuth2ClientContext());
        context = this.getOAuth2ClientContext();
    }

    public MyOAuth2RestTemplate(OAuth2ProtectedResourceDetails resource, OAuth2ClientContext context) {
        super(resource, context);
        this.context = context;
    }

    public OAuth2AccessToken getAccessToken() throws UserRedirectRequiredException {

        OAuth2AccessToken accessToken = context.getAccessToken();

        if (accessToken == null || accessToken.isExpired()) {
            synchronized (MyOAuth2RestTemplate.class) {
                if (accessToken == null || accessToken.isExpired()) {
                    try {
                        accessToken = acquireAccessToken(context);
                    } catch (UserRedirectRequiredException e) {
                        context.setAccessToken(null); // No point hanging onto it now
                        accessToken = null;
                        String stateKey = e.getStateKey();
                        if (stateKey != null) {
                            Object stateToPreserve = e.getStateToPreserve();
                            if (stateToPreserve == null) {
                                stateToPreserve = "NONE";
                            }
                            context.setPreservedState(stateKey, stateToPreserve);
                        }
                        throw e;
                    }
                }
            }
        }
        return accessToken;
    }
}
