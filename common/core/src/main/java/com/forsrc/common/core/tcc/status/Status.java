package com.forsrc.common.core.tcc.status;

public enum Status {

    TRY(0),
    CONFIRM(1),
    CANCEL(2),
    ERROR(3),
    TRY_ERROR(4),
    CONFIRM_ERROR(5),
    CANCEL_ERROR(6),
    ALREADY_CONFIRMED(7),
    ALREADY_CANCELED(8),
    TCC_NOT_FOUND(9),
    TCC_LINK_NOT_FOUND(10),
    TCC_TIMEOUT(11)
    ;

    private int status;

    private Status(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Status of(int status) {
        Status[] statuses = values();
        for (Status s : statuses) {
            if (s.getStatus() == status) {
                return s;
            }
        }
        return Status.ERROR;
    }
}
