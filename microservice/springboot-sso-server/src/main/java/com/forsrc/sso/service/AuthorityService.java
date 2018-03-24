package com.forsrc.sso.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.forsrc.sso.domain.entity.Authority;

@Service
@Transactional(rollbackFor = { Exception.class })
public interface AuthorityService {

    @Transactional(readOnly = true)
    public List<Authority> getByUsername(String username);

    /**
     * save Authorities<br/>
     * * all the username must be the same
     * 
     * @param list
     * @return
     */
    public List<Authority> save(List<Authority> list);

    /**
     * update Authorities<br/>
     * * all the username must be the same
     * 
     * @param list
     * @return
     */
    public List<Authority> update(List<Authority> list);

    public void delete(String username);

}
