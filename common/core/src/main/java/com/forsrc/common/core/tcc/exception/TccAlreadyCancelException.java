package com.forsrc.common.core.tcc.exception;

import com.forsrc.common.core.tcc.status.Status;

public class TccAlreadyCancelException extends TccException {

    private static final long serialVersionUID = 2920085266009787637L;

    public TccAlreadyCancelException(Long id, String message) {
        super(id, message, Status.ALREADY_CANCELED);
    }

    public TccAlreadyCancelException(Long id, String message, Status status) {
        super(id, message, status);
    }
}
