package com.chengqianyun.eeweb2networkadmin.biz.enums;


import lombok.Getter;

/**
 * @author 聂鹏
 * @version 1.0
 * @date 18/4/12
 */
@Getter
public enum DeviceTypeEnum {

  temp(1,"温度", true),
  humi(2,"湿度", true),
  power(4,"电量", true),
  shine(8,"光照", true),
  pressure(16,"压力", true),
  smoke(32,"烟感", false),
  water(64,"浸水", false),
  electric(128,"停电来点", false),
  out(256,"开关量输出", false),
  ;

  final int id;
  final String name;
  final boolean isDataDevice;


  private DeviceTypeEnum(int id, String name, boolean isDataDevice) {
    this.id = id;
    this.name = name;
    this.isDataDevice = isDataDevice;
  }

  public static DeviceTypeEnum getOneById(int id) {
    for(DeviceTypeEnum tmp : DeviceTypeEnum.values()) {
      if(tmp.getId() == id) {
        return tmp;
      }
    }
    return null;
  }

  public static boolean hasType(int idValue, DeviceTypeEnum deviceTypeEnum) {
    if(deviceTypeEnum == null) {
      return false;
    }
    return (deviceTypeEnum.getId() & idValue) == deviceTypeEnum.getId();
  }

}