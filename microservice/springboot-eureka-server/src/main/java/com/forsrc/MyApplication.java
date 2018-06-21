package com.forsrc;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@SpringBootApplication
// @ComponentScan(basePackages = "com.forsrc")
//@EnableCloudDashboard
@EnableAutoConfiguration
@EnableEurekaServer
@EnableDiscoveryClient
@EnableEurekaClient
public class MyApplication {


    @RestControllerAdvice
    public static class IOExceptionControllerAdvice {
        private static final Logger LOGGER = LoggerFactory.getLogger(IOExceptionControllerAdvice.class);
        @ExceptionHandler(IOException.class)
        @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
        public Object exceptionHandler(IOException e, HttpServletRequest request) {
            LOGGER.error(e.getMessage());
            if ("Broken pipe".equalsIgnoreCase(ExceptionUtils.getRootCauseMessage(e))) {
                return null;
            } else {
                return new HttpEntity<>(e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }
}
