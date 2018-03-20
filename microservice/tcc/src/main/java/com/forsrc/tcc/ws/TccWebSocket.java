package com.forsrc.tcc.ws;

import java.security.Principal;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.messaging.Message;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import com.forsrc.common.utils.StringUtils;
import com.forsrc.tcc.domain.entity.Tcc;
import com.forsrc.tcc.service.TccService;

@Controller
public class TccWebSocket {
    @Autowired
    private TccService tccService;
    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    private static final Logger LOGGER = LoggerFactory.getLogger(TccWebSocket.class);

    @MessageMapping("/tcc/{tccId}")
    @SendTo("/topic/tcc")
    public String send( @DestinationVariable("tccId") String tccId, @Payload Message<String> message, SimpMessageHeaderAccessor headerAccessor, Principal user) throws Exception {
        LOGGER.info("SimpMessageHeaderAccessor: {}", headerAccessor);
        LOGGER.info("Message: {}", message);
        LOGGER.info("Principal: {}", user);
        headerAccessor.getSessionId();
        //messagingTemplate.
        String text = message.getPayload();
        Tcc tcc = tccService.get(StringUtils.toUuid(tccId));
        return tcc != null ? tcc.toString() : tccId;
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException {
        ResourceOwnerPasswordResourceDetails resourceDetails = new ResourceOwnerPasswordResourceDetails();
        resourceDetails.setUsername("tcc");
        resourceDetails.setPassword("tcc");
        // resourceDetails.setAccessTokenUri("http://SPRINGBOOT-SSO-SERVER/sso/oauth/token");
        resourceDetails.setAccessTokenUri("http://forsrc.local:10000/sso/oauth/token");
        // resourceDetails.setId("tcc");
        resourceDetails.setClientId("forsrc");
        resourceDetails.setClientSecret("forsrc");
        resourceDetails.setGrantType("password");
        resourceDetails.setScope(Arrays.asList("read", "write"));

        OAuth2RestTemplate tccOAuth2RestTemplate = new OAuth2RestTemplate(resourceDetails,
                new DefaultOAuth2ClientContext());
        tccOAuth2RestTemplate.setMessageConverters(Arrays.asList(new MappingJackson2HttpMessageConverter()));
        String accessToken = tccOAuth2RestTemplate.getAccessToken().getValue();

        WebSocketStompClient stompClient = new WebSocketStompClient(
                new SockJsClient(Arrays.asList(new WebSocketTransport(new StandardWebSocketClient()))));

        // stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        stompClient.setMessageConverter(new StringMessageConverter());

        StompSession session = stompClient.connect("ws://forsrc.local:10020/tcc/ws/tcc?access_token=" + accessToken,
                new StompSessionHandlerAdapter() {
                }).get(5, TimeUnit.SECONDS);

        System.out.println(session.isConnected());
        CompletableFuture<String> completableFuture = new CompletableFuture<>();
        session.subscribe("/topic/tcc", new TccStompSessionHandler(completableFuture));

        session.send("/app/tcc/d4e55207-db0a-4b8e-9691-90305cb51a44", "test");

        LOGGER.info("ws: {}", completableFuture.get());

        session.disconnect();
    }
}
