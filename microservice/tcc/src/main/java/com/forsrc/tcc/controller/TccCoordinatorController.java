package com.forsrc.tcc.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.atomikos.icatch.tcc.rest.CoordinatorImp;
import com.atomikos.tcc.rest.Coordinator;
import com.atomikos.tcc.rest.ParticipantLink;
import com.atomikos.tcc.rest.Transaction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
// @RequestMapping(path = "/")
public class TccCoordinatorController extends CoordinatorImp implements Coordinator {

    @Autowired
    private ObjectMapper objectMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(TccCoordinatorController.class);

    @GetMapping(path = "/test")
    public String test() {
        return new Date().toString();
    }

    @PostMapping(path = "/test")
    public Transaction test(@RequestBody Transaction transaction) {
        return transaction;
    }

    @PutMapping(path = "/confirm")
    public ResponseEntity<Void> confirm(@RequestParam("access_token") String accessToken, @RequestBody Transaction transaction, HttpServletRequest request) {
        for(ParticipantLink link : transaction.getParticipantLinks()) {
            String uri = link.getUri();
            uri = uri.indexOf("?") > 0
                    ? String.format("%s&access_token=%s", uri, accessToken) 
                            : String.format("%s?access_token=%s", uri, accessToken) ;
                    link.setUri(uri);
        }
        log("--> /confirm: {}", transaction);
        try {
            super.confirm(transaction);
            return ResponseEntity.noContent().header("tcc", "confirm").build();
        } catch (Exception e) {
            LOGGER.error("--> /confirm: {}", e.getMessage());
            return ResponseEntity
                    .notFound()
                    .header("tcc", "confirm")
                    .header("tccError", e.getMessage())
                    .build();
        }
    }

    @PutMapping(path = "/cancel")
    public ResponseEntity<Void> cancel(@RequestParam("access_token") String accessToken, @RequestBody Transaction transaction, HttpServletRequest request) {
        for(ParticipantLink link : transaction.getParticipantLinks()) {
            String uri = link.getUri();
            uri = uri.indexOf("?") > 0
                    ? String.format("%s&access_token=%s", uri, accessToken) 
                    : String.format("%s?access_token=%s", uri, accessToken) ;
            link.setUri(uri);
        }
        log("--> /cancel: {}", transaction);
        try {
            super.cancel(transaction);
            return ResponseEntity.noContent().header("tcc", "cancel").build();
        } catch (Exception e) {
            LOGGER.error("--> /cancel: {}", e.getMessage());
            return ResponseEntity
                    .notFound()
                    .header("tcc", "cancel")
                    .header("tccError", e.getMessage())
                    .build();
        }
    }

    private void log(String tag, Transaction transaction) {
        String transactionJson = null;
        try {
            transactionJson = objectMapper.writeValueAsString(transaction);
            LOGGER.info(tag, transactionJson);
        } catch (JsonProcessingException e) {
            LOGGER.info(tag, e.getMessage());
        }
    }
}
