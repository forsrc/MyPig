package com.forsrc.sso.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.forsrc.sso.domain.entity.Authority;
import com.forsrc.sso.domain.entity.User;

@Service
@Transactional
public interface SsoService {

    public User getUserByUsername(String username);

    public void save(User entity);

    public void update(User entity);

    public List<Authority> getAuthorityByUsername(String username);

    public void save(Authority entity);

    public void update(Authority entity);

}
