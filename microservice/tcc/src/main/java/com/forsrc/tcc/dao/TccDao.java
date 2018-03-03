package com.forsrc.tcc.dao;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.forsrc.tcc.domain.entity.Tcc;


public interface TccDao extends JpaRepository<Tcc, UUID> {

}
