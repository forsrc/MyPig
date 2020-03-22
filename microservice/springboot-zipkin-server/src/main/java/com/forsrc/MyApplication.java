package com.forsrc;

import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

import zipkin.server.EnableZipkinServer;
import zipkin2.server.internal.ZipkinActuatorImporter;
import zipkin2.server.internal.ZipkinModuleImporter;
import zipkin2.server.internal.banner.ZipkinBanner;

@SpringBootConfiguration
@EnableAutoConfiguration
@EnableZipkinServer
@ComponentScan(basePackages = "com.forsrc")
@EnableEurekaClient
@EnableDiscoveryClient
public class MyApplication {

	static {
	    SLF4JBridgeHandler.removeHandlersForRootLogger();
	    SLF4JBridgeHandler.install();
	  }

	  public static void main(String[] args) {
	    new SpringApplicationBuilder(MyApplication.class)
	      .banner(new ZipkinBanner())
	      .initializers(new ZipkinModuleImporter(), new ZipkinActuatorImporter())
	      .properties(
	        EnableAutoConfiguration.ENABLED_OVERRIDE_PROPERTY + "=false"
	        //, "spring.config.name=zipkin-server"
	        )
	      .run(args);
	  }
}
