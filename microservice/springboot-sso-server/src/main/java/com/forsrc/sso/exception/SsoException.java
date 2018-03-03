package com.forsrc.sso.exception;

public class SsoException extends RuntimeException {

    private static final long serialVersionUID = -3932224874119296571L;
    private Long id;

    public SsoException(Long id, String message) {
        super(message);
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
