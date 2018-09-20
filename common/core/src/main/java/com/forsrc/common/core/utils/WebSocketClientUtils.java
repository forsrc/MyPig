package com.forsrc.common.core.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.RestTemplateXhrTransport;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

public class WebSocketClientUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketClientUtils.class);

    private StompSession session;
    private WebSocketStompClient stompClient;

    private WebSocketClientUtils() {
    }

    public static WebSocketClientUtils get(String ws, MessageConverter messageConverter,
            OAuth2RestTemplate restTemplate) throws InterruptedException, ExecutionException, TimeoutException {
        return new WebSocketClientUtils().set(ws, messageConverter, restTemplate);
    }

    private WebSocketClientUtils set(String ws, MessageConverter messageConverter, OAuth2RestTemplate restTemplate)
            throws InterruptedException, ExecutionException, TimeoutException {
        String accessToken = restTemplate.getAccessToken().getValue();

        List<Transport> transports = new ArrayList<>();
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        RestTemplateXhrTransport xhrTransport = new RestTemplateXhrTransport(restTemplate);
        xhrTransport.setTaskExecutor(new SyncTaskExecutor());
        transports.add(xhrTransport);
        SockJsClient sockJsClient = new SockJsClient(transports);
        stompClient = new WebSocketStompClient(sockJsClient);
        stompClient.setMessageConverter(messageConverter);
        session = stompClient
                .connect(String.format("%s?access_token=%s", ws, accessToken), new StompSessionHandlerAdapter() {
                }).get(1, TimeUnit.SECONDS);
        LOGGER.info("ws session id: {}", session.getSessionId());
        return this;
    }

    public StompSession getSession() {
        return session;
    }

    public WebSocketClientUtils set(String topic, StompSessionHandlerAdapter sessionHandlerAdapter) {
        if (session == null || !session.isConnected()) {
            LOGGER.error("ws session is null");
            throw new RuntimeException("ws session is null");
        }
        try {
            session.subscribe(topic, sessionHandlerAdapter);
        } catch (RuntimeException e) {
            LOGGER.error("ws runtimeException: {}", e.getMessage());
            disconnect();
            throw e;
        } finally {
        }
        return this;
    }

    public WebSocketClientUtils send(String topic, Object message) {
        if (session == null || !session.isConnected()) {
            LOGGER.error("ws session is null");
            throw new RuntimeException("ws session is null");
        }
        try {
            session.send(topic, message);
        } catch (RuntimeException e) {
            LOGGER.error("ws runtimeException: {}", e.getMessage());
            disconnect();
            throw e;
        } finally {
        }
        return this;
    }

    public WebSocketClientUtils handle(Handler handler) throws RuntimeException {
        if (session == null || !session.isConnected()) {
            LOGGER.error("ws session is null");
            throw new RuntimeException("ws session is null");
        }
        try {
            handler.handle(session);
        } catch (RuntimeException e) {
            LOGGER.error("ws runtimeException: {}", e.getMessage());
            disconnect();
            throw e;
        } finally {
        }
        return this;
    }

    public void disconnect() {
        if (session != null && session.isConnected()) {
            session.disconnect();
            session = null;
        }
    }

    public static interface Handler {
        public void handle(StompSession session) throws RuntimeException;
    }
}
