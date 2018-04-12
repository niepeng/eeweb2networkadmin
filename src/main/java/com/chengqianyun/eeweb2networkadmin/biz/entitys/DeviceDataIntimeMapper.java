package com.chengqianyun.eeweb2networkadmin.biz.entitys;

import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceDataIntime;

public interface DeviceDataIntimeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(DeviceDataIntime record);

    int insertSelective(DeviceDataIntime record);

    DeviceDataIntime selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DeviceDataIntime record);

    int updateByPrimaryKey(DeviceDataIntime record);
}