package com.chengqianyun.eeweb2networkadmin.biz.entitys;

import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceDataHistory;

public interface DeviceDataHistoryMapper {
    int deleteByPrimaryKey(Long id);

    int insert(DeviceDataHistory record);

    int insertSelective(DeviceDataHistory record);

    DeviceDataHistory selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DeviceDataHistory record);

    int updateByPrimaryKey(DeviceDataHistory record);
}