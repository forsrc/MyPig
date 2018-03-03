package com.forsrc.tcc.dao;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.forsrc.tcc.domain.entity.TccLink;


public interface TccLinkDao extends JpaRepository<TccLink, UUID> {

}
