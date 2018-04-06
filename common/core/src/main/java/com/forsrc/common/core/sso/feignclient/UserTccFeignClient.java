package com.forsrc.common.core.sso.feignclient;

import org.springframework.cloud.netflix.feign.FeignClient;

import com.forsrc.common.core.tcc.feignclient.TccBaseFeignClient;
import com.forsrc.sso.domain.entity.UserTcc;

@FeignClient(name = "SPRINGBOOT-SSO-SERVER", path = "/sso/api/v1/tcc/user")
public interface UserTccFeignClient extends TccBaseFeignClient<UserTcc> {

/*
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<UserTcc> tccTry(@RequestBody UserTcc tcc, @RequestHeader("Authorization") String accessToken)
            throws TccException;

    @RequestMapping(value = "/confirm/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> confirm(@PathVariable("id") String id,
            @RequestHeader("Authorization") String accessToken) throws TccException;

    @RequestMapping(value = "/cancel/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> cancel(@PathVariable("id") String id,
            @RequestHeader("Authorization") String accessToken) throws TccException;
*/
}
