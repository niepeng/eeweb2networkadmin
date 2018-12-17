package com.chengqianyun.eeweb2networkadmin.biz.bean.api;


import lombok.Data;

/**
 * @author 聂鹏
 * @version 1.0
 * @date 18/12/17
 */
@Data
public class ElementDataResp {

  private long deviceId;

  private long areaId;

  private int deviceOneType;

  private String data;

  /**
   * StatusEnum
   */
  private int status;

  private String date;

}