package com.chengqianyun.eeweb2networkadmin.biz.bean.api;


import lombok.Data;

/**
 * @author 聂鹏
 * @version 1.0
 * @date 18/12/14
 */
@Data
public class DeviceResp {

  private long deviceId;

  /**
   * 所属区域
   */
  private long areaId;

  /**
   * sn号
   */
  private String sn;

  /**
   * 名称
   */
  private String name;

  /**
   * 标签
   */
  private String tag;

  /**
   * 设备地址
   */
  private int address;

  /**
   * 设备类型: DeviceTypeEnum 按位存储
   */
  private int type;

  /**
   * 温度上限:23.34
   */
  private String tempUp;

  /**
   * 温度下限:23.34
   */
  private String tempDown;

  /**
   * 温度校正值:23.34
   */
  private String tempDev;

  /**
   * 湿度上限:45.67
   */
  private String humiUp;

  /**
   * 湿度下限:45.67
   */
  private String humiDown;

  /**
   * 湿度校正值:45.67
   */
  private String humiDev;

  /**
   * 光照上限:原值
   */
  private String shineUp;

  /**
   * 光照下限:原值
   */
  private String shineDown;

  /**
   * 光照校正值:原值
   */
  private String shineDev;

  /**
   * 压力上限:800.1
   */
  private String pressureUp;

  /**
   * 压力下限:800.1
   */
  private String pressureDown;

  /**
   * 压力校正值:800.1
   */
  private String pressureDev;

}