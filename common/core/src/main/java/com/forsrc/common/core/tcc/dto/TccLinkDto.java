package com.forsrc.common.core.tcc.dto;

import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

public class TccLinkDto implements java.io.Serializable {

    private static final long serialVersionUID = -3603568859174762821L;

    private UUID id;

    private UUID tccId;

    private String uri;

    private String path;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Date create;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Date update;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Date expire;

    private Integer status;

    private int version;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Date getExpire() {
        return expire;
    }

    public void setExpire(Date expire) {
        this.expire = expire;
    }

    public UUID getTccId() {
        return tccId;
    }

    public void setTccId(UUID tccId) {
        this.tccId = tccId;
    }

    public Date getCreate() {
        return create;
    }

    public void setCreate(Date create) {
        this.create = create;
    }

    public Date getUpdate() {
        return update;
    }

    public void setUpdate(Date update) {
        this.update = update;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return String.format(
                "{\"id\":\"%s\", \"tccId\":\"%s\", \"uri\":\"%s\", \"path\":\"%s\", \"create\":\"%s\", \"update\":\"%s\", \"expire\":\"%s\", \"status\":\"%s\", \"version\":\"%s\"}",
                id, tccId, uri, path, create, update, expire, status, version);
    }

}
