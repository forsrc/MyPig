package com.forsrc;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.turbine.EnableTurbine;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import de.codecentric.boot.admin.server.config.EnableAdminServer;



@SpringBootApplication
@ComponentScan(basePackages = "com.forsrc")
//@EnableAutoConfiguration
@EnableEurekaClient
@EnableDiscoveryClient
@EnableAdminServer
@EnableHystrixDashboard
@EnableTurbine
@EnableHystrix
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
