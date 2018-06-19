package com.forsrc.common.core.tcc.exception;

import com.forsrc.common.core.tcc.status.Status;

public class TccAlreadyConfirmException extends TccException {

    private static final long serialVersionUID = -7842321215529870623L;

    public TccAlreadyConfirmException(Long id, String message) {
        super(id, message, Status.ALREADY_CONFIRMED);
    }

    public TccAlreadyConfirmException(Long id, String message, Status status) {
        super(id, message, status);
    }
}
