package com.forsrc.common.core.tcc.feignclient;

import org.springframework.cloud.openfeign.FeignClient;

import com.forsrc.tcc.domain.entity.Tcc;

@FeignClient(name = "MYPIG-TCC", path = "/tcc/api/v1/tcc", url = "http://mypig-tcc:10020")
public interface TccFeignClient extends TccBaseFeignClient<Tcc> {

/*
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<Tcc> tccTry(@RequestBody Tcc tcc, @RequestHeader("Authorization") String accessToken)
            throws TccException;

    @RequestMapping(value = "/confirm/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> confirm(@PathVariable("id") String id,
            @RequestHeader("Authorization") String accessToken) throws TccException;

    @RequestMapping(value = "/cancel/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> cancel(@PathVariable("id") String id,
            @RequestHeader("Authorization") String accessToken) throws TccException;
*/

}
