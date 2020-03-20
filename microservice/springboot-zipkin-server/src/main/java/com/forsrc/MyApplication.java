package com.forsrc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import zipkin2.server.internal.EnableZipkinServer;




@SpringBootApplication
// @ComponentScan(basePackages = "com.forsrc")
@EnableAutoConfiguration
@EnableZipkinServer
@EnableDiscoveryClient
// @EnableZipkinStreamServer
public class MyApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }
}
