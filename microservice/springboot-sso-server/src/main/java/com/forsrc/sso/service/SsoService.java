package com.forsrc.sso.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.forsrc.sso.domain.entity.Authority;
import com.forsrc.sso.domain.entity.User;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public interface SsoService {

    @Transactional(readOnly = true)
    public User getUserByUsername(String username);

    public void save(User entity);

    public void update(User entity);

    @Transactional(readOnly = true)
    public List<Authority> getAuthorityByUsername(String username);

    public void save(Authority entity);

    public void save(List<Authority> list);

    public void update(Authority entity);

    public void update(List<Authority> list);

    public void deleteAuthority(String username);

}
