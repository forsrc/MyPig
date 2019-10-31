package com.forsrc.sso.aop;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.forsrc.sso.aop.ExceptionControllerAdvice.Err;

@ControllerAdvice
public class SecurityControllerAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityControllerAdvice.class);

    //@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    //@ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    public String handle(HttpServletRequest request, HttpServletResponse response, AuthenticationCredentialsNotFoundException e) {
        LOGGER.error("--> Exception {} : {}", request.getRequestURI(), e.getMessage());
        return String.format("redirect:/login?uri=%s", request.getRequestURI());
    }

    @ResponseBody
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleAuthenticationCredentialsNotFoundException(HttpServletRequest request, HttpServletResponse response, AuthenticationCredentialsNotFoundException e) {
        LOGGER.error("--> Exception {} : {}", request.getRequestURI(), e.getMessage());
        Map<String, Object> map = new HashMap<>(2);
        map.put("uri", request.getRequestURI());
        map.put("message", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(map);
    }
}
