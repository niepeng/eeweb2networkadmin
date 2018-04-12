package com.chengqianyun.eeweb2networkadmin.biz.entitys;

import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceAlarm;

public interface DeviceAlarmMapper {
    int deleteByPrimaryKey(Long id);

    int insert(DeviceAlarm record);

    int insertSelective(DeviceAlarm record);

    DeviceAlarm selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DeviceAlarm record);

    int updateByPrimaryKey(DeviceAlarm record);
}