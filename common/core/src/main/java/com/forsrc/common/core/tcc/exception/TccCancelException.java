package com.forsrc.common.core.tcc.exception;

import java.util.UUID;

import com.forsrc.common.core.tcc.status.Status;

public class TccCancelException extends TccException {

    private static final long serialVersionUID = 7472788480727705253L;

    public TccCancelException(UUID id, String message) {
        super(id, message, Status.CANCEL_ERROR);
    }

    public TccCancelException(UUID id, String message, Status status) {
        super(id, message, status);
    }

}
