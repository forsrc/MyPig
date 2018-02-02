package com.forsrc.sso.controller;

import java.security.Principal;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SsoController {

    @RequestMapping("/me")
    public Principal getCurrentLoggedInUser(Principal user) {
        return user;
    }
}
