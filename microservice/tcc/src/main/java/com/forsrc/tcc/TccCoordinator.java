package com.forsrc.tcc;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.atomikos.tcc.rest.Coordinator;
import com.atomikos.tcc.rest.Transaction;

@RestController
public interface TccCoordinator extends Coordinator {

    //@PutMapping(path = "/confirm", produces = "application/tcc", consumes = "application/tcc")
    public void confirm(@RequestBody Transaction transaction);

    //@DeleteMapping(path = "/cancel", produces = "application/tcc", consumes = "application/tcc")
    public void cancel(@RequestBody Transaction transaction);

    @PutMapping(path = "/confirm/{id}", produces = "application/tcc", consumes = "application/tcc")
    public ResponseEntity<Void> confirm(@PathVariable("id") Long id, @RequestParam("access_token") String accessToken, @RequestBody Transaction transaction, HttpServletRequest request);

    @DeleteMapping(path = "/cancel/{id}", produces = "application/tcc", consumes = "application/tcc")
    public ResponseEntity<Void> cancel(@PathVariable("id") Long id, @RequestParam("access_token") String accessToken, @RequestBody Transaction transaction, HttpServletRequest request);

}
