package com.forsrc.sso.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.forsrc.sso.dao.AuthorityDao;
import com.forsrc.sso.dao.UserDao;
import com.forsrc.sso.domain.entity.Authority;
import com.forsrc.sso.domain.entity.User;
import com.forsrc.sso.service.SsoService;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class SsoServiceImpl implements SsoService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private AuthorityDao authorityDao;

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
        authorityDao.save(entity);
    }

    @Override
    public void update(Authority entity) {
        authorityDao.save(entity);

    }

    @Override
    @Transactional(readOnly = true)
    public User getUserByUsername(String username) {
        return userDao.findOne(username);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "spring/cache/Authority", key = "#username")
    public List<Authority> getAuthorityByUsername(String username) {
        Authority entity = new Authority();
        entity.setUsername(username);
        Example<Authority> example = Example.of(entity);
        return authorityDao.findAll(example);
    }

    @Override
    public void deleteAuthority(String username) {
        List<Authority> list = getAuthorityByUsername(username);
        for (Authority authority : list) {
            authorityDao.delete(authority);
        }
    }

    @Override
    public void update(List<Authority> list) {
        authorityDao.save(list);
    }

    @Override
    public void save(List<Authority> list) {
        authorityDao.save(list);
    }

}
