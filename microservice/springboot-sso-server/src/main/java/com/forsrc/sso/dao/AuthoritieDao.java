package com.forsrc.sso.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.forsrc.sso.domain.entity.Authoritie;

@Repository
public interface AuthoritieDao extends JpaRepository<Authoritie, String> {

}
