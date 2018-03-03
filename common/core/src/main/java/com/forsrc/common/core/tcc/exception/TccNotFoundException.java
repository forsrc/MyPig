package com.forsrc.common.core.tcc.exception;

import java.util.UUID;

import com.forsrc.common.core.tcc.status.Status;

public class TccNotFoundException extends TccException {

    private static final long serialVersionUID = 3552561723073078070L;

    public TccNotFoundException(UUID id, String message) {
        super(id, message, Status.TRY_ERROR);
    }

    public TccNotFoundException(UUID id, String message, Status status) {
        super(id, message, status);
    }
}
