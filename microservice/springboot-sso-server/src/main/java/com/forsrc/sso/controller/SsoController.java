package com.forsrc.sso.controller;

import java.security.Principal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.forsrc.sso.domain.entity.Authority;
import com.forsrc.sso.service.SsoService;

@RestController
@SessionAttributes("authorizationRequest")
public class SsoController {

    @Autowired
    private SsoService ssoService;

    private static final Logger LOGGER = LoggerFactory.getLogger(SsoController.class);

    @RequestMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public Principal me(Principal user) {
        LOGGER.info("--> user: {}", user);
        return user;
    }

    @RequestMapping("/api/test")
    //@Secured({ "ROLE_ADMIN" })
    @PreAuthorize("hasRole('ADMIN')")
    public long test() {
        LOGGER.info("--> test: {}", System.currentTimeMillis());
        return System.currentTimeMillis();
    }

    @RequestMapping("/api/authority/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Authority> authority(@PathVariable("username") String username) {
        List<Authority> list = ssoService.getAuthorityByUsername(username);
        LOGGER.info("--> {} : {}", username, list);
        return list;
    }
}
