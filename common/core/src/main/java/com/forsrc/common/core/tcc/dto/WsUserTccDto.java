package com.forsrc.common.core.tcc.dto;

import java.io.Serializable;

import com.forsrc.common.core.tcc.exception.TccException;

public class WsUserTccDto implements Serializable {
    private static final long serialVersionUID = -6117408218100477113L;

    private Long id;

    private int status;

    private TccException tccException;

    private boolean isEnd;

    public WsUserTccDto() {
    }

    public WsUserTccDto(Long id, int status) {
        this.id = id;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
