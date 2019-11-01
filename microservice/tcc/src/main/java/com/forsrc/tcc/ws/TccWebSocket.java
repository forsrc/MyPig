package com.forsrc.tcc.ws;

import java.security.Principal;
import java.util.List;

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
import org.springframework.transaction.annotation.Transactional;

import com.forsrc.common.core.tcc.dto.WsUserTccDto;
import com.forsrc.common.core.tcc.status.Status;
import com.forsrc.tcc.domain.entity.Tcc;
import com.forsrc.tcc.domain.entity.TccLink;
import com.forsrc.tcc.service.TccService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Controller
public class TccWebSocket {
    @Autowired
    private TccService tccService;
    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    private static final Logger LOGGER = LoggerFactory.getLogger(TccWebSocket.class);

    @MessageMapping("/tcc/{tccId}")
    @SendTo("/topic/tcc")
    @HystrixCommand()
    @Transactional(rollbackFor = { Exception.class })
    public WsUserTccDto tcc(@DestinationVariable("tccId") Long tccId, @Payload Message<WsUserTccDto> message,
            SimpMessageHeaderAccessor headerAccessor, Principal user) throws Exception {
        LOGGER.info("ws ttc: {} -> {} -> {} -> {}", tccId, user.getName(), message, headerAccessor);

        WsUserTccDto dto = message.getPayload();
        LOGGER.info("ws ttc: {} -> {}", tccId, dto);
        Tcc tcc = tccService.get(tccId);
        tcc.setStatus(dto.getStatus());
        tccService.update(tcc);

        dto.setEnd(true);
        messagingTemplate.convertAndSend(String.format("/topic/tcc/%s", tccId), dto);

        return dto;
    }

    @MessageMapping("/tccLink/{resourceId}")
    @SendTo("/topic/tccLink")
    @HystrixCommand()
    public WsUserTccDto tccLink(@DestinationVariable("resourceId") Long resourceId,
            @Payload Message<WsUserTccDto> message, SimpMessageHeaderAccessor headerAccessor, Principal user)
            throws Exception {
        LOGGER.info("ws tccLink: {} -> {} -> {} -> {}", resourceId, user.getName(), message, headerAccessor);

        WsUserTccDto dto = message.getPayload();
        LOGGER.info("ws tccLink: {} -> {}", resourceId, dto);
        TccLink tccLink = tccService.getTccLinkByResourceId(resourceId);
        tccLink.setStatus(dto.getStatus());
        tccService.update(tccLink);

        Tcc tcc = tccService.getTccByResourceId(resourceId);
        List<TccLink> links = tcc.getTccLinks();
        boolean isSucc = true;
        for (TccLink link : links) {
            isSucc &= Status.CONFIRM.getStatus() == link.getStatus();
        }
        if (isSucc) {
            dto.setEnd(true);
            for (TccLink link : links) {
                dto.setId(link.getId());
                dto.setStatus(link.getStatus());
                messagingTemplate.convertAndSend(String.format("/topic/tccLink/%s", link.getResourceId()), dto);
            }

        } else {
            messagingTemplate.convertAndSend(String.format("/topic/tccLink/%s", resourceId), dto);
        }

        return dto;
    }
}
