package com.forsrc.tcc.ws;

import java.lang.reflect.Type;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

public class TccStompSessionHandler extends StompSessionHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(TccStompSessionHandler.class);
    private CompletableFuture<String> completableFuture ;

    public TccStompSessionHandler(CompletableFuture<String> completableFuture ) {
        this.completableFuture = completableFuture;
    }
    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        LOGGER.info("New session established : " + session.getSessionId());
        //session.subscribe("/topic/tcc", this);

        //LOGGER.info("Subscribed to /topic/messages");
 
        //session.send("/app/tcc", "test".getBytes());

        LOGGER.info("Message sent to websocket server");
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload,
            Throwable exception) {
        LOGGER.error("Got an exception", exception);
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        System.out.println("--> " + headers);
        return String.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        String text = payload.toString();
        System.out.println("--> " + text);
        LOGGER.info("Received : {}", text);
        //completableFuture.complete(text);
    }
}
