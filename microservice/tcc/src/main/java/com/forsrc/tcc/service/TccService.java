package com.forsrc.tcc.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.forsrc.tcc.domain.entity.Tcc;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public interface TccService {

    @Transactional(readOnly = true)
    public Tcc get(UUID id);

    public Tcc save(Tcc tcc);

    public Tcc update(Tcc tcc);

    public void delete(UUID id);

    public Tcc confirm(UUID uuid, String accessToken);

    public Tcc cancel(UUID uuid, String accessToken);

    @Transactional(readOnly = true)
    public List<Tcc> getTryStatusList();
}
