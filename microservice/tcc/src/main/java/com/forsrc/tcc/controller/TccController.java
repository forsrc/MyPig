package com.forsrc.tcc.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forsrc.common.core.tcc.exception.TccCancelException;
import com.forsrc.common.core.tcc.exception.TccConfirmException;
import com.forsrc.common.core.tcc.exception.TccException;
import com.forsrc.common.core.tcc.exception.TccTryException;
import com.forsrc.common.core.tcc.feignclient.TccFeignClient;
import com.forsrc.common.core.tcc.functional.TccSupplier;
import com.forsrc.common.core.tcc.status.Status;

import com.forsrc.tcc.domain.entity.Tcc;
import com.forsrc.tcc.service.TccService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RestController
@RequestMapping(path = "/api/v1/tcc")
public class TccController implements TccFeignClient {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TccService tccService;

    private static final Logger LOGGER = LoggerFactory.getLogger(TccController.class);

    @GetMapping(path = "/{id}")
    @HystrixCommand(fallbackMethod = "fallback")
    public ResponseEntity<Tcc> get(
            @PathVariable("id") Long id,
            @RequestHeader("Authorization") String accessToken) {
        LOGGER.info("--> get id: {}", id);
        Assert.notNull(id, "Tcc id is null");
        Tcc tcc = tccService.get(id);
        return ResponseEntity.ok().body(tcc);
    }

    @GetMapping(path = "/async/{id}")
    @HystrixCommand()
    public DeferredResult<ResponseEntity<Tcc>> getDeferredResult(
            @RequestHeader("Authorization") String accessToken,
            @PathVariable("id") Long id) throws TccException {
        final DeferredResult<ResponseEntity<Tcc>> result = new DeferredResult<>();
        handle(result, () -> get(id, accessToken));
        return result;
    }

    @Override
    @PostMapping(path = "/")
    @HystrixCommand(fallbackMethod = "tccTryFallBack")
    public ResponseEntity<Tcc> tccTry(
            @RequestBody Tcc tcc,
            @RequestHeader("Authorization") String accessToken) throws TccTryException {
        LOGGER.info("--> tccTry: {}", tcc);
        Assert.notNull(tcc, "Tcc is null");
        tcc.setStatus(Status.TRY.getStatus());
        tcc.setTimes(0);
        try {
            tccService.save(tcc);
        } catch (Exception e) {
            throw new TccTryException(tcc.getId(), e.getMessage());
        }
        return ResponseEntity.ok().body(tcc);
    }

    @PostMapping(path = "/async")
    @HystrixCommand()
    public DeferredResult<ResponseEntity<Tcc>> tccTryDeferredResult(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody Tcc tcc) throws TccException {
        final DeferredResult<ResponseEntity<Tcc>> result = new DeferredResult<>();
        handle(result, () -> tccTry(tcc, accessToken));
        return result;
    }

    public ResponseEntity<Tcc> tccTryFallBack(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody Tcc tcc) throws TccException {
        LOGGER.info("--> tccTryFallBack: {}", tcc);
        Assert.notNull(tcc, "Tcc is null");
        return ResponseEntity.ok().body(tcc);
    }

    @ResponseBody
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(TccTryException.class)
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request, HttpServletResponse response, TccTryException e)
            throws TccException {
        LOGGER.error("--> TccTryException: {} : {}", request.getRequestURI(), e.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("tccType", "try")
                .header("tccId", e.getId().toString())
                .header("tccStatus", String.valueOf(e.getStatus().getStatus()))
                .header("tccError",  e.getMessage())
                .build();
    }

    @Override
    @PutMapping(path = "/confirm/{id}")
    @HystrixCommand()
    public ResponseEntity<Void> confirm(
            @PathVariable("id") Long id,
            @RequestHeader("Authorization") String accessToken
            ) throws TccException {
        LOGGER.info("--> /confirm: {}", id);
        Tcc tcc = null;
        try {
            tcc = tccService.confirm(id, accessToken.replace("Bearer ", ""));
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .header("tccType", "confirm")
                    .header("tccId", tcc.getId().toString())
                    .header("tccStatus", String.valueOf(tcc.getStatus()))
                    .header("tcc", tcc.toString())
                    .build();
        } catch (TccException e) {
            LOGGER.error("--> /confirm: {}", e.getMessage());
            throw new TccConfirmException(id, e.getMessage(), e.getStatus());
        }
    }

    @PutMapping(path = "/async/confirm/{id}")
    @HystrixCommand()
    public DeferredResult<ResponseEntity<Void>> confirmDeferredResult(
            @PathVariable("id") Long id,
            @RequestHeader("Authorization") String accessToken
            ) throws TccException {
        final DeferredResult<ResponseEntity<Void>> result = new DeferredResult<>();
        handle(result, () -> confirm(id, accessToken));
        return result;
    }

    @ResponseBody
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(TccConfirmException.class)
    public ResponseEntity<Void> error(HttpServletRequest request, HttpServletResponse response, TccConfirmException e) {
        LOGGER.error("--> TccConfirmException: {} : {}", request.getRequestURI(), e.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("tccType", "confirm")
                .header("tccId", e.getId().toString())
                .header("tccStatus", String.valueOf(e.getStatus().getStatus()))
                .header("tccError",  e.getMessage())
                .build();
    }

    @Override
    @PutMapping(path = "/cancel")
    @HystrixCommand()
    public ResponseEntity<Void> cancel(@PathVariable("id") Long id,
            @RequestHeader("Authorization") String accessToken) throws TccException{
        LOGGER.info("--> /cancel: {}", id);
        try {
            Tcc tcc = tccService.cancel(id, accessToken.replace("Bearer ", ""));
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .header("tccType", "cancel")
                    .header("tccId", tcc.getId().toString())
                    .header("tccStatus", String.valueOf(tcc.getStatus()))
                    .header("tcc", tcc.toString())
                    .build();
        }  catch (TccException e) {
            LOGGER.error("--> /cancel: {}", e.getMessage());
            throw new TccCancelException(id, e.getMessage(), e.getStatus());
        }
    }

    @PutMapping(path = "/async/cancel")
    @HystrixCommand()
    public DeferredResult<ResponseEntity<Void>> cancelDeferredResult(@PathVariable("id") Long id,
            @RequestHeader("Authorization") String accessToken) throws TccException{
        final DeferredResult<ResponseEntity<Void>> result = new DeferredResult<>();
        handle(result, () -> confirm(id, accessToken));
        return result;
    }

    @ResponseBody
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(TccCancelException.class)
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request, HttpServletResponse response, TccCancelException e) {
        LOGGER.error("--> TccCancelException: {} : {}", request.getRequestURI(), e.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("tccType", "cancel")
                .header("tccId", e.getId().toString())
                .header("tccStatus", String.valueOf(e.getStatus().getStatus()))
                .header("tccError",  e.getMessage())
                .build();
    }

    public ResponseEntity<Tcc> fallback(@RequestHeader("Authorization") String accessToken,
            @PathVariable("id") String id) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("tcc", id)
                .header("tccMessage", "fallback")
                .build();
    }

    @Async
    protected <T> void handle(DeferredResult<T> result, TccSupplier<T> supplier) throws TccException {
        result.setResult(supplier.get());
    }
}