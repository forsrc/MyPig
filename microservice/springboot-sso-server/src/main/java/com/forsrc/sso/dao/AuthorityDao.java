package com.forsrc.sso.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.forsrc.sso.domain.entity.Authority;

@Repository
public interface AuthorityDao extends JpaRepository<Authority, String> {

}
