package com.forsrc.common.core.tcc.exception;

import java.util.UUID;

import com.forsrc.common.core.tcc.status.Status;

public class TccTryException extends TccException {

    private static final long serialVersionUID = -8735263364193326850L;


    public TccTryException(UUID id, String message) {
        super(id, message, Status.TRY_ERROR);
    }

    public TccTryException(UUID id, String message, Status status) {
        super(id, message, status);
    }
}
