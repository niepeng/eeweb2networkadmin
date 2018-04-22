package com.chengqianyun.eeweb2networkadmin.core.utils;


/**
 * @author 聂鹏
 * @version 1.0
 * @date 18/4/18
 */

public interface BizConstant {

  /**
   * 最低电量值:3v
   */
  public static final int power_min = 300;

  /**
   * 最高电量值:3.7v
   */
  public static final int power_max = 370;


  interface Times {
    static final int second = 1000;
    static final int minute = second * 60;
    static final int hour = minute * 60;
    static final int day = hour * 24;
  }

}