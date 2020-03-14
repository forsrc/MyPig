package com.forsrc.common.core.sso.feignclient;

import org.springframework.cloud.openfeign.FeignClient;

import com.forsrc.common.core.tcc.feignclient.TccBaseFeignClient;
import com.forsrc.sso.domain.entity.UserTcc;

//@FeignClient(name = "MYPIG-SSO-SERVER", path = "/sso/api/v1/tcc/user", url = "https://mypig-sso-server:10000")
@FeignClient(name = "${mypig.feignClient.sso.name}", path = "${mypig.feignClient.sso.path}", url = "${mypig.feignClient.sso.url}")
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
