package com.forsrc.sso.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Collection;

@RestController
@RequestMapping(value = "/test")
public class TestController {

    @Autowired
    private FindByIndexNameSessionRepository<? extends Session> sessions;

    @Autowired
    private Environment environment;

    @RequestMapping(value = "/")
    public ResponseEntity<String> test(String name) {

        return new ResponseEntity<>("hello world. " + (name == null ? "" : name), HttpStatus.OK);
    }

    @RequestMapping(value = "/evn/{key}")
    public ResponseEntity<String> evn(@PathVariable("key") String key) {
        return new ResponseEntity<>("Environment: " + environment.getProperty(key), HttpStatus.OK);
    }

    @RequestMapping(value = "/hystrix/{name}")
    @HystrixCommand(fallbackMethod = "hystrixFallBack")
    public ResponseEntity<String> hystrix(@PathVariable("name") String name) {

        return new ResponseEntity<>("hello world. " + (name == null ? "" : name), HttpStatus.OK);
    }

    public ResponseEntity<String> hystrixFallBack(String name) {

        return new ResponseEntity<>("forsrc-springcloud-hystrix: hystrixFallBack -> " + (name == null ? "" : name),
                HttpStatus.OK);
    }

    @GetMapping("/r/{username}")
    public Mono<ServerResponse> getByUsername(@PathVariable("username") String username) {

        return ServerResponse //
                .ok() //
                .contentType(MediaType.APPLICATION_JSON) //
                .body(Mono.just(username), String.class) //
                .or(ServerResponse.notFound().build());
    }

    @RequestMapping(value = "/1")
    public ResponseEntity<String> test1(HttpServletRequest request, HttpSession session, Principal user) {
        session.setAttribute("test", System.currentTimeMillis());

        return new ResponseEntity<>("1: " + request.getRequestedSessionId() + "->" + (user == null ? null : user.getName()) + "->" + session.getAttribute("test"), HttpStatus.OK);
    }

    @RequestMapping(value = "/2")
    public ResponseEntity<String> test2(HttpServletRequest request, HttpSession session, Principal user) {

        return new ResponseEntity<>("2: " + request.getRequestedSessionId() + "->" + (user == null ? null : user.getName()) + "->" + session.getAttribute("test"), HttpStatus.OK);
    }

    public Session getUserSession(String username) {

        Session userSession = null;
        Collection<? extends Session> usersSessions = sessions.findByIndexNameAndIndexValue(FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME, username).values();

        if (CollectionUtils.isEmpty(usersSessions)) {
            return userSession;
        }
        if (usersSessions.size() > 1) {
            throw new IllegalStateException(
                    String.format("expected 1 session, but found %d", usersSessions.size()));
        }
        userSession = usersSessions.iterator().next();
        return userSession;
    }
}
