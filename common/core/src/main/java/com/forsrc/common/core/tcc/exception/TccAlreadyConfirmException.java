package com.forsrc.common.core.tcc.exception;

import java.util.UUID;

import com.forsrc.common.core.tcc.status.Status;

public class TccAlreadyConfirmException extends TccException {

    private static final long serialVersionUID = -7842321215529870623L;

    public TccAlreadyConfirmException(UUID id, String message) {
        super(id, message, Status.ALREADY_CONFIRMED);
    }

    public TccAlreadyConfirmException(UUID id, String message, Status status) {
        super(id, message, status);
    }
}
