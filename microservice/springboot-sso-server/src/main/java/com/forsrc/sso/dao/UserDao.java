package com.forsrc.sso.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.forsrc.sso.domain.entity.User;


@Repository
public interface UserDao extends JpaRepository<User, String> {

}
