package com.forsrc.sso.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.forsrc.sso.dao.UserDao;
import com.forsrc.sso.domain.entity.User;
import com.forsrc.sso.service.UserService;

@Service
@Transactional(rollbackFor = { Exception.class })
public class UserServiceImpl implements UserService {

    private static final String TIMEOUT_REFRESH = "#${select.cache.timeout:1800}#${select.cache.refresh:600}";

    private static final String CACHE_VALUE = "spring/cache/sso/User";

    private static final String CACHE_VALUE_PAGE = "spring/cache/sso/User/Page";

    @Autowired
    private UserDao userDao;

    @Override
    @CachePut(value = CACHE_VALUE + TIMEOUT_REFRESH, key = "#user.username")
    @CacheEvict(value = CACHE_VALUE_PAGE)
    public User save(User user) {
        return userDao.save(user);
    }

    @Override
    @CachePut(value = CACHE_VALUE + TIMEOUT_REFRESH, key = "#user.username")
    @CacheEvict(value = CACHE_VALUE_PAGE)
    public User update(User user) {
        return userDao.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = CACHE_VALUE + TIMEOUT_REFRESH, key = "#username")
    public User getByUsername(String username) {
        return userDao.getOne(username);
    }

    @Override
    @Cacheable(value = CACHE_VALUE_PAGE, key = "#page + '-' + #size")
    public Page<User> get(int page, int size) {
        Page<User> p = userDao.findAll(new PageRequest(page, size));
        return p;
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = CACHE_VALUE_PAGE),
            @CacheEvict(value = CACHE_VALUE, key = "#username")
            })
    public void delete(String username) {
        userDao.deleteById(username);
    }

}
