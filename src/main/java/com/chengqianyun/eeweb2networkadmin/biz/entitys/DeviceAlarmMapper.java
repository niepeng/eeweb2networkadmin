package com.chengqianyun.eeweb2networkadmin.biz.entitys;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

public interface DeviceAlarmMapper {
    int deleteByPrimaryKey(Long id);

    int insert(DeviceAlarm record);

    int insertSelective(DeviceAlarm record);

    DeviceAlarm selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DeviceAlarm record);

    int updateByPrimaryKey(DeviceAlarm record);

    int resetDeviceAlarm(DeviceAlarm deviceAlarm);
    int resetDeviceAlarmAll(DeviceAlarm deviceAlarm);


    Integer findPageCount(Map<String,String> map);

    Long hasData(DeviceAlarm deviceAlarm);
    Long hasAlarmData();

    List<DeviceAlarm> findPage(Map<String,String> map);

    List<DeviceAlarm> selectAlarmByDeviceId(long deviceId);
    Long hasAlarmDataByDeviceId(long deviceId);
    Long hasAlarmDataByDeviceIdAndOneType(@Param("deviceId") long deviceId, @Param("deviceOneType") int deviceOneType);

}