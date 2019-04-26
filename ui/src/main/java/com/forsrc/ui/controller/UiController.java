package com.forsrc.ui.controller;

import java.security.Principal;
import java.util.Date;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UiController {

    @RequestMapping("/user")
    public Principal me(Principal user) {
        return user;
    }

    @RequestMapping("/test")
    public String test() {
        return new Date().toString();
    }
}
