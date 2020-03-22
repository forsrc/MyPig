package com.forsrc;

import com.forsrc.tcc.config.RibbonClinetConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringBootApplication
@ComponentScan(basePackages = "com.forsrc")
@EnableAutoConfiguration
@EnableEurekaClient
@EnableZuulProxy
@EnableDiscoveryClient
@EnableOAuth2Sso
//@EnableFeignClients
@EnableCircuitBreaker
@EnableScheduling
@EnableHystrix
@EnableAspectJAutoProxy(proxyTargetClass = true)
@RibbonClients(
        {
                @RibbonClient(name = "mypig-sso-server", configuration = RibbonClinetConfig.class),
                @RibbonClient(name = "mypig-tcc", configuration = RibbonClinetConfig.class)
        }
)
//@EnableRedisHttpSession
public class MyApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }
}
