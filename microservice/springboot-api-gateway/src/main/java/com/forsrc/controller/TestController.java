package com.forsrc.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestController.class);

    @RequestMapping("/test")
    public long test() {
        LOGGER.info("--> test: {}", System.currentTimeMillis());
        return System.currentTimeMillis();
    }
}
