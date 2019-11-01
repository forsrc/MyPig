package com.forsrc.common.core.tcc.exception;

import com.forsrc.common.core.tcc.status.Status;

public class TccConfirmException extends TccException {

    private static final long serialVersionUID = 2896040574821454201L;

    public TccConfirmException(Long id, String message) {
        super(id, message, Status.CONFIRM_ERROR);
    }

    public TccConfirmException(Long id, String message, Status status) {
        super(id, message, status);
    }
}
