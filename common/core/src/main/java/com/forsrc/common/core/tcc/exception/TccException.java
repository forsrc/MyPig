package com.forsrc.common.core.tcc.exception;

import com.forsrc.common.core.tcc.status.Status;

public class TccException extends Exception {


    private static final long serialVersionUID = -7540498410823440508L;

    protected Long id;

    protected Status status;

    public TccException(Long id, String message) {
        super(message);
        this.id = id;
        this.status = Status.ERROR;
    }

    public TccException(Long id, String message, Status status) {
        super(message);
        this.id = id;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

}
