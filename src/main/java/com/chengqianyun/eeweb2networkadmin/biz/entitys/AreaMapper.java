package com.chengqianyun.eeweb2networkadmin.biz.entitys;

import java.util.List;
import java.util.Map;

public interface AreaMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Area record);

    int insertSelective(Area record);

    Area selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Area record);

    int updateByPrimaryKey(Area record);

    Integer findPageCount(Map<String,String> map);

    List<Area> findPage(Map<String,String> map);

    Area selectByName(String name);

    List<Area> listAll();

    int deleteForExport();
}