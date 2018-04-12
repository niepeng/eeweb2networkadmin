package com.chengqianyun.eeweb2networkadmin.biz.entitys;

import com.chengqianyun.eeweb2networkadmin.biz.entitys.SmsPhone;

public interface SmsPhoneMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SmsPhone record);

    int insertSelective(SmsPhone record);

    SmsPhone selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SmsPhone record);

    int updateByPrimaryKey(SmsPhone record);
}