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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.web.client.HttpStatusCodeException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forsrc.MyApplicationTests;
import com.forsrc.common.core.sso.dto.UserTccDto;
import com.forsrc.common.core.sso.feignclient.UserTccFeignClient;
import com.forsrc.common.core.tcc.dto.TccDto;
import com.forsrc.common.core.tcc.dto.TccLinkDto;
import com.forsrc.common.core.tcc.feignclient.TccFeignClient;
import com.forsrc.sso.domain.entity.UserTcc;
import com.forsrc.tcc.domain.entity.Tcc;
import com.forsrc.tcc.domain.entity.TccLink;
import com.netflix.discovery.EurekaClient;

public class TccTest extends MyApplicationTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(TccTest.class);

    @Autowired
    @Qualifier("tccLoadBalancedOAuth2RestTemplate")
    private OAuth2RestTemplate tccLoadBalancedOAuth2RestTemplate;

    @Value("${spring.application.name}")
    private String applicationName;

    @Autowired
    private EurekaClient discoveryClient;

    @Autowired
    private TccFeignClient tccFeignClient;

    @Autowired
    private UserTccFeignClient userTccFeignClient;

    private static final String userTccUrl = "http://SPRINGBOOT-SSO-SERVER/sso/api/v1/tcc/user/";

    @After
    public void init() {

    }

    @Test
    public void testFeignClient() throws Exception {

        try {
            String microservice = discoveryClient.getNextServerFromEureka(applicationName, false).getInstanceId();
            LOGGER.info("--> microservice: {}", microservice);
        } catch (Exception e) {
            TimeUnit.SECONDS.sleep(3);
            testFeignClient();
            return;
        }

        for (int i = 0; i < 100; i++) {
            UserTcc userTcc = createUserTcc();
            UserTcc userTcc1 = createUserTcc();
            UserTcc userTcc2 = createUserTcc();

            // ResponseEntity<UserTcc> userTccResponseEntity =
            // userTccFeignClient.tccTry(userTcc,
            // "Bearer " + tccLoadBalancedOAuth2RestTemplate.getAccessToken().getValue());
            ResponseEntity<UserTcc> userTccResponseEntity = sendUserTcc(userTcc, 2);
            ResponseEntity<UserTcc> userTccResponseEntity1 = sendUserTcc(userTcc1, 2);
            ResponseEntity<UserTcc> userTccResponseEntity2 = sendUserTcc(userTcc2, 2);
            UserTcc dto = userTccResponseEntity.getBody();
            UserTcc dto1 = userTccResponseEntity1.getBody();
            UserTcc dto2 = userTccResponseEntity2.getBody();
            System.out.println("UserTcc --> " + dto);
            Tcc tcc = createTcc(dto, dto1, dto2);

            // ResponseEntity<Tcc> r = tccFeignClient.tccTry(tcc,
            // "Bearer " + tccLoadBalancedOAuth2RestTemplate.getAccessToken().getValue());
            ResponseEntity<Tcc> r = sendTcc(tcc, 2);
            System.out.println("Tcc --> " + r.getBody());
        }

    }

    private UserTcc createUserTcc() {
        UserTcc userTcc = new UserTcc();
        UUID id = UUID.randomUUID();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 30);
        Date expire = calendar.getTime();
        userTcc.setAuthorities("ROLE_" + id.toString());
        userTcc.setUsername(id.toString());
        userTcc.setPassword(id.toString());
        userTcc.setEnabled(0);
        userTcc.setExpire(expire);
        return userTcc;
    }

    private Tcc createTcc(UserTcc... dto) {
        Tcc tcc = new Tcc();
        tcc.setMicroservice("");
        List<TccLink> links = new ArrayList<>();
        for (UserTcc userTcc : dto) {
            TccLink tccLink = new TccLink();
            tccLink.setExpire(userTcc.getExpire());
            tcc.setExpire(userTcc.getExpire());
            tccLink.setUri(userTccUrl);
            tccLink.setResourceId(userTcc.getId());
            links.add(tccLink);
        }
        tcc.setTccLinks(links);
        return tcc;
    }

    public ResponseEntity<UserTcc> sendUserTcc(UserTcc userTcc, int retry) {

        try {
            return userTccFeignClient.tccTry(userTcc,
                    "Bearer " + tccLoadBalancedOAuth2RestTemplate.getAccessToken().getValue());
        } catch (Exception e) {
            if (retry >= 0) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e1) {
                }
                return sendUserTcc(userTcc, --retry);
            }
            // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            // .header("errorMessage", e.getMessage())
            // .body(null);
            throw new RuntimeException(e.getMessage());
        }
    }

    public ResponseEntity<Tcc> sendTcc(Tcc tcc, int retry) {

        try {
            return tccFeignClient.tccTry(tcc,
                    "Bearer " + tccLoadBalancedOAuth2RestTemplate.getAccessToken().getValue());
        } catch (Exception e) {
            if (retry >= 0) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e1) {
                }
                return sendTcc(tcc, --retry);
            }
            // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            // .header("errorMessage", e.getMessage())
            // .body(null);
            throw new RuntimeException(e.getMessage());
        }
    }

    @Test
    public void test() throws Exception {

        String userTccUrl = "http://MYPIG-SSO-SERVER/sso/api/v1/tcc/user/";
        String tccUrl = "http://MYPIG-TCC/tcc/api/v1/tcc/";

        ObjectMapper objectMapper = new ObjectMapper();
        for (int i = 0; i < 10; i++) {
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
            tccDto.setMicroservice("");
            List<TccLinkDto> links = new ArrayList<>();
            TccLinkDto tccLinkDto = new TccLinkDto();
            tccLinkDto.setExpire(expire);
            tccLinkDto.setUri(userTccUrl);
            tccLinkDto.setResourceId(dto.getId());
            links.add(tccLinkDto);
            tccDto.setTccLinks(links);
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
                HttpStatusCodeException hsce = (HttpStatusCodeException) e;
                LOGGER.warn("--> {}: {} -> {}", e.getClass(), hsce.getStatusCode(), hsce.getResponseBodyAsString());
                return ResponseEntity.status(hsce.getStatusCode()).header("tccUrl", url)
                        .header("responseBody", hsce.getResponseBodyAsString())
                        .header("errorMessage", hsce.getMessage()).headers(hsce.getResponseHeaders()).build();
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    // .header("responseBody", e.getResponseBodyAsString())
                    .header("errorMessage", e.getMessage())
                    // .headers(e.getResponseHeaders())
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
