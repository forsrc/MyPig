package com.forsrc.common.core.tcc.exception;

import java.util.UUID;

import com.forsrc.common.core.tcc.status.Status;

public class TccAlreadyCancelException extends TccException {

    private static final long serialVersionUID = 2920085266009787637L;

    public TccAlreadyCancelException(UUID id, String message) {
        super(id, message, Status.ALREADY_CANCELED);
    }

    public TccAlreadyCancelException(UUID id, String message, Status status) {
        super(id, message, status);
    }
}
