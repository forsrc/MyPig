package com.forsrc.sso.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import com.forsrc.sso.domain.entity.User;

@Repository
@RepositoryRestResource(collectionResourceRel = "user", path = "user")
public interface UserDao extends JpaRepository<User, String>, PagingAndSortingRepository<User, String> {

}
