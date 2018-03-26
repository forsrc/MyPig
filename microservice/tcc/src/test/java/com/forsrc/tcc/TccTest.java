package com.forsrc.tcc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.After;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import com.forsrc.MyApplicationTests;
import com.forsrc.common.core.sso.dto.UserTccDto;


public class TccTest extends MyApplicationTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(TccTest.class);

    @After
    public void init() {
        
//        String accessToken = tccOAuth2RestTemplate.getAccessToken().getValue();
    }

    @Test
    public void testTransaction() throws Exception {
        ResourceOwnerPasswordResourceDetails resourceDetails = new ResourceOwnerPasswordResourceDetails();
        resourceDetails.setUsername("tcc");
        resourceDetails.setPassword("tcc");
        //resourceDetails.setAccessTokenUri("http://SPRINGBOOT-SSO-SERVER/sso/oauth/token");
        resourceDetails.setAccessTokenUri("http://forsrc.local:10000/sso/oauth/token");
        //resourceDetails.setId("tcc");
        resourceDetails.setClientId("forsrc");
        resourceDetails.setClientSecret("forsrc");
        resourceDetails.setGrantType("password");
        resourceDetails.setScope(Arrays.asList("read", "write"));

        OAuth2RestTemplate tccOAuth2RestTemplate = new OAuth2RestTemplate(resourceDetails, new DefaultOAuth2ClientContext());
        tccOAuth2RestTemplate.setMessageConverters(Arrays.asList(new MappingJackson2HttpMessageConverter()));

        String userTccUrl = "http://forsrc.local:10000/sso/api/v1/tcc/user/";

        for (int i = 0; i < 2; i++) {
            UserTccDto userTccDto = new UserTccDto();
            UUID id = UUID.randomUUID();
            userTccDto.setId(id);
            userTccDto.setAuthorities("ROLE_" + id.toString());
            userTccDto.setUsername(id.toString());
            userTccDto.setPassword(id.toString());
            userTccDto.setEnabled(0);
            userTccDto.setExpire(new Date(System.currentTimeMillis() + 5 * 1000 * 60));

            ResponseEntity<String> response  =  send(tccOAuth2RestTemplate, userTccUrl, userTccDto, HttpMethod.POST);
            System.out.println(response.getBody());
        }
        
    }

    private ResponseEntity<String> send(OAuth2RestTemplate tccOAuth2RestTemplate, String url, Object body, HttpMethod httpMethod) {

        LOGGER.info("--> ResponseEntity: {} -> {}", url, body);
        HttpHeaders requestHeaders = new HttpHeaders();
        List<MediaType> mediaTypeList = new ArrayList<MediaType>();
        mediaTypeList.add(MediaType.APPLICATION_JSON);
        requestHeaders.setAccept(mediaTypeList);
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        String accessToken = tccOAuth2RestTemplate.getAccessToken().getValue();
        LOGGER.info("--> accessToken: {}", accessToken);
        requestHeaders.set("Authorization", String.format("Bearer %s", accessToken));
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(body, requestHeaders);

        // ResponseEntity<Object> response = oauth2RestTemplate.exchange(url,
        // httpMethod, requestEntity, Object.class, id);
        
        try {
            ResponseEntity<String> response = tccOAuth2RestTemplate.exchange(url, httpMethod, requestEntity,
                    String.class);
            LOGGER.info("--> ResponseEntity: {}", response);
            return response;
        } catch (HttpServerErrorException e) {
            LOGGER.warn("--> HttpServerErrorException: {} {} -> {}", e.getStatusCode(), e.getStatusText(),
                    e.getResponseBodyAsString());
            return ResponseEntity.status(e.getStatusCode())
                    .header("responseBody", e.getResponseBodyAsString()).header("errorMessage", e.getMessage())
                    .headers(e.getResponseHeaders())
                    .body(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            LOGGER.warn("--> HttpClientErrorException: {} {} -> {}", e.getStatusCode(), e.getStatusText(),
                    e.getResponseBodyAsString());
            return ResponseEntity.status(e.getStatusCode())
                    .header("responseBody", e.getResponseBodyAsString()).header("errorMessage", e.getMessage())
                    .headers(e.getResponseHeaders())
                    .body(e.getResponseBodyAsString());
        }
    }
}
