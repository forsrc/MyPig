package com.forsrc.sso.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.forsrc.sso.domain.entity.UserTcc;


@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public interface UserTccService {

    public UserTcc tccTry(UserTcc tcc);

    public UserTcc confirm(UUID id);

    public UserTcc cancel(UUID id);

}
