package com.forsrc.sso.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.forsrc.sso.domain.entity.Authority;
import com.forsrc.sso.service.AuthorityService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v2/authority")
public class AuthorityReactiveController {

    @Autowired
    private AuthorityService authorityService;

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorityReactiveController.class);

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{username}")
    public Flux<Authority> getAuthorityByUsername(@PathVariable("username") String username) {
        List<Authority> list = authorityService.getByUsername(username);
        LOGGER.info("--> {} : {}", username, list);
        return Flux.concat(Flux.fromIterable(list));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{username}")
    public Mono<Authority> save(@PathVariable("username") String username, @RequestBody Authority entity) {
        Assert.notNull(entity, "save: Authority is null");
        Assert.notNull(username, "update: username is null");
        String authority = entity.getAuthority();
        Assert.notNull(authority, "update: authority is null");
        List<Authority> list = new ArrayList<>();
        if (authority.indexOf(",") > 0) {
            String[] authorities = authority.split(",");
            for (String role : authorities) {
                list.add(new Authority(username, role.trim()));
            }
        }
        authorityService.save(list);
        return Mono.just(entity);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/")
    public Flux<Authority> save(@RequestBody Authority[] Authorities) {
        Assert.notNull(Authorities, "save: Authority is null");
        Assert.isTrue(Authorities.length > 0, "save: Authority is empty");
        List<Authority> list = Arrays.asList(Authorities);
        list = authorityService.save(list);
        return Flux.concat(Flux.fromIterable(list));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{username}")
    public Mono<Authority> update(@PathVariable("username") String username, @RequestBody Authority entity) {
        Assert.notNull(username, "update: username is null");
        entity.setUsername(username);
        Assert.notNull(entity, "update: Authority is null");
        String authority = entity.getAuthority();
        Assert.notNull(authority, "update: authority is null");
        List<Authority> list = new ArrayList<>();
        if (authority.indexOf(",") > 0) {
            String[] authorities = authority.split(",");
            for (String role : authorities) {
                list.add(new Authority(username, role.trim()));
            }
        }
        authorityService.update(list);
        return Mono.just(entity);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/")
    public Flux<Authority> update(@RequestBody Authority[] Authorities) {
        Assert.notNull(Authorities, "update: Authority is null");
        Assert.isTrue(Authorities.length > 0, "update: Authority is empty");
        List<Authority> list = Arrays.asList(Authorities);
        list = authorityService.update(list);
        return Flux.concat(Flux.fromIterable(list));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{username}")
    public Mono<String> delete(@PathVariable("username") String username) {
        Assert.notNull(username, "delete: username is null");
        authorityService.delete(username);
        return Mono.just(String.format("delete: %s", username));
    }

}
