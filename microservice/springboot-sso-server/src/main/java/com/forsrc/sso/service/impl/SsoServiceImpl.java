package com.forsrc.sso.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    @CachePut(value = "spring/cache/sso/User", key = "#user.username")
    public User save(User user) {
       return userDao.save(user);
    }

    @Override
    @CachePut(value = "spring/cache/sso/User", key = "#user.username")
    public User update(User user) {
        return userDao.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "spring/cache/sso/User", key = "#username")
    public User getUserByUsername(String username) {
        return userDao.findOne(username);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "spring/cache/sso/Authority", key = "#username")
    public List<Authority> getAuthorityByUsername(String username) {
        Authority entity = new Authority();
        entity.setUsername(username);
        Example<Authority> example = Example.of(entity);
        return authorityDao.findAll(example);
    }

    @Override
    @CacheEvict(value = "spring/cache/sso/Authority", key = "#username")
    public void deleteAuthority(String username) {
        List<Authority> list = getAuthorityByUsername(username);
        for (Authority authority : list) {
            authorityDao.delete(authority);
        }
    }

    @Override
    /**
     * update Authorities<br/>
     * * all the username must be the same
     * @param list
     * @return
     */
    @CachePut(value = "spring/cache/sso/Authority", key = "#list.get(0).username")
    public List<Authority> update(List<Authority> list) {
       return authorityDao.save(list);
    }

    @Override
    /**
     * save Authorities<br/>
     * * all the username must be the same
     * @param list
     * @return
     */
    @CachePut(value = "spring/cache/sso/Authority", key = "#list.get(0).username")
    public List<Authority> save(List<Authority> list) {
        return authorityDao.save(list);
    }

    @Override
    @Cacheable(value = "spring/cache/sso/User/Page", key = "#page + '-' + #size")
    public Page<User> getUser(int page, int size) {
        Page<User> p = userDao.findAll(new PageRequest(page, size));
        return p;
    }

}
