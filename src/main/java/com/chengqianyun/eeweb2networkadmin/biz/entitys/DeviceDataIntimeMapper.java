package com.chengqianyun.eeweb2networkadmin.biz.entitys;

import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceDataIntime;
import java.util.List;

public interface DeviceDataIntimeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(DeviceDataIntime record);

    int insertSelective(DeviceDataIntime record);

    DeviceDataIntime selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DeviceDataIntime record);

    int updateByPrimaryKey(DeviceDataIntime record);

    List<DeviceDataIntime> listAll();
}