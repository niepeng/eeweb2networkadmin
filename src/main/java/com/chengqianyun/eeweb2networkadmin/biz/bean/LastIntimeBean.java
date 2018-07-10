package com.chengqianyun.eeweb2networkadmin.biz.bean;


import lombok.Data;
import lombok.ToString;

/**
 * @author 聂鹏
 * @version 1.0
 * @date 18/7/10
 */
@Data
@ToString
public class LastIntimeBean {

  // 设备类型
  private String deviceTypes = "";
  // 报警状态
  private String statuses = "";
  // 设备区域
  private String areaIds = "";

}