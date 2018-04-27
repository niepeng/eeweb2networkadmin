package com.chengqianyun.eeweb2networkadmin.core.utils;

/**
 * @author 聂鹏
 * @version 1.0
 * @date 18/2/10
 */

public class RandomUtil {

  public static long random(long min, long max) {
    return (long) (Math.random() * (max - min)) + min;
  }
}