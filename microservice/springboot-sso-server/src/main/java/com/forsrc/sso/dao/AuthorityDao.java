package com.forsrc.sso.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import com.forsrc.sso.domain.entity.Authority;
import com.forsrc.sso.domain.entity.AuthorityPk;

@Repository
@RepositoryRestResource(collectionResourceRel = "authority", path = "authority")
public interface AuthorityDao
        extends JpaRepository<Authority, AuthorityPk>, PagingAndSortingRepository<Authority, AuthorityPk> {

}
