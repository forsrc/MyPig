package com.forsrc.sso.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.forsrc.common.core.tcc.status.Status;
import com.forsrc.sso.dao.UserTccDao;
import com.forsrc.sso.domain.entity.Authority;
import com.forsrc.sso.domain.entity.User;
import com.forsrc.sso.domain.entity.UserTcc;
import com.forsrc.sso.service.AuthorityService;
import com.forsrc.sso.service.UserService;
import com.forsrc.sso.service.UserTccService;
import com.forsrc.sso.config.PasswordEncoderConfig;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class UserTccServiceImpl implements UserTccService{

    private static final Logger LOGGER = LoggerFactory.getLogger(UserTccServiceImpl.class);

    @Autowired
    private UserService userService;

    @Autowired
    private AuthorityService authorityService;

    @Autowired
    private UserTccDao userTccDao;

    @Override
    public UserTcc tccTry(UserTcc userTcc) {
        userTcc.setId(null);
        userTcc.setPassword(PasswordEncoderConfig.PASSWORD_ENCODER.encode(userTcc.getPassword()));
        userTcc = userTccDao.save(userTcc);
        userTcc.setStatus(Status.TRY.getStatus());
        return userTcc;
    }

    @Override
    public UserTcc confirm(UUID id) {
        UserTcc userTcc = userTccDao.getOne(id);
        Assert.notNull(userTcc, "Not found userTcc: " + id);
        LOGGER.info("--> {}", userTcc);
        if (new Date().compareTo( userTcc.getExpire()) > 0) {
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
            return userTcc;
        } else {
            LOGGER.warn("--> UserTcc confirm error status: {}", userTcc);
        }
        return userTcc;
    }

    @Override
    public UserTcc cancel(UUID id) {
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

}
