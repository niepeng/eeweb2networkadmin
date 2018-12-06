package com.chengqianyun.eeweb2networkadmin.biz.entitys;

import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceDataIntime;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DeviceDataIntimeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(DeviceDataIntime record);

    int insertSelective(DeviceDataIntime record);

    DeviceDataIntime selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DeviceDataIntime record);

    int updateByPrimaryKey(DeviceDataIntime record);

    /**
     * 每一个设备最新一条数据id
     * @return
     */
    List<Long> listDataOneIds();

    List<DeviceDataIntime> listData(@Param("ids") String ids);

    /**
     * 一个设备的最新的 recordNum 条记录
     * @return
     */
    List<DeviceDataIntime> listDataOneDevice(@Param("deviceId") long deviceId, @Param("recordNum") int recordNum);
    List<DeviceDataIntime> listDataOneDeviceNoOffline(@Param("deviceId") long deviceId, @Param("recordNum") int recordNum);

    DeviceDataIntime listDataOneDeviceSuccess(@Param("deviceId") long deviceId, @Param("date") Date date);

    Long hasRecentlyOne(@Param("deviceId") long deviceId, @Param("afterDate") Date afterDate);

  /**
   * 这个设备(deviceId):删除id小于当前值的设备
   */
    int deleteByDeviceId(@Param("deviceId") long deviceId, @Param("id") long id);

    int deleteAllByDeviceId(@Param("deviceId") long deviceId);

}