package com.forsrc.common.core.tcc.dto;

import java.io.Serializable;
import java.util.UUID;

import com.forsrc.common.core.tcc.exception.TccException;

public class WsUserTccDto implements Serializable {
    private static final long serialVersionUID = -6117408218100477113L;

    private UUID id;

    private int status;

    private TccException tccException;

    private boolean isEnd;

    public WsUserTccDto() {
    }

    public WsUserTccDto(UUID id, int status) {
        this.id = id;
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public TccException getTccException() {
        return tccException;
    }

    public void setTccException(TccException tccException) {
        this.tccException = tccException;
    }

    public boolean isEnd() {
        return isEnd;
    }

    public void setEnd(boolean isEnd) {
        this.isEnd = isEnd;
    }

    @Override
    public String toString() {
        return String.format("{\"id\":\"%s\", \"status\":\"%s\", \"tccException\":\"%s\", \"isEnd\":\"%s\"} ", id,
                status, tccException, isEnd);
    }

}
