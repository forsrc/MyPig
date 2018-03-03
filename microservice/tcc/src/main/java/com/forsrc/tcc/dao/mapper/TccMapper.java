package com.forsrc.tcc.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.forsrc.tcc.domain.entity.Tcc;

@Mapper
public interface TccMapper {

    public List<Tcc> getTryStatusList();
}
