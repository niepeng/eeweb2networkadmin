package com.chengqianyun.eeweb2networkadmin.biz.entitys;

import java.util.List;
import java.util.Map;

public interface DeviceInfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(DeviceInfo record);

    int insertSelective(DeviceInfo record);

    DeviceInfo selectByPrimaryKey(Long id);

    DeviceInfo selectBySn(String sn);

    int updateByPrimaryKeySelective(DeviceInfo record);

    int updateByPrimaryKey(DeviceInfo record);

    Integer findPageCount(Map<String,String> map);

    List<DeviceInfo> findPage(Map<String,String> map);

    List<DeviceInfo> findAll();

    List<DeviceInfo> findByAreaId(long areaId);

    int deleteForExport();



}