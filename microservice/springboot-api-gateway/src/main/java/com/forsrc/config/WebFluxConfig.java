package com.forsrc.config;

import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;

@Configuration
@EnableWebFlux
//@EnableWebFluxSecurity
public class WebFluxConfig {

//    @Bean
//    RedisRateLimiter redisRateLimiter() {
//        return new RedisRateLimiter(1, 2);
//    }


}
