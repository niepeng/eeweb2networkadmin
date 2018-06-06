package com.chengqianyun.eeweb2networkadmin.biz.entitys;

import java.util.List;
import java.util.Map;

public interface DeviceAlarmMapper {
    int deleteByPrimaryKey(Long id);

    int insert(DeviceAlarm record);

    int insertSelective(DeviceAlarm record);

    DeviceAlarm selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DeviceAlarm record);

    int updateByPrimaryKey(DeviceAlarm record);

    Integer findPageCount(Map<String,String> map);

    Long hasData(DeviceAlarm deviceAlarm);

    List<DeviceAlarm> findPage(Map<String,String> map);

}