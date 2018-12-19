package com.chengqianyun.eeweb2networkadmin.biz.enums;


import lombok.Getter;

/**
 * @author 聂鹏
 * @version 1.0
 * @date 18/4/12
 */
@Getter
public enum DeviceTypeEnum {

  temp(1,"温度", true, false,"℃"),
  humi(2,"湿度", true, false, "%RH"),
  power(4,"电量", true, false, "V"),
  shine(8,"光照", true, false, "Lx"),
  pressure(16,"压力", true, false, "kPa"),
  smoke(32,"烟感", false, true, ""),
  water(64,"跑冒滴漏", false, true, ""),
  electric(128,"断电来电", false, true, ""),
  body(256,"人体感应", false, true, ""),
  out(512,"开关量输出", false, false, ""),
  ;

  final int id;
  final String name;
  final boolean isDataDevice;
  final boolean isIn;
  final String unit;


  private DeviceTypeEnum(int id, String name, boolean isDataDevice, boolean isIn,String unit) {
    this.id = id;
    this.name = name;
    this.isDataDevice = isDataDevice;
    this.isIn = isIn;
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

  public static boolean hasEnv(int paramValue) {
    return hasType(paramValue, temp) || hasType(paramValue, humi) || hasType(paramValue, power) || hasType(paramValue, shine) || hasType(paramValue, pressure);
  }

  public static boolean hasIn(int paramValue) {
    return hasType(paramValue, smoke) || hasType(paramValue, water) || hasType(paramValue, electric) || hasType(paramValue, body);
  }

  public static boolean hasOut(int paramValue) {
    return hasType(paramValue, out);
  }

  public static boolean hasType(int idValue, DeviceTypeEnum deviceTypeEnum) {
    if(deviceTypeEnum == null) {
      return false;
    }
    return (deviceTypeEnum.getId() & idValue) == deviceTypeEnum.getId();
  }

}