package com.forsrc.sso.aop;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.forsrc.common.core.tcc.exception.TccCancelException;
import com.forsrc.common.core.tcc.exception.TccConfirmException;
import com.forsrc.common.core.tcc.exception.TccTryException;
import com.forsrc.sso.service.UserTccService;

@ControllerAdvice
public class TccExceptionControllerAdvice {

    public static class Err {

        private String message;

        public Err(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(TccExceptionControllerAdvice.class);

    @Autowired
    private UserTccService userTccService;

    @ResponseBody
    @ExceptionHandler(TccTryException.class)
    public ResponseEntity<Void> tccTryException(HttpServletRequest request, HttpServletResponse response, TccTryException ex) {
        LOGGER.error("--> TccTryException: {} : {}", request.getRequestURI(), ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)//500
                .header("tccLinkId", ex.getId().toString())
                .header("tccLinkStatus", String.valueOf(ex.getStatus().getStatus()))
                .build();
    }

    @ResponseBody
    //@ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(TccConfirmException.class)
    public ResponseEntity<Void> tccConfirmException(HttpServletRequest request, HttpServletResponse response, TccConfirmException ex) {
        LOGGER.error("--> TccTryException: {} : {}", request.getRequestURI(), ex.getMessage());
        LOGGER.warn("Find TccConfirmException: {}", ex.getId());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)//500
                .header("tccLinkId", ex.getId().toString())
                .header("tccLinkStatus", String.valueOf(ex.getStatus().getStatus()))
                .header("tccError", ex.getMessage())
                .build();
    }

    @ResponseBody
    //@ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(TccCancelException.class)
    public ResponseEntity<Void> tccCancelException(HttpServletRequest request, HttpServletResponse response, TccCancelException ex) {
        LOGGER.error("--> TccTryException: {} : {}", request.getRequestURI(), ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)//500
                .header("tccLinkId", ex.getId().toString())
                .header("tccLinkStatus", String.valueOf(ex.getStatus().getStatus()))
                .header("tccError", ex.getMessage())
                .build();

    }
}
