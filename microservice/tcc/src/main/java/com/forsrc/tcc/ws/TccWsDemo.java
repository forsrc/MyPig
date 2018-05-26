package com.forsrc.tcc.ws;

import java.lang.reflect.Type;
import java.security.Principal;
import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.stereotype.Controller;

import com.forsrc.common.core.utils.WebSocketClientUtils;
import com.forsrc.common.utils.CompletableFutureUtils;
import com.forsrc.tcc.domain.entity.Tcc;
import com.forsrc.tcc.service.TccService;

@Controller
public class TccWsDemo {
    @Autowired
    private TccService tccService;
    @Autowired
    private SimpMessageSendingOperations messagingTemplate;
    @Autowired
    @Qualifier("oauth2RestTemplate")
    private OAuth2RestTemplate oauth2RestTemplate;

    private static final Logger LOGGER = LoggerFactory.getLogger(TccWsDemo.class);

    @MessageMapping("/demo/{tccId}")
    @SendTo("/topic/demo")
    public String send(@DestinationVariable("tccId") Long tccId, @Payload Message<String> message,
            SimpMessageHeaderAccessor headerAccessor, Principal user) throws Exception {
        String sessionId = headerAccessor.getSessionId();
        LOGGER.info("SimpMessageHeaderAccessor: {} -> {}", sessionId, headerAccessor);
        LOGGER.info("Message: {}", message);
        LOGGER.info("Principal: {} -> {}", user.getName(), user);
        // messagingTemplate.
        String text = message.getPayload();
        Tcc tcc = tccService.get(tccId);

        messagingTemplate.convertAndSend("/topic/demo", "test 1");
        messagingTemplate.convertAndSend("/topic/demo", "test 2", createHeaders(sessionId));
        messagingTemplate.convertAndSend("/topic/demo/" + tccId, "test 3 " + tccId);
        messagingTemplate.convertAndSendToUser(user.getName(), "/usermessage", "test user");

        messagingTemplate.convertAndSendToUser(user.getName(), "/usermessage", "test messagingTemplate",
                createHeaders(sessionId));

        return tcc != null ? tcc.toString() : String.valueOf(tccId);
    }

    private MessageHeaders createHeaders(String sessionId) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);
        return headerAccessor.getMessageHeaders();
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
//        String accessToken = tccOAuth2RestTemplate.getAccessToken().getValue();
//
// 
//        List<Transport> transports = new ArrayList<>();
//        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
//        RestTemplateXhrTransport xhrTransport = new RestTemplateXhrTransport(tccOAuth2RestTemplate);
// 
//        transports.add(xhrTransport);
//
//        SockJsClient sockJsClient = new SockJsClient(transports);
//
//        WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);
//        //stompClient.setMessageConverter(new MappingJackson2MessageConverter());
//        stompClient.setMessageConverter(new StringMessageConverter());
//
//        StompSession session = stompClient.connect("ws://forsrc.local:10020/tcc/ws/tcc?access_token=" + accessToken,
//                new StompSessionHandlerAdapter() {
//                }).get(5, TimeUnit.SECONDS);
//
//
//        System.out.println(session.isConnected());
//        System.out.println(session.getSessionId());
        final CompletableFuture<String> completableFuture1 = CompletableFutureUtils.withTimeout(Duration.ofSeconds(3));
        final CompletableFuture<String> completableFuture2 = CompletableFutureUtils.withTimeout(Duration.ofSeconds(3));
        final CompletableFuture<String> completableFuture3 = CompletableFutureUtils.withTimeout(Duration.ofSeconds(3));
        String ws = "ws://forsrc.local:10020/tcc/ws/tcc";
        WebSocketClientUtils
            .get(ws, new StringMessageConverter(), tccOAuth2RestTemplate)
            .set("/topic/demo", new TccStompSessionHandler(completableFuture1))
            .set("/user/tcc/usermessage", new TccStompSessionHandler(completableFuture2))
            .set("/topic/demo/" + "d4e55207-db0a-4b8e-9691-90305cb51a44", new TccStompSessionHandler(completableFuture3))
            .send("/app/demo/d4e55207-db0a-4b8e-9691-90305cb51a44", "test")
            .handle(new WebSocketClientUtils.Handler() {
                @Override
                public void handle(StompSession session) throws RuntimeException {
                    // TODO Auto-generated method stub
                    try {
                        LOGGER.info("--> ws1: {}", completableFuture1.get());
                        LOGGER.info("--> ws2: {}", completableFuture2.get());
                        LOGGER.info("--> ws3: {}", completableFuture3.get());
                        TimeUnit.SECONDS.sleep(3);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            })
            .disconnect();


    }

    static class TccStompSessionHandler extends StompSessionHandlerAdapter {

        private static final Logger LOGGER = LoggerFactory.getLogger(TccStompSessionHandler.class);
        private CompletableFuture<String> completableFuture ;

        public TccStompSessionHandler(CompletableFuture<String> completableFuture ) {
            this.completableFuture = completableFuture;
        }
        @Override
        public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
            LOGGER.info("New session established : " + session.getSessionId());
            //session.subscribe("/topic/demo", this);

            //LOGGER.info("Subscribed to /topic/messages");
     
            //session.send("/app/demo", "test".getBytes());

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
            System.out.println("payload --> " + text);
            LOGGER.info("Received : {}", text);
            completableFuture.complete(text);
        }
    }
}
