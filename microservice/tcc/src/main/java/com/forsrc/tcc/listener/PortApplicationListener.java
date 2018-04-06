package com.forsrc.tcc.listener;

import org.springframework.boot.context.embedded.EmbeddedServletContainerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class PortApplicationListener implements ApplicationListener<EmbeddedServletContainerInitializedEvent>  {

    private int port;

    @Override
    public void onApplicationEvent(
            EmbeddedServletContainerInitializedEvent event) {
        port = event.getEmbeddedServletContainer().getPort();
    }

    public int getPort() {
        return port;
    }

}