package com.forsrc.sso.controller;


import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import com.forsrc.common.core.spring.DeferredResultSupplier;
import com.forsrc.common.core.tcc.exception.TccCancelException;
import com.forsrc.common.core.tcc.exception.TccConfirmException;
import com.forsrc.common.core.tcc.exception.TccException;
import com.forsrc.common.core.tcc.exception.TccTryException;
import com.forsrc.common.utils.StringUtils;
import com.forsrc.sso.domain.entity.UserTcc;
import com.forsrc.sso.service.UserTccService;

@RestController
@RequestMapping("/api/v1/tcc/user")
public class UserTccController{


    @Autowired
    private UserTccService userTccService;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserTccController.class);

    @PreAuthorize("hasRole('ADMIN') or hasRole('TCC')")
    @PostMapping("/sync/")
    public ResponseEntity<UserTcc> tccTry(@RequestBody UserTcc tcc, @RequestHeader("Authorization") String accessToken) throws TccException {
        Assert.notNull(tcc, "UserTcc is null");
        Assert.notNull(tcc.getUsername(), "UserTcc username is nul");
        Assert.notNull(tcc.getPassword(), "UserTcc password is nul");
        Assert.notNull(tcc.getAuthorities(), "UserTcc authorities is null");
        Assert.notNull(tcc.getExpire(), "UserTcc expire is null");
  
        UserTcc userTcc = null;
        try {
            userTcc = userTccService.tccTry(tcc);
        } catch (Exception e) {
            throw new TccTryException(null, e.getMessage());
        }
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("tccLinkId", tcc.getId().toString())
                .header("tccLinkStatus", String.valueOf(userTcc.getStatus()))
                .body(userTcc);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('TCC')")
    @PostMapping("/")
    public DeferredResult<ResponseEntity<UserTcc>> tccTryDeferredResult(@RequestBody UserTcc tcc, @RequestHeader("Authorization") String accessToken) throws TccException {
        final DeferredResult<ResponseEntity<UserTcc>> result = new DeferredResult<>();

        handle(result, () -> tccTry(tcc, accessToken));
        return result;
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('TCC')")
    @PutMapping(path = "/sync/confirm/{id}")
    public ResponseEntity<Void> confirm(@PathVariable("id") String id, @RequestHeader("Authorization") String accessToken) throws TccException {
        LOGGER.info("--> /tcc/user/confirm/{}", id);
        UserTcc userTcc = null;
        try {
            //check timeout
            userTcc = userTccService.confirm(id);
            LOGGER.info("--> /tcc/user/confirm/{}: {}", id, userTcc);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            UUID uuid = StringUtils.toUuid(id);
            throw new TccConfirmException(uuid, e.getMessage());
        }
        return ResponseEntity
                .noContent()
                .header("tccLinkId", id)
                .header("tccLinkStatus", String.valueOf(userTcc.getStatus()))
                .build();
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('TCC')")
    @PutMapping(path = "/confirm/{id}")
    public DeferredResult<ResponseEntity<Void>> confirmResult(@PathVariable("id") String id, @RequestHeader("Authorization") String accessToken) throws TccException {
        final DeferredResult<ResponseEntity<Void>> result = new DeferredResult<>();

        handle(result, () -> confirm(id, accessToken));
        return result;
    }


    @PreAuthorize("hasRole('ADMIN') or hasRole('TCC')")
    @DeleteMapping(path = "/sync/cancel/{id}")
    public ResponseEntity<Void> cancel(@PathVariable("id") String id, @RequestHeader("Authorization") String accessToken) throws TccException{
        LOGGER.info("--> /tcc/user/cancel/{}", id);
        UserTcc userTcc = null;
        try {
            userTcc = userTccService.cancel(id);
            LOGGER.info("--> /tcc/user/cancel/{}: {}", id, userTcc);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            UUID uuid = StringUtils.toUuid(id);
            throw new TccCancelException(uuid, e.getMessage());
        }
        return ResponseEntity
                .noContent()
                .header("tccLinkId", id)
                .header("tccLinkStatus", String.valueOf(userTcc.getStatus()))
                .build();
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('TCC')")
    @DeleteMapping(path = "/cancel/{id}")
    public DeferredResult<ResponseEntity<Void>> cancelResult(@PathVariable("id") String id, @RequestHeader("Authorization") String accessToken) throws TccException {
        final DeferredResult<ResponseEntity<Void>> result = new DeferredResult<>();

        handle(result, () -> cancel(id, accessToken));
        return result;
    }

    @Async
    private <T> void handle(DeferredResult<T> result, DeferredResultSupplier<T> supplier) throws TccException {
        result.setResult(supplier.get());
    }

}
