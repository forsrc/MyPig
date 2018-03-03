package com.forsrc.common.core.tcc.exception;

import java.util.UUID;

import com.forsrc.common.core.tcc.status.Status;

public class TccLinkNotFoundException extends TccException {

    private static final long serialVersionUID = 1107592099215092054L;

    public TccLinkNotFoundException(UUID id, String message) {
        super(id, message, Status.TRY_ERROR);
    }

    public TccLinkNotFoundException(UUID id, String message, Status status) {
        super(id, message, status);
    }
}
