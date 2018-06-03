package com.chengqianyun.eeweb2networkadmin.biz.entitys;

import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceDataIntime;
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

}