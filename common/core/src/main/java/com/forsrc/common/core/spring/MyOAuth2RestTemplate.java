package com.forsrc.common.core.spring;

import java.net.URI;
import java.util.Date;

import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.http.AccessTokenRequiredException;
import org.springframework.security.oauth2.client.resource.OAuth2AccessDeniedException;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.resource.UserRedirectRequiredException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;

public class MyOAuth2RestTemplate extends OAuth2RestTemplate implements OAuth2RestOperations {

    private OAuth2ClientContext context;

    private OAuth2ProtectedResourceDetails resource;

    public MyOAuth2RestTemplate(OAuth2ProtectedResourceDetails resource) {
        super(resource, new DefaultOAuth2ClientContext());
        this.context = this.getOAuth2ClientContext();
        this.resource = resource;
    }

    public MyOAuth2RestTemplate(OAuth2ProtectedResourceDetails resource, OAuth2ClientContext context) {
        super(resource, context);
        this.context = context;
        this.resource = resource;
    }

    public synchronized OAuth2AccessToken getAccessToken() throws UserRedirectRequiredException {

        OAuth2AccessToken accessToken = context.getAccessToken();

        if (accessToken == null || isExpired(accessToken)) {

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
        return accessToken;
    }

    @Override
    protected <T> T doExecute(URI url, HttpMethod method, RequestCallback requestCallback,
            ResponseExtractor<T> responseExtractor) throws RestClientException {
        OAuth2AccessToken accessToken = this.getAccessToken();
        RuntimeException rethrow = null;
        try {
            return super.doExecute(url, method, requestCallback, responseExtractor);
        } catch (AccessTokenRequiredException e) {
            rethrow = e;
        } catch (OAuth2AccessDeniedException e) {
            rethrow = e;
        } catch (InvalidTokenException e) {
            // Don't reveal the token value in case it is logged
            rethrow = new OAuth2AccessDeniedException("Invalid token for client=" + resource.getClientId());
        }
        if (accessToken != null) {
            context.setAccessToken(null);
            try {
                return super.doExecute(url, method, requestCallback, responseExtractor);
            } catch (InvalidTokenException e) {
                // Don't reveal the token value in case it is logged
                rethrow = new OAuth2AccessDeniedException("Invalid token for client=" + resource.getClientId());
            }
        }
        throw rethrow;
    }

    public boolean isExpired(OAuth2AccessToken accessToken) {
        if (accessToken == null) {
            return false;
        }
        Date expiration = accessToken.getExpiration();
        return expiration != null && new Date().getTime() > expiration.getTime() - 5000;
    }
}
