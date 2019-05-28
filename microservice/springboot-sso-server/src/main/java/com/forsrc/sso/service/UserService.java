package com.forsrc.sso.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.forsrc.sso.domain.entity.User;

@Service
@Transactional(rollbackFor = { Exception.class })
public interface UserService {

    public User getByUsername(String username);

    public User save(User user);

    public User update(User user);

    public Page<User> get(int page, int size);

    public void delete(String username);

}
