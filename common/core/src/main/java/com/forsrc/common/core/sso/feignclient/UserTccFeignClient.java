package com.forsrc.common.core.sso.feignclient;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.forsrc.common.core.tcc.exception.TccException;
import com.forsrc.sso.domain.entity.UserTcc;

@FeignClient(name = "SPRINGBOOT-SSO-SERVER")
public interface UserTccFeignClient {

    @PostMapping("/")
    public ResponseEntity<UserTcc> tccTry(@RequestBody UserTcc tcc, @RequestParam("access_token") String accessToken)
            throws TccException;

    @PutMapping(path = "/confirm/{id}")
    public ResponseEntity<Void> confirm(@PathVariable("id") String id, @RequestParam("access_token") String accessToken)
            throws TccException;

    @DeleteMapping(path = "/cancel/{id}")
    public ResponseEntity<Void> cancel(@PathVariable("id") String id, @RequestParam("access_token") String accessToken)
            throws TccException;

}
