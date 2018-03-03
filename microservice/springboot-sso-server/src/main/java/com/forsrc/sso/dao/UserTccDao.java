package com.forsrc.sso.dao;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.forsrc.sso.domain.entity.UserTcc;


@Repository
public interface UserTccDao extends JpaRepository<UserTcc, UUID> {

}
