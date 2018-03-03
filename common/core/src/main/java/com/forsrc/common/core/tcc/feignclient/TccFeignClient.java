package com.forsrc.common.core.tcc.feignclient;


import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.forsrc.tcc.domain.entity.Tcc;

@FeignClient(name = "microservice-tcc")
public interface TccFeignClient {

    @PostMapping(path = "/")
    public ResponseEntity<Tcc> tccTry(
            @RequestParam("access_token") String accessToken,
            @RequestBody Tcc tcc);

    @PutMapping(path = "/confirm/{id}")
    public ResponseEntity<Void> confirm(@PathVariable("id") String id, @RequestParam("access_token") String accessToken);

    @DeleteMapping(path = "/cancel/{id}")
    public ResponseEntity<Void> cancel(@PathVariable("id") String id, @RequestParam("access_token") String accessToken);

}
