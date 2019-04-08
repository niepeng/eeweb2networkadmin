package com.chengqianyun.eeweb2networkadmin.biz.entitys;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

public interface DeviceDataHistoryMapper {
    int deleteByPrimaryKey(Long id);

    int insert(DeviceDataHistory record);

    int insertSelective(DeviceDataHistory record);

    DeviceDataHistory selectByPrimaryKey(Long id);

    Long findData(@Param("deviceId") long deviceId, @Param("dateStr") String dateStr);

    int updateByPrimaryKeySelective(DeviceDataHistory record);

    int updateByPrimaryKey(DeviceDataHistory record);

    Integer findPageCount(Map<String,String> map);

    List<DeviceDataHistory> findPage(Map<String,String> map);

    List<DeviceDataHistory> findPageAll(Map<String,String> map);

    void deleteByTime(String maxTime);

}