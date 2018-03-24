package com.forsrc.sso.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.forsrc.common.core.tcc.exception.TccException;
import com.forsrc.sso.domain.entity.UserTcc;

@Service
@Transactional(rollbackFor = { Exception.class, TccException.class })
public interface UserTccService {

    public UserTcc tccTry(UserTcc tcc) throws TccException;

    public UserTcc confirm(String id) throws TccException;

    public UserTcc cancel(String id) throws TccException;

}
