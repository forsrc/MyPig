package com.forsrc.sso.service.impl;

import java.lang.reflect.Type;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.forsrc.common.core.tcc.dto.WsUserTccDto;
import com.forsrc.common.core.tcc.exception.TccConfirmException;
import com.forsrc.common.core.tcc.exception.TccException;
import com.forsrc.common.core.tcc.status.Status;
import com.forsrc.common.core.utils.WebSocketClientUtils;
import com.forsrc.common.utils.CompletableFutureUtils;
import com.forsrc.common.utils.SnowflakeIDGenerator;
import com.forsrc.common.utils.StringUtils;
import com.forsrc.sso.config.PasswordEncoderConfig;
import com.forsrc.sso.dao.UserTccDao;
import com.forsrc.sso.domain.entity.Authority;
import com.forsrc.sso.domain.entity.User;
import com.forsrc.sso.domain.entity.UserTcc;
import com.forsrc.sso.service.AuthorityService;
import com.forsrc.sso.service.UserService;
import com.forsrc.sso.service.UserTccService;

@Service
@Transactional(rollbackFor = { Exception.class, TccException.class })
public class UserTccServiceImpl implements UserTccService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserTccServiceImpl.class);

    @Value("${tcc.ws}")
    private String tccWs;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthorityService authorityService;

    @Autowired
    private UserTccDao userTccDao;

    @Autowired
    private Executor asyncExecutor;

    @Autowired
    private OAuth2RestTemplate tccOAuth2RestTemplate;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserTcc tccTry(UserTcc userTcc) {
        userTcc.setId(SnowflakeIDGenerator.get().getId());
        userTcc.setPassword(passwordEncoder.encode(userTcc.getPassword()));
        userTcc.setStatus(Status.TRY.getStatus());
        userTcc = userTccDao.save(userTcc);
        return userTcc;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, TccException.class})
    public UserTcc confirm(Long id) throws TccConfirmException {

        UserTcc userTcc = userTccDao.getOne(id);
        Assert.notNull(userTcc, "Not found userTcc: " + id);
        LOGGER.info("--> {}", userTcc);
        if (new Date().compareTo(userTcc.getExpire()) > 0) {
            userTcc.setStatus(Status.TCC_TIMEOUT.getStatus());
            userTcc = userTccDao.save(userTcc);
            return userTcc;
        }
        if (userTcc.getStatus() == Status.TRY.getStatus()) {
            userTcc.setStatus(Status.CONFIRM.getStatus());
            User user = new User();
            user.setUsername(userTcc.getUsername());
            user.setPassword(userTcc.getPassword());
            user.setEnabled(userTcc.getEnabled());
            userService.save(user);
            String[] authorities = userTcc.getAuthorities().split(",");
            List<Authority> list = new ArrayList<>(authorities.length);
            for (String authority : authorities) {
                list.add(new Authority(user.getUsername(), authority.trim()));
            }
            authorityService.save(list);
            userTcc = userTccDao.save(userTcc);
            try {
                confirm(id, userTcc.getStatus());
            } catch (Exception e) {
                throw new TccConfirmException(id, e.getMessage());
            }
            return userTcc;
        } else {
            LOGGER.warn("--> UserTcc confirm error status: {}", userTcc);
        }
        return userTcc;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, TccException.class})
    public UserTcc cancel(Long id) {

        UserTcc userTcc = userTccDao.getOne(id);
        Assert.notNull(userTcc, "Not found userTcc: " + id);
        LOGGER.info("--> {}", userTcc);
        if (userTcc.getStatus() == Status.TRY.getStatus()) {
            userTcc.setStatus(Status.CANCEL.getStatus());
            userTcc = userTccDao.save(userTcc);
        } else {
            LOGGER.warn("--> UserTcc cancel error status: {}", userTcc);
        }
        return userTcc;
    }

    @Transactional(rollbackFor = {Exception.class, TccException.class})
    private void confirm(Long id, int status) throws InterruptedException, ExecutionException, TimeoutException {

        WsUserTccDto dto = new WsUserTccDto(id, status);
        final CompletableFuture<WsUserTccDto> completableFuture = CompletableFutureUtils.withTimeout(Duration.ofSeconds(10));
        WebSocketClientUtils.get(tccWs, new MappingJackson2MessageConverter(), tccOAuth2RestTemplate)
                .set(String.format("/topic/tccLink/%s", id), new TccStompSessionHandler(completableFuture))
                .send(String.format("/app/tccLink/%s", id), dto)
                .handle(new WebSocketClientUtils.Handler() {
                    @Override
                    public void handle(StompSession session) throws RuntimeException {
                        try {
                            LOGGER.info("--> ws: /topic/tccLink/{} ...", id);
                            LOGGER.info("--> ws: {}", completableFuture.get());
                        } catch (Exception e) {
                            throw new RuntimeException(e.getMessage());
                        }
                    }
                }).disconnect();
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
