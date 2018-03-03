package com.forsrc.sso.domain.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonFormat;

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

    @Column(name = "create", insertable = false, updatable = false, nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern="yyyy-MM-dd'T'hh:mm:ss.SSS'Z'")
    private Date create;

    @Column(name = "update", insertable = false, updatable = false, nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern="yyyy-MM-dd'T'hh:mm:ss.SSS'Z'")
    private Date update;
    
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

    public Date getCreate() {
        return create;
    }

    public void setCreate(Date create) {
        this.create = create;
    }

    public Date getUpdate() {
        return update;
    }

    public void setUpdate(Date update) {
        this.update = update;
    }

    @Override
    public String toString() {
        return String.format("{\"username\":\"%s\", \"authority\":\"%s\", \"create\":\"%s\", \"update\":\"%s\"} ",
                username, authority, create, update);
    }


}
