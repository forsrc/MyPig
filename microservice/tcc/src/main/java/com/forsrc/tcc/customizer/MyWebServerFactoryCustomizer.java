package com.forsrc.tcc.customizer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.AbstractConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.stereotype.Component;

@Component
public class MyWebServerFactoryCustomizer implements WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {

    @Value("${server.port:8080}")
    private Integer port;
    @Override
    public void customize(ConfigurableServletWebServerFactory server) {
        if (server instanceof AbstractConfigurableWebServerFactory) {
            port = ((AbstractConfigurableWebServerFactory)server).getPort();
        }
    }

    public Integer getPort() {
        return port;
    }
}