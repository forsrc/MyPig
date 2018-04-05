package com.forsrc.common.core.tcc.feignclient;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.forsrc.common.core.tcc.exception.TccException;


public interface TccBaseFeignClient<T> {

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<T> tccTry(@RequestBody T tcc, @RequestHeader("Authorization") String accessToken)
            throws TccException;

    @RequestMapping(value = "/confirm/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> confirm(@PathVariable("id") String id,
            @RequestHeader("Authorization") String accessToken) throws TccException;

    @RequestMapping(value = "/cancel/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> cancel(@PathVariable("id") String id,
            @RequestHeader("Authorization") String accessToken) throws TccException;

}
