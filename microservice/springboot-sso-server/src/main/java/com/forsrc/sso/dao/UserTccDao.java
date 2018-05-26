package com.forsrc.sso.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import com.forsrc.sso.domain.entity.UserTcc;


@Repository
@RepositoryRestResource(collectionResourceRel = "userTcc", path = "userTcc")
public interface UserTccDao extends JpaRepository<UserTcc, Long>, PagingAndSortingRepository<UserTcc, Long> {

}
