package com.chengqianyun.eeweb2networkadmin.service;


import com.chengqianyun.eeweb2networkadmin.biz.entitys.AreaMapper;
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

}