package com.chengqianyun.eeweb2networkadmin.biz.bean.api;


import lombok.Data;

/**
 * @author 聂鹏
 * @version 1.0
 * @date 18/12/17
 */
@Data
public class DeviceDataHistoryResp {

  private String time;

  /**
   * 温度
   */
  private String tempMin;
  private String tempMax;
  private String tempAvg;

  /**
   * 湿度
   */
  private String humiMin;
  private String humiMax;
  private String humiAvg;

  /**
   * 电量
   */
  private String powerMin;
  private String powerMax;
  private String powerAvg;

  /**
   * 光照
   */
  private String shineMin;
  private String shineMax;
  private String shineAvg;

  /**
   * 压力
   */
  private String pressureMin;
  private String pressureMax;
  private String pressureAvg;

}