package com.chengqianyun.eeweb2networkadmin.biz.enums;


import lombok.Getter;

/**
 * @author 聂鹏
 * @version 1.0
 * @date 18/4/12
 */
@Getter
public enum DeviceTypeEnum {

  temp(1,"温度"),
  humi(2,"湿度"),
  power(4,"电量"),
  shine(8,"光照"),
  pressure(16,"压力"),
  smoke(32,"烟感"),
  water(64,"浸水"),
  electric(128,"停电来点"),
  out(256,"开关量输出"),
  ;

  final int id;
  final String name;


  private DeviceTypeEnum(int id, String name) {
    this.id = id;
    this.name = name;
  }




}