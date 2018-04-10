package com.forsrc.sso.domain.entity;

public class AuthorityPk implements java.io.Serializable {

    private static final long serialVersionUID = -1985182093016989312L;

    private String username;

    private String authority;

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
