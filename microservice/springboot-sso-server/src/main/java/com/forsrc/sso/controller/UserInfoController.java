package com.forsrc.sso.controller;

import java.security.Principal;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.RestTemplate;

@RestController
@SessionAttributes("authorizationRequest")
public class UserInfoController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserInfoController.class);

    @Autowired
    @Qualifier("loadBalancedRestTemplate")
    private RestTemplate loadBalancedRestTemplate;

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    @PostAuthorize ("#user != null")
    public Principal me(Principal user) {
        LOGGER.info("--> user: {}", user);
        return user;
    }

    @GetMapping("/userinfo")
    @PreAuthorize("isAuthenticated()")
    public Map userinfo(@RequestParam("access_token") String accessToken) {
        LOGGER.info("--> userinfo access_token: {}", accessToken);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        HttpEntity<Object> entity = new HttpEntity<>(null, headers);
        ResponseEntity<Map> responseEntity = loadBalancedRestTemplate.exchange("http://SPRINGBOOT-SSO-SERVER/sso/me", HttpMethod.GET, entity, Map.class);
        LOGGER.info("--> userinfo: {}", responseEntity.getBody());
        return responseEntity.getBody();
    }

    @GetMapping("/api/test")
    //@Secured({ "ROLE_ADMIN" })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> test() {
        LOGGER.info("--> test: {}", System.currentTimeMillis());
        CacheControl cacheControl = CacheControl.maxAge(2, TimeUnit.SECONDS);
        return ResponseEntity.ok()
                .cacheControl(cacheControl)
                .body(System.currentTimeMillis());
    }
}
