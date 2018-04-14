package com.chengqianyun.eeweb2networkadmin.core.utils;

/**
 * @author 聂鹏
 * @version 1.0
 * @date 18/4/14
 */

public class UnitUtil {

  public String changeTemp(int value) {
    return chu100(value);
  }

  public String changeHumi(int value) {
    return chu100(value);
  }

  public String changePressure(int value) {
    return chu100(value);
  }

  public String changePower(int value) {
    return chu100(value);
  }

  public String chu100(int value) {
    return ArithUtil.div2Str(value, 100, 2);
  }



}