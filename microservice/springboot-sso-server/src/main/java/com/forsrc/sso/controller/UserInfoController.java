package com.forsrc.sso.controller;

import java.security.Principal;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

@RestController
@SessionAttributes("authorizationRequest")
public class UserInfoController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserInfoController.class);

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    @PostAuthorize ("#user != null")
    public Principal me(Principal user) {
        LOGGER.info("--> user: {}", user);
        return user;
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
