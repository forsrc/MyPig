package com.forsrc.tcc;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forsrc.MyApplicationTests;
import com.forsrc.common.core.sso.dto.UserTccDto;
import com.forsrc.common.core.tcc.dto.TccDto;
import com.forsrc.common.core.tcc.dto.TccLinkDto;
import com.forsrc.tcc.domain.entity.Tcc;

public class TccTest extends MyApplicationTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(TccTest.class);

    @Autowired
    @Qualifier("tccLoadBalancedOAuth2RestTemplate")
    private OAuth2RestTemplate tccLoadBalancedOAuth2RestTemplate;

    @After
    public void init() {


    }

    @Test
    public void test() throws Exception {

        String userTccUrl = "http://SPRINGBOOT-SSO-SERVER/sso/api/v1/tcc/user/";
        String tccUrl = "http://MICROSERVICE-TCC/tcc/api/v1/tcc/";

        ObjectMapper objectMapper = new ObjectMapper();
        for (int i = 0; i < 1000; i++) {
            UserTccDto userTccDto = new UserTccDto();
            UUID id = UUID.randomUUID();
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MINUTE, 30);
            Date expire = calendar.getTime();
            userTccDto.setAuthorities("ROLE_" + id.toString());
            userTccDto.setUsername(id.toString());
            userTccDto.setPassword(id.toString());
            userTccDto.setEnabled(0);
            userTccDto.setExpire(expire);

            ResponseEntity<String> response = send(userTccUrl, userTccDto, HttpMethod.POST, 2);
            UserTccDto dto = objectMapper.readValue(response.getBody(), UserTccDto.class);
            System.out.println("UserTcc --> " + dto);
            TccDto tccDto = new TccDto();
            tccDto.setExpire(expire);
            List<TccLinkDto> links = new ArrayList<>();
            TccLinkDto tccLinkDto = new TccLinkDto();
            tccLinkDto.setExpire(expire);
            tccLinkDto.setUri(userTccUrl);
            tccLinkDto.setPath(dto.getId().toString());
            links.add(tccLinkDto);
            tccDto.setLinks(links);
            response = send(tccUrl, tccDto, HttpMethod.POST, 2);
            TccDto tcc = objectMapper.readValue(response.getBody(), TccDto.class);
            System.out.println("Tcc --> " + tcc);
        }

    }

    @Async
    private ResponseEntity<String> send(String url, Object body, HttpMethod httpMethod, int retry) {

        HttpHeaders requestHeaders = new HttpHeaders();
        List<MediaType> mediaTypeList = new ArrayList<MediaType>();
        mediaTypeList.add(MediaType.APPLICATION_JSON);
        requestHeaders.setAccept(mediaTypeList);
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        String accessToken = tccLoadBalancedOAuth2RestTemplate.getAccessToken().getValue();
        LOGGER.info("--> accessToken: {}", accessToken);
        requestHeaders.set("Authorization", String.format("Bearer %s", accessToken));
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(body, requestHeaders);
        LOGGER.info("--> ResponseEntity: {} -> {}", url, body);
        // ResponseEntity<Object> response = oauth2RestTemplate.exchange(url,
        // httpMethod, requestEntity, Object.class, id);

        try {
            ResponseEntity<String> response = tccLoadBalancedOAuth2RestTemplate.exchange(url, httpMethod, requestEntity,
                    String.class);
            LOGGER.info("--> ResponseEntity: {}", response);
            return response;
        } catch (Exception e) {
            LOGGER.warn("--> {}: {}", e.getClass(), e.getMessage());
            
            if (retry >= 0) {
                return resend(url, body, httpMethod, retry);
            }
            if (e instanceof HttpStatusCodeException) {
                HttpStatusCodeException hsce = (HttpStatusCodeException)e;
                LOGGER.warn("--> {}: {} -> {}", e.getClass(), hsce.getStatusCode(), hsce.getResponseBodyAsString());
                return ResponseEntity
                        .status(hsce.getStatusCode())
                        .header("tccUrl", url)
                        .header("responseBody", hsce.getResponseBodyAsString())
                        .header("errorMessage", hsce.getMessage())
                        .headers(hsce.getResponseHeaders())
                        .build();
            }
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    //.header("responseBody", e.getResponseBodyAsString())
                    .header("errorMessage", e.getMessage())
                    //.headers(e.getResponseHeaders())
                    .body(e.getMessage());
        }
    }
 
    private synchronized ResponseEntity<String> resend(String url, Object body, HttpMethod httpMethod, int retry) {
        LOGGER.warn("--> Retry {}: {} -> {} -> {}", retry, url, body, httpMethod);
        tccLoadBalancedOAuth2RestTemplate.getOAuth2ClientContext().setAccessToken(null);
        try {
            TimeUnit.MILLISECONDS.sleep(1000);
        } catch (InterruptedException ie) {
        }
        return send(url, body, httpMethod, --retry);
    }
}
