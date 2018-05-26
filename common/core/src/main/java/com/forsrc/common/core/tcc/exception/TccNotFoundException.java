package com.forsrc.common.core.tcc.exception;

import com.forsrc.common.core.tcc.status.Status;

public class TccNotFoundException extends TccException {

    private static final long serialVersionUID = 3552561723073078070L;

    public TccNotFoundException(Long id, String message) {
        super(id, message, Status.TRY_ERROR);
    }

    public TccNotFoundException(Long id, String message, Status status) {
        super(id, message, status);
    }
}
