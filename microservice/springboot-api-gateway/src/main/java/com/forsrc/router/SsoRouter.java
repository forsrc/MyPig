package com.forsrc.router;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class SsoRouter {


    @Bean("ssoRouteLocator")
    public RouteLocator ssoRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("sso_test_route", r -> r.path("/sso_test")
                        .uri("http://mypig-sso-server:10000/sso"))
                .build();
    }
}
