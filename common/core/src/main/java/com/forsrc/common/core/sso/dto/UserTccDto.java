package com.forsrc.common.core.sso.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class UserTccDto implements Serializable {

    private static final long serialVersionUID = -7276431400656160027L;

    private UUID id;

    private String username;

    private String password;

    private Integer enabled;

    private String authorities;

    private Integer status;

    private Date expire;

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

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getAuthorities() {
        return authorities;
    }

    public void setAuthorities(String authorities) {
        this.authorities = authorities;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getExpire() {
        return expire;
    }

    public void setExpire(Date expire) {
        this.expire = expire;
    }

    @Override
    public String toString() {
        return String.format(
                "{\"id\":\"%s\", \"username\":\"%s\", \"password\":\"%s\", \"enabled\":\"%s\", \"authorities\":\"%s\", \"status\":\"%s\", \"expire\":\"%s\"} ",
                id, username, password, enabled, authorities, status, expire);
    }
}
