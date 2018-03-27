package com.forsrc.common.core.tcc.dto;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

public class TccDto implements java.io.Serializable {

    private static final long serialVersionUID = 4706144731706609711L;

    private UUID id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Date create;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Date update;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Date expire;

    private Integer status;

    private Integer times;

    private List<TccLinkDto> links;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Date getCreate() {
        return create;
    }

    public void setCreate(Date create) {
        this.create = create;
    }

    public Date getExpire() {
        return expire;
    }

    public void setExpire(Date expire) {
        this.expire = expire;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getUpdate() {
        return update;
    }

    public void setUpdate(Date update) {
        this.update = update;
    }

    public List<TccLinkDto> getLinks() {
        return links;
    }

    public void setLinks(List<TccLinkDto> links) {
        this.links = links;
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }

    @Override
    public String toString() {
        return String.format(
                "{\"id\":\"%s\", \"create\":\"%s\", \"update\":\"%s\", \"expire\":\"%s\", \"status\":\"%s\", \"times\":\"%s\", \"links\":\"%s\"} ",
                id, create, update, expire, status, times, links);
    }

}
