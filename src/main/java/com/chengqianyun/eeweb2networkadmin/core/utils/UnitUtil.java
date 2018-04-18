package com.chengqianyun.eeweb2networkadmin.core.utils;

import com.chengqianyun.eeweb2networkadmin.biz.enums.DeviceTypeEnum;

/**
 * @author 聂鹏
 * @version 1.0
 * @date 18/4/14
 */

public class UnitUtil {

  public static String changeTemp(int value) {
    return chu100(value);
  }

  public static String changeHumi(int value) {
    return chu100(value);
  }

  public static String changePressure(int value) {
    return chu100(value);
  }

  public static String changePower(int value) {
    return chu100(value);
  }

  public static String chu100(int value) {
    return String.valueOf(ArithUtil.div(value, 100));
  }

  public static int changeTemp(String value) {
    return (int)ArithUtil.mul(StringUtil.str2Double(value), 100);
  }

  public static int changeHumi(String value) {
    return (int)ArithUtil.mul(StringUtil.str2Double(value), 100);
  }

  public static int changePressure(String value) {
    return (int)ArithUtil.mul(StringUtil.str2Double(value), 100);
  }

  public static int changePower(String value) {
    return (int)ArithUtil.mul(StringUtil.str2Double(value), 100);
  }


  public static String showValueAndUnit(int data, DeviceTypeEnum typeEnum) {
    return showValue(data, typeEnum) + (typeEnum != null ? typeEnum.getUnit() : "");
  }

  public static String showValue(int data, DeviceTypeEnum typeEnum) {
    if (typeEnum == DeviceTypeEnum.temp) {
      return changeTemp(data);
    }

    if (typeEnum == DeviceTypeEnum.humi) {
      return changeHumi(data);
    }

    if (typeEnum == DeviceTypeEnum.power) {
      return changePower(data);
    }

    if (typeEnum == DeviceTypeEnum.shine) {
      return String.valueOf(data);
    }

    if (typeEnum == DeviceTypeEnum.pressure) {
      return changePressure(data);
    }
    return "";
  }

  public static void main(String[] args) {
    String s = "12.39";
    System.out.println(changeTemp(s));
  }




}