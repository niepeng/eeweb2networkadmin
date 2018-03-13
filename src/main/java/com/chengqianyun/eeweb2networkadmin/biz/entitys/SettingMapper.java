package com.chengqianyun.eeweb2networkadmin.biz.entitys;

import java.util.List;
import java.util.Map;

public interface SettingMapper {

    Setting selectByCode(String code);

    Integer findPageCount(Map<String,String> map);

    List<Setting> findPage(Map<String,String> map);

    int deleteByPrimaryKey(Long id);

    int insert(Setting record);

    int insertSelective(Setting record);

    Setting selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Setting record);

    int updateByPrimaryKey(Setting record);
}