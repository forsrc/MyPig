package com.forsrc.sso.controller;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SsoController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SsoController.class);

    @RequestMapping("/me")
    public Principal me(Principal user) {
        LOGGER.info("--> user: {}", user);
        return user;
    }

    @RequestMapping("/me/test")
    public long test() {
        LOGGER.info("--> test: {}", System.currentTimeMillis());
        return System.currentTimeMillis();
    }
}
