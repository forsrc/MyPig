package com.forsrc.tcc.dao;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import com.forsrc.tcc.domain.entity.Tcc;

@Repository
@RepositoryRestResource(collectionResourceRel = "tcc", path = "tcc" )
public interface TccDao extends JpaRepository<Tcc, UUID>, PagingAndSortingRepository<Tcc, UUID> {

}
