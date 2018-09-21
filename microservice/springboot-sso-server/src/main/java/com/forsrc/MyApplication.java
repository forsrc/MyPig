package com.forsrc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
@ComponentScan(basePackages = "com.forsrc")
@EnableAutoConfiguration
@EnableEurekaClient
@EnableDiscoveryClient
@EnableResourceServer
@EnableWebSecurity
//@EnableCaching(mode = AdviceMode.PROXY)
@EnableFeignClients
@EnableCircuitBreaker
@EnableHystrix
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableRedisHttpSession
public class MyApplication {

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
    	return new ObjectMapper();
    }

    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }
}
