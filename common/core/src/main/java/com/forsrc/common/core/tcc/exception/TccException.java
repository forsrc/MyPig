package com.forsrc.common.core.tcc.exception;

import java.util.UUID;

import com.forsrc.common.core.tcc.status.Status;

public class TccException extends RuntimeException {


    private static final long serialVersionUID = -7540498410823440508L;

    protected UUID id;

    protected Status status;

    public TccException(UUID id, String message) {
        super(message);
        this.id = id;
        this.status = Status.ERROR;
    }

    public TccException(UUID id, String message, Status status) {
        super(message);
        this.id = id;
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

}
