package com.forsrc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

import com.github.vanroy.cloud.dashboard.config.EnableCloudDashboard;

@SpringBootApplication
//@ComponentScan(basePackages = "com.forsrc")
//@EnableAutoConfiguration
@EnableEurekaServer
@EnableCloudDashboard
@EnableDiscoveryClient
@EnableEurekaClient
public class MyApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }
}
