package com.forsrc.sso.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.forsrc.sso.domain.entity.Authoritie;
import com.forsrc.sso.domain.entity.User;

@Service
@Transactional
public interface SsoService {

    public User getUserByUsername(String username);

    public void save(User entity);

    public void update(User entity);

    public List<Authoritie> getAuthoritieByUsername(String username);

    public void save(Authoritie entity);

    public void update(Authoritie entity);

}
