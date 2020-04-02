package com.forsrc.common.core.utils;

import java.lang.reflect.Type;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;

import com.forsrc.common.core.tcc.dto.WsUserTccDto;
import com.forsrc.common.utils.CompletableFutureUtils;


public class TccWsUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(TccWsUtils.class);
    
    public static void confirmTcc(Long id, int status, String tccWsUri, OAuth2RestTemplate  tccOAuth2RestTemplate) throws InterruptedException, ExecutionException, TimeoutException {
        final CompletableFuture<WsUserTccDto> completableFuture = CompletableFutureUtils.withTimeout(Duration.ofSeconds(10));
        confirmTcc(id, status, tccWsUri, new WebSocketClientUtils.Handler() {
                    @Override
                    public void handle(StompSession session) throws RuntimeException {
                        try {
                            LOGGER.info("--> ws: /topic/tccLink/{} ...", id);
                            LOGGER.info("--> ws: {}", completableFuture.get());
                        } catch (Exception e) {
                            throw new RuntimeException(e.getMessage());
                        }
                    }
                } , tccOAuth2RestTemplate);;
    }
    
    public static void confirmTcc(Long id, int status, String tccWsUri, WebSocketClientUtils.Handler handler, OAuth2RestTemplate  tccOAuth2RestTemplate) throws InterruptedException, ExecutionException, TimeoutException {

        WsUserTccDto dto = new WsUserTccDto(id, status);
        final CompletableFuture<WsUserTccDto> completableFuture = CompletableFutureUtils.withTimeout(Duration.ofSeconds(10));

        WebSocketClientUtils.get(tccWsUri, new MappingJackson2MessageConverter(), tccOAuth2RestTemplate)
                .set(String.format("/topic/tccLink/%s", id), new TccStompSessionHandler(completableFuture))
                .send(String.format("/app/tccLink/%s", id), dto)
                .handle(handler).disconnect();
    }

    static class TccStompSessionHandler extends StompSessionHandlerAdapter {

        private static final Logger LOGGER = LoggerFactory.getLogger(TccStompSessionHandler.class);
        private CompletableFuture<WsUserTccDto> completableFuture;

        public TccStompSessionHandler(CompletableFuture<WsUserTccDto> completableFuture) {
            this.completableFuture = completableFuture;
        }

        @Override
        public Type getPayloadType(StompHeaders headers) {
            return WsUserTccDto.class;
        }

        @Override
        public void handleFrame(StompHeaders headers, Object payload) {
            WsUserTccDto dto = (WsUserTccDto) payload;
            LOGGER.info("Received : {}", dto);
            if (dto.getTccException() != null) {
                completableFuture.completeExceptionally(dto.getTccException());
                return;
            }
            if (dto.isEnd()) {
                completableFuture.complete(dto);
            }

        }
    }
}
