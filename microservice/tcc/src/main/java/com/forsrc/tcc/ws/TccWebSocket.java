package com.forsrc.tcc.ws;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import com.forsrc.common.utils.StringUtils;
import com.forsrc.tcc.domain.entity.Tcc;
import com.forsrc.tcc.domain.entity.TccLink;
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
    public String tcc(@DestinationVariable("tccId") String tccId, @Payload Message<String> message,
            SimpMessageHeaderAccessor headerAccessor, Principal user) throws Exception {
        LOGGER.info("ws ttc: {} -> {} -> {} -> {}", tccId, user.getName(), message, headerAccessor);

        String text = message.getPayload();
        LOGGER.info("ws ttc: {} -> {}", tccId, text);
        Tcc tcc = tccService.get(StringUtils.toUuid(tccId));

        messagingTemplate.convertAndSend("/topic/tcc/" + tccId, tcc.getStatus());

        return tcc != null ? tcc.toString() : tccId;
    }

    @MessageMapping("/tccLink/{tccLinkId}")
    @SendTo("/topic/tccLink")
    public String tccLink(@DestinationVariable("tccLinkId") String tccLinkId, @Payload Message<String> message,
            SimpMessageHeaderAccessor headerAccessor, Principal user) throws Exception {
        LOGGER.info("ws ttc: {} -> {} -> {} -> {}", tccLinkId, user.getName(), message, headerAccessor);

        String text = message.getPayload();
        LOGGER.info("ws ttc: {} -> {}", tccLinkId, text);
        TccLink tccLink = tccService.getTccLink(StringUtils.toUuid(tccLinkId));

        messagingTemplate.convertAndSend("/topic/tccLink/" + tccLinkId, tccLink.getStatus());

        return tccLink != null ? tccLink.toString() : tccLinkId;
    }
}
