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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.forsrc.common.core.tcc.dto.WsUserTccDto;
import com.forsrc.common.core.tcc.status.Status;
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
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public WsUserTccDto tcc(@DestinationVariable("tccId") String tccId, @Payload Message<WsUserTccDto> message,
            SimpMessageHeaderAccessor headerAccessor, Principal user) throws Exception {
        LOGGER.info("ws ttc: {} -> {} -> {} -> {}", tccId, user.getName(), message, headerAccessor);

        WsUserTccDto dto = message.getPayload();
        LOGGER.info("ws ttc: {} -> {}", tccId, dto);
        Tcc tcc = tccService.get(StringUtils.toUuid(tccId));
        tcc.setStatus(dto.getStatus());
        tccService.update(tcc);

        dto.setEnd(true);
        messagingTemplate.convertAndSend(String.format("/topic/tcc/%s", tccId), dto);

        return dto;
    }

    @MessageMapping("/tccLink/{path}")
    @SendTo("/topic/tccLink")
    public WsUserTccDto tccLink(@DestinationVariable("path") String path,
            @Payload Message<WsUserTccDto> message, SimpMessageHeaderAccessor headerAccessor, Principal user)
            throws Exception {
        LOGGER.info("ws tccLink: {} -> {} -> {} -> {}", path, user.getName(), message, headerAccessor);

        WsUserTccDto dto = message.getPayload();
        LOGGER.info("ws tccLink: {} -> {}", path, dto);
        TccLink tccLink = tccService.getTccLinkByPath(path);
        tccLink.setStatus(dto.getStatus());
        tccService.update(tccLink);

        Tcc tcc = tccService.getTccByPath(path);
        List<TccLink> links = tcc.getLinks();
        boolean isSucc = true;
        for (TccLink link : links) {
            isSucc &= Status.CONFIRM.getStatus() == link.getStatus();
        }
        if (isSucc) {
            dto.setEnd(true);
            for (TccLink link : links) {
                dto.setId(link.getId());
                dto.setStatus(link.getStatus());
                messagingTemplate.convertAndSend(String.format("/topic/tccLink/%s", link.getPath()), dto);
            }

        } else {
            messagingTemplate.convertAndSend(String.format("/topic/tccLink/%s", path), dto);
        }

        return dto;
    }
}
