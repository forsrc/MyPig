package com.forsrc.tcc.dao;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.forsrc.tcc.domain.entity.TccLink;

@Repository
public interface TccLinkDao extends JpaRepository<TccLink, UUID> {

}
