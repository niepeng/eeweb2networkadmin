package com.chengqianyun.eeweb2networkadmin.biz.entitys;

import java.util.List;
import java.util.Map;

public interface DeviceDataHistoryMapper {
    int deleteByPrimaryKey(Long id);

    int insert(DeviceDataHistory record);

    int insertSelective(DeviceDataHistory record);

    DeviceDataHistory selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DeviceDataHistory record);

    int updateByPrimaryKey(DeviceDataHistory record);

    Integer findPageCount(Map<String,String> map);

    List<DeviceDataHistory> findPage(Map<String,String> map);

    List<DeviceDataHistory> findPageAll(Map<String,String> map);

}