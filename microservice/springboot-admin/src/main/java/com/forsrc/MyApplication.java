package com.forsrc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.turbine.EnableTurbine;
import org.springframework.context.annotation.ComponentScan;

import de.codecentric.boot.admin.config.EnableAdminServer;

@SpringBootApplication
@ComponentScan(basePackages = "com.forsrc")
//@EnableAutoConfiguration
@EnableEurekaClient
@EnableDiscoveryClient
@EnableAdminServer
@EnableHystrixDashboard
@EnableTurbine
@EnableHystrix
public class MyApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }
}
