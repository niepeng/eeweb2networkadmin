package com.chengqianyun.eeweb2networkadmin.core.utils;

import java.math.BigDecimal;
import java.text.NumberFormat;

public class ArithUtil {

  // 默认除法运算精度
  private static final int DEF_DIV_SCALE = 2;

  /**
   *
   * setScale(1)表示保留一位小数，默认用四舍五入方式
   setScale(1,BigDecimal.ROUND_DOWN)直接删除多余的小数位，如2.35会变成2.3
   setScale(1,BigDecimal.ROUND_UP)进位处理，2.35变成2.4
   setScale(1,BigDecimal.ROUND_HALF_UP)四舍五入，2.35变成2.4
   setScaler(1,BigDecimal.ROUND_HALF_DOWN)四舍五入，2.35变成2.3，如果是5则向下舍
   setScaler(1,BigDecimal.ROUND_CEILING)接近正无穷大的舍入
   setScaler(1,BigDecimal.ROUND_FLOOR)接近负无穷大的舍入，数字>0和ROUND_UP作用一样，数字<0和ROUND_DOWN作用一样
   setScaler(1,BigDecimal.ROUND_HALF_EVEN)向最接近的数字舍入，如果与两个相邻数字的距离相等，则向相邻的偶数舍入。
   */
  public static final int DEFAULT_DOWN = BigDecimal.ROUND_DOWN;

  public ArithUtil() {
  }

  /**
   * 提供精确的加法运算。
   *
   * @param a
   *            被加数
   * @param b
   *            加数
   * @return 两个参数的和
   */
  public static double add(double a, double b) {
    BigDecimal b1 = new BigDecimal(Double.toString(a));
    BigDecimal b2 = new BigDecimal(Double.toString(b));
    return b1.add(b2).doubleValue();
  }

  /**
   * 提供精确的减法运算。
   *
   * @param a
   *            被减数
   * @param b
   *            减数
   * @return 两个参数的差
   */
  public static double sub(double a, double b) {
    BigDecimal b1 = new BigDecimal(Double.toString(a));
    BigDecimal b2 = new BigDecimal(Double.toString(b));
    return b1.subtract(b2).doubleValue();
  }

  /**
   * 提供精确的乘法运算。
   *
   * @param a
   *            被乘数
   * @param b
   *            乘数
   * @return 两个参数的积
   */
  public static double mul(double a, double b) {
    BigDecimal b1 = new BigDecimal(Double.toString(a));
    BigDecimal b2 = new BigDecimal(Double.toString(b));
    return b1.multiply(b2).doubleValue();
  }

  /**
   * 提供精确的乘法运算。
   *
   * @param a
   *            被乘数
   * @param b
   *            乘数
   * @return 两个参数的积
   */
  public static int mul(int a, int b) {
    BigDecimal b1 = new BigDecimal(Double.toString(a));
    BigDecimal b2 = new BigDecimal(Double.toString(b));
    return b1.multiply(b2).intValue();
  }

  /**
   * 提供（相对）精确的除法运算，当发生除不尽的情况时， 精确到小数点以后10位，以后的数字四舍五入。
   *
   * @param a
   *            被除数
   * @param b
   *            除数
   * @return 两个参数的商
   */
  public static double div(double a, double b) {
    return div(a, b, DEF_DIV_SCALE);
  }

  public static String div2Str(double d1, double d2, int scale) {
    Double x = div(d1, d2, scale);
    NumberFormat ddf1 = NumberFormat.getNumberInstance();
    ddf1.setMaximumFractionDigits(2);
    return ddf1.format(x);
  }

  /**
   * 提供（相对）精确的除法运算。 当发生除不尽的情况时，由scale参数指定精度
   *
   * @param a
   *            被除数
   * @param b
   *            除数
   * @param scale
   *            表示表示需要精确到小数点以后几位。
   * @return 两个参数的商
   */
  public static double div(double a, double b, int scale) {
    if (scale < 0) {
      throw new IllegalArgumentException("The scale must be a positive integer or zero");
    }
    BigDecimal b1 = new BigDecimal(Double.toString(a));
    BigDecimal b2 = new BigDecimal(Double.toString(b));
    return b1.divide(b2, scale, DEFAULT_DOWN).doubleValue();
  }


  /**
   * 提供精确的小数位四舍五入处理。
   *
   * @param v
   * @param scale
   *            小数点后保留几位
   */
  public static double round(double v, int scale) {
    if (scale < 0) {
      throw new IllegalArgumentException("The scale must be a positive integer or zero");
    }
    BigDecimal b = new BigDecimal(Double.toString(v));
    BigDecimal one = new BigDecimal("1");
    return b.divide(one, scale, DEFAULT_DOWN).doubleValue();
  }

}