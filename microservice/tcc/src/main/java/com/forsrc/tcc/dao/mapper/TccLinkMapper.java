package com.forsrc.tcc.dao.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.forsrc.tcc.domain.entity.TccLink;

@Mapper
public interface TccLinkMapper {

    public TccLink getByResourceId(@Param("resourceId") Long resourceId);

}
