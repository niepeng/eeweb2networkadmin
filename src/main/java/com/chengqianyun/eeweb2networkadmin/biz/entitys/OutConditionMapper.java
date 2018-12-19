package com.chengqianyun.eeweb2networkadmin.biz.entitys;

import com.chengqianyun.eeweb2networkadmin.biz.entitys.OutCondition;
import java.util.List;

public interface OutConditionMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OutCondition record);

    int insertSelective(OutCondition record);

    OutCondition selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OutCondition record);

    int updateByPrimaryKey(OutCondition record);

    Integer deleteBySn(String sn);

    List<OutCondition> listByDeviceId(long deviceId);

    List<OutCondition> selectConditionSn(String condtionSn);
}