package com.chengqianyun.eeweb2networkadmin.core.utils;


/**
 * @author 聂鹏
 * @version 1.0
 * @date 18/4/18
 */

public interface BizConstant {

  /**
   * token 有效期单位:天
   */
  public static final int token_days = 7;

  /**
   * 最低电量值:3v
   */
  public static final int power_min = 300;

  /**
   * 最高电量值:3.7v
   */
  public static final int power_max = 370;


  interface Times {
    static final long second = 1000;
    static final long minute = second * 60;
    static final long hour = minute * 60;
    static final long day = hour * 24;
  }

  interface ApiCode {
    int fail = 1;
    int empty = 2;
    int check_fail = 3;
    int token_expire = 4;

  }

}