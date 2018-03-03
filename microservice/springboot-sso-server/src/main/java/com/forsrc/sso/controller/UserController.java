package com.forsrc.sso.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.forsrc.sso.domain.entity.User;
import com.forsrc.sso.service.UserService;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{username}")
    public ResponseEntity<User> getByUsername(@PathVariable("username") String username) {
        User user = userService.getByUsername(username);
        LOGGER.info("--> {} : {}", username, user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/")
    public ResponseEntity<Page<User>> get(@RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "size", required = false) Integer size) {
        page = page == null || page.intValue() == 0 ? 0 : page;
        size = size == null || size.intValue() == 0 ? 10 : size;
        size = size.intValue() >= 1000 ? 1000 : size;
        Page<User> list = userService.get(page.intValue(), size.intValue());
        LOGGER.info("--> getUsers({}, {}) : {}", page, size, list);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/")
    public ResponseEntity<User> save(@RequestBody User user) {
        Assert.notNull(user, "save: User is null");
        Assert.notNull(user.getUsername(), "save: username is nul");
        user = userService.save(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/")
    public ResponseEntity<User> update(@RequestBody User user) {
        Assert.notNull(user, "save: User is null");
        Assert.notNull(user.getUsername(), "save: username is nul");
        user = userService.save(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{username}")
    public ResponseEntity<String> delete(@PathVariable("username") String username) {
        Assert.notNull(username, "delete: username is nul");
        userService.delete(username);
        return new ResponseEntity<>(username, HttpStatus.OK);
    }
}
