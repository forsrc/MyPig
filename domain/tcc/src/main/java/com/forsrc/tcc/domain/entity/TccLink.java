package com.forsrc.tcc.domain.entity;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;


@Entity
@Table(name = "t_tcc_link")
public class TccLink implements java.io.Serializable {

    private static final long serialVersionUID = -3603568859174762821L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @Column(name = "tcc_id", nullable = false)
    private UUID tccId;

    @Column(name = "entity_id", length = 500, nullable = false)
    private String entityId;

    @Column(name = "uri", length = 1000, nullable = false)
    private String uri;

    @Column(name = "create", insertable = false, updatable = false, nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern="yyyy-MM-dd'T'hh:mm:ss.SSS'Z'")
    private Date create;

    @Column(name = "update", insertable = false, updatable = false, nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern="yyyy-MM-dd'T'hh:mm:ss.SSS'Z'")
    private Date update;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern="yyyy-MM-dd'T'hh:mm:ss.SSS'Z'")
    @Column(name = "expire", nullable = false, columnDefinition = "TIMESTAMP DEFAULT TIMESTAMPADD(MINUTE, 5, CURRENT_TIMESTAMP)")
    private Date expire;
 
    @Column(name = "status", length = 2, nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer status;

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

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format(
                "{\"id\":\"%s\", \"tccId\":\"%s\", \"entityId\":\"%s\", \"uri\":\"%s\", \"create\":\"%s\", \"update\":\"%s\", \"expire\":\"%s\", \"status\":\"%s\"} ",
                id, tccId, entityId, uri, create, update, expire, status);
    }

}
