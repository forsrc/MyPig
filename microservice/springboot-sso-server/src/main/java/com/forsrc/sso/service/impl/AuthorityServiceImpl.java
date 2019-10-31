package com.forsrc.sso.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.forsrc.sso.dao.AuthorityDao;
import com.forsrc.sso.domain.entity.Authority;
import com.forsrc.sso.service.AuthorityService;

@Service
@Transactional(rollbackFor = { Exception.class })
public class AuthorityServiceImpl implements AuthorityService {

    @Autowired
    private AuthorityDao authorityDao;
    
    private static final String CACHE_NAME = "spring/cache/sso/Authority";

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = CACHE_NAME, key = "#username")
    public List<Authority> getByUsername(String username) {
        Authority entity = new Authority();
        entity.setUsername(username);
        Example<Authority> example = Example.of(entity);
        return authorityDao.findAll(example);
    }

    @Override
    @CacheEvict(value = CACHE_NAME, key = "#username")
    public void delete(String username) {
        List<Authority> list = getByUsername(username);
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
    @CachePut(value = CACHE_NAME, key = "#list.get(0).username")
    public List<Authority> update(List<Authority> list) {
       return authorityDao.saveAll(list);
    }

    @Override
    /**
     * save Authorities<br/>
     * * all the username must be the same
     * @param list
     * @return
     */
    @CachePut(value = CACHE_NAME, key = "#list.get(0).username")
    public List<Authority> save(List<Authority> list) {
        return authorityDao.saveAll(list);
    }

}
