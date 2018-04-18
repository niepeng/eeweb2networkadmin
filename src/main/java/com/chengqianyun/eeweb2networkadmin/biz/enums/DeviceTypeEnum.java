package com.chengqianyun.eeweb2networkadmin.biz.enums;


import lombok.Getter;

/**
 * @author 聂鹏
 * @version 1.0
 * @date 18/4/12
 */
@Getter
public enum DeviceTypeEnum {

  temp(1,"温度", true, "℃"),
  humi(2,"湿度", true, "%RH"),
  power(4,"电量", true, "V"),
  shine(8,"光照", true, "Lx"),
  pressure(16,"压力", true, "kPa"),
  smoke(32,"烟感", false, ""),
  water(64,"跑冒滴漏", false, ""),
  electric(128,"断电来电", false, ""),
  body(256,"人体感应", false, ""),
  out(512,"开关量输出", false, ""),
  ;

  final int id;
  final String name;
  final boolean isDataDevice;
  final String unit;


  private DeviceTypeEnum(int id, String name, boolean isDataDevice, String unit) {
    this.id = id;
    this.name = name;
    this.isDataDevice = isDataDevice;
    this.unit = unit;
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