package com.forsrc.ui.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.Date;

@Controller
public class UiController {

    @RequestMapping(value={"/"})
    public String index() {
        return "redirect:/ui/index.html";
    }

    @RequestMapping(value = "/user", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public Principal me(Principal user) {
        return user;
    }

    @RequestMapping("/test")
    @ResponseBody
    public String test() {
        return new Date().toString();
    }
}
