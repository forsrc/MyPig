package com.forsrc.sso.controller;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class SsoController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SsoController.class);

    @RequestMapping("/me")
    @ResponseBody
    @PreAuthorize("isAuthenticated()")
    public Principal me(Principal user) {
        LOGGER.info("--> user: {}", user);
        return user;
    }

    @RequestMapping("/api/test")
    @ResponseBody
    @PreAuthorize("isAuthenticated()")
    @Secured({ "ROLE_USER"}) 
    public long test() {
        LOGGER.info("--> test: {}", System.currentTimeMillis());
        return System.currentTimeMillis();
    }
}
