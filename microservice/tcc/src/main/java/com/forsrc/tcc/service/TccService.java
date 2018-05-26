package com.forsrc.tcc.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.forsrc.common.core.tcc.exception.TccException;
import com.forsrc.tcc.domain.entity.Tcc;
import com.forsrc.tcc.domain.entity.TccLink;

@Service
@Transactional(rollbackFor = { Exception.class })
public interface TccService {

    @Transactional(readOnly = true)
    public Tcc get(Long id);

    @Transactional(readOnly = true)
    public TccLink getTccLink(UUID id);

    @Transactional(readOnly = true)
    public Tcc getTccByResourceId(Long resourceId);

    @Transactional(readOnly = true)
    public TccLink getTccLinkByResourceId(Long resourceId);

    public int setTccMicroservice(String microservice);

    public Tcc save(Tcc tcc);

    public Tcc update(Tcc tcc);

    public TccLink update(TccLink tccLink);

    public void delete(Long id);

    public Tcc confirm(Long id, String accessToken) throws TccException;

    public Tcc cancel(Long id, String accessToken) throws TccException;

    @Transactional(readOnly = true)
    public List<Tcc> getTryStatusList();

    @Transactional(readOnly = true)
    public List<Tcc> getTryStatusList(String microservice);

}
