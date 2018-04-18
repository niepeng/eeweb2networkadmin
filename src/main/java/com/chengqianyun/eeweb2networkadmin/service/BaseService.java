package com.chengqianyun.eeweb2networkadmin.service;


import com.chengqianyun.eeweb2networkadmin.biz.entitys.AreaMapper;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceDataIntimeMapper;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceInfoMapper;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.OutConditionMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author 聂鹏
 * @version 1.0
 * @date 18/3/13
 */
@Slf4j
public class BaseService {

  @Autowired
  protected AreaMapper areaMapper;

  @Autowired
  protected DeviceInfoMapper deviceInfoMapper;

  @Autowired
  protected OutConditionMapper outConditionMapper;

  @Autowired
  protected DeviceDataIntimeMapper dataIntimeMapper;
}