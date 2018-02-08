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

    public User save(User user);

    public User update(User user);

    @Transactional(readOnly = true)
    public List<Authority> getAuthorityByUsername(String username);

    /**
     * save Authorities<br/>
     * * all the username must be the same
     * @param list
     * @return
     */
    public List<Authority> save(List<Authority> list);

    /**
     * update Authorities<br/>
     * * all the username must be the same
     * @param list
     * @return
     */
    public List<Authority> update(List<Authority> list);

    public void deleteAuthority(String username);

}
