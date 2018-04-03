package com.forsrc.common.core.tcc.feignclient;


import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.forsrc.common.core.tcc.exception.TccException;
import com.forsrc.tcc.domain.entity.Tcc;

@FeignClient(name = "MICROSERVICE-TCC")
public interface TccFeignClient {

    @PostMapping(path = "/api/v1/tcc/user/api/v1/tcc/")
    public ResponseEntity<Tcc> tccTry(
            @RequestBody Tcc tcc,
            @RequestParam("access_token") String accessToken) throws TccException;

    @PutMapping(path = "/api/v1/tcc/user/api/v1/tcc/confirm/{id}")
    public ResponseEntity<Void> confirm(@PathVariable("id") String id, @RequestParam("access_token") String accessToken)
            throws TccException;

    @DeleteMapping(path = "/api/v1/tcc/user/api/v1/tcc/cancel/{id}")
    public ResponseEntity<Void> cancel(@PathVariable("id") String id, @RequestParam("access_token") String accessToken)
            throws TccException;

}
