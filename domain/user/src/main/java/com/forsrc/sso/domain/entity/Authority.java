package com.forsrc.sso.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "authorities", indexes = {
            @Index(name = "index_authorities_username", columnList = "username") },
            uniqueConstraints = { @UniqueConstraint(columnNames = { "username", "authority" })}
        )
@IdClass(Authority.class)
public class Authority implements java.io.Serializable {

    private static final long serialVersionUID = -1985182093016989312L;

    @Id
    @Column(name = "username", unique = false, length = 200, nullable = false)
    private String username;

    @Id
    @Column(name = "authority", unique = false, length = 200, nullable = false)
    private String authority;

    public Authority() {
    }

    public Authority(String username, String authority) {
        this.username = username;
        this.authority = authority;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

}
