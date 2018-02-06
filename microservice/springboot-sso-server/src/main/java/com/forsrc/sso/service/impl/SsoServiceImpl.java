package com.forsrc.sso.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.forsrc.sso.dao.AuthoritieDao;
import com.forsrc.sso.dao.UserDao;
import com.forsrc.sso.domain.entity.Authority;
import com.forsrc.sso.domain.entity.User;
import com.forsrc.sso.service.SsoService;

@Service
@Transactional
public class SsoServiceImpl implements SsoService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private AuthoritieDao authoritiesDao;

    @Override
    public void save(User entity) {
        userDao.save(entity);

    }

    @Override
    public void update(User entity) {
        userDao.save(entity);
    }

    @Override
    public void save(Authority entity) {
        authoritiesDao.save(entity);
    }

    @Override
    public void update(Authority entity) {
        authoritiesDao.save(entity);

    }

    @Override
    public User getUserByUsername(String username) {
        return userDao.findOne(username);
    }

    @Override
    public List<Authority> getAuthorityByUsername(String username) {
        Authority entity = new Authority();                         
        entity.setUsername(username);                          
        Example<Authority> example = Example.of(entity);
        return authoritiesDao.findAll(example);
    }

}
