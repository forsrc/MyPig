package com.forsrc.sso.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "users", indexes = {
        @Index(name = "index_users_username", columnList = "username") }, uniqueConstraints = {
                @UniqueConstraint(columnNames = { "username" }) })
public class User implements java.io.Serializable {

    private static final long serialVersionUID = 7053075402341362549L;

    @Id
    // @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "username", unique = true, length = 200, nullable = false)
    private String username;

    @Column(name = "password", unique = true, length = 200, nullable = false)
    private String password;

    @Column(name = "enabled", length = 1, nullable = false, columnDefinition = "INT DEFAULT 1")
    private Integer enabled;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

}
