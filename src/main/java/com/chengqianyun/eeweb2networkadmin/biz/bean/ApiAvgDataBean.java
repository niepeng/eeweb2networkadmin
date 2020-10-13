package com.chengqianyun.eeweb2networkadmin.biz.bean;


import lombok.Data;
import lombok.ToString;

/**
 * @author 聂鹏
 * @version 1.0
 */
@Data
@ToString
public class ApiAvgDataBean {

  private Long deviceId;
  /**
   * 温度
   */
  private Integer tempMin;
  private Integer tempMax;
  private Integer tempAvg;

  /**
   * 湿度
   */
  private Integer humiMin;
  private Integer humiMax;
  private Integer humiAvg;

  /**
   * 电量
   */
  private Integer powerMin;
  private Integer powerMax;
  private Integer powerAvg;

  /**
   * 光照
   */
  private Integer shineMin;
  private Integer shineMax;
  private Integer shineAvg;

  /**
   * 压力
   */
  private Integer pressureMin;
  private Integer pressureMax;
  private Integer pressureAvg;

}