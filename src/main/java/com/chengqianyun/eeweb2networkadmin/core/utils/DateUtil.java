package com.chengqianyun.eeweb2networkadmin.core.utils;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

  private static final SimpleDateFormat fromat = new SimpleDateFormat("yyyyMMddHHmmss");

  public static String getCurrentTimeStamp() {
    return fromat.format(new Date());
  }

  public static String datePattern = "yyyy-MM-dd";
  public static String timePattern = "HH:mm:ss";
  public static String dateFullPattern =  datePattern + " " + timePattern;
  public static String dateFullPatternNoSecond =  datePattern + " " + "HH:mm";
  public static String PATTERN_YYYYMMDDANDHHMMSS =  datePattern + "HHmmss";



  public static Date formatCurrentMin(Date date) {
    if (date == null) {
      return null;
    }
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    c.set(Calendar.HOUR_OF_DAY, 0);
    c.set(Calendar.MINUTE, 0);
    c.set(Calendar.SECOND, 0);
    c.set(Calendar.MILLISECOND, 0);
    return c.getTime();
  }

  public static Date formatCurrentMax(Date date) {
    if (date == null) {
      return null;
    }
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    c.set(Calendar.HOUR_OF_DAY, 23);
    c.set(Calendar.MINUTE, 59);
    c.set(Calendar.SECOND, 0);
    c.set(Calendar.MILLISECOND, 0);
    return c.getTime();
  }


  /**
   * 根据默认日期格式，返回日期字符串。
   *
   * @param aDate 日期对象
   * @return String '年月日'日期字符串
   */
  public static final String getDate(Date aDate) {
    SimpleDateFormat df = null;
    String returnValue = "";
    if (aDate != null) {
      df = new SimpleDateFormat(datePattern);
      returnValue = df.format(aDate);
    }
    return (returnValue);
  }

  /**
   * 日期去掉trim
   * @param date
   * @return
   */
  public static Date trimTime(Date date) {
    String dateStr = getDate(date, datePattern);
    return getDate(dateStr, datePattern);
  }

  /**
   * 根据传入日期返回指定日期的日期字符串
   *
   * @param date 日期对象
   * @return String 格式化后的'年月日时分秒'日期字符串
   */
  public static String getDateTime(Date date) {
    SimpleDateFormat df = new SimpleDateFormat(datePattern);
    if (date == null) {
      return "";
    }
    df.applyPattern(dateFullPattern);
    return df.format(date);
  }

  /**
   * 根据传入日期返回指定日期的日期字符串
   *
   * @param date 日期对象
   * @return String 格式化后的'年月日时分秒'日期字符串
   */
  public static String getDateTimeNoSecond(Date date) {
    SimpleDateFormat df = new SimpleDateFormat(dateFullPatternNoSecond);
    if (date == null) {
      return "";
    }
    df.applyPattern(dateFullPatternNoSecond);
    return df.format(date);
  }

  /**
   * 返回当前日期 指定格式化格式的 日期字符串
   *
   * @param pattern 格式化字符串
   * @return String 日期字符串
   */
  public static final String getDate(String pattern) {
    Date date = new Date();
    return getDate(date, pattern);
  }

  /**
   * 把传入的日期对象，转换成指定格式的日期字符串
   *
   * @param date 日期对象
   * @param pattern 指定转换格式
   * @return String 格式化后的日期字符串
   */
  public static final String getDate(Date date, String pattern) {
    SimpleDateFormat df = null;
    String returnValue = "";
    if (date != null) {
      df = new SimpleDateFormat(pattern);
      returnValue = df.format(date);
    }
    return (returnValue);
  }

  /**
   * 把传入的日期字符串，转换成指定格式的日期对象
   *
   * @param dateString 日期字符串
   * @param pattern 指定转换格式
   * @return Date  日期对象
   */
  public static Date getDate(String dateString, String pattern) {
    SimpleDateFormat df = null;
    Date date = null;
    if (dateString != null) {
      try {
        df = new SimpleDateFormat(pattern);
        date = df.parse(dateString);
      } catch (Exception e) {
      }
    }
    return date;
  }

  public static boolean isSameDay(Date date1, Date date2) {
    return isSameDay(toCalendar(date1), toCalendar(date2));
  }

  public static Calendar toCalendar(Date date) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    return cal;
  }


  public static boolean isSameDay(Calendar cal1, Calendar cal2) {
    int year1 = cal1.get(Calendar.YEAR);
    int day1 = cal1.get(Calendar.DAY_OF_YEAR);

    int year2 = cal2.get(Calendar.YEAR);
    int day2 = cal2.get(Calendar.DAY_OF_YEAR);

    if (year1 != year2) {
      return false;
    }

    if (day1 != day2) {
      return false;
    }
    return true;
  }



  /**
   * 获取传入日期的对象
   *
   * @param date 传入的日期
   * @return String  日期年月字符串
   */
  public static String getMonth(Date date) {
    SimpleDateFormat df = null;
    if (date != null) {
      df = new SimpleDateFormat();
      df.applyPattern("yyyy-MM");
      return df.format(date);
    }
    return null;
  }

  /**
   * 获取传入日期的时分秒
   *
   * @param date 传入的日期
   * @return String 时分秒字符串
   */
  public static String getTime(Date date) {
    SimpleDateFormat df = null;
    if (date != null) {
      df = new SimpleDateFormat();
      df.applyPattern(timePattern);
      return df.format(date);
    }
    return null;
  }


  /**
   * 获取当前年
   *
   * @return String 日期年字符串
   */
  public static String getYear() {
    Date date = new Date();
    return getDate(date, "yyyy");
  }

  /**
   * 获取当前月
   *
   * @return String 日期月字符串
   */
  public static String getMonth() {
    Date date = new Date();
    return getDate(date, "MM");
  }

  /**
   * 获取当前日
   *
   * @return String 日期日字符串
   */
  public static String getDay() {
    Date date = new Date();
    return getDate(date, "dd");
  }

  /**
   * 获取当前时间的小时
   *
   * @return 返回小时
   */
  public static int getHour() {
    Date date = new Date();
    java.util.Calendar c = java.util.Calendar.getInstance();
    c.setTime(date);
    return c.get(java.util.Calendar.HOUR_OF_DAY);
  }

  /**
   * 获取当前时间的分钟
   *
   * @return 返回分钟
   */
  public static int getMinute() {
    Date date = new Date();
    java.util.Calendar c = java.util.Calendar.getInstance();
    c.setTime(date);
    return c.get(java.util.Calendar.MINUTE);
  }

  /**
   * 获取当前时间的秒钟
   *
   * @return 返回秒钟
   */
  public static int getSecond() {
    Date date = new Date();
    java.util.Calendar c = java.util.Calendar.getInstance();
    c.setTime(date);
    return c.get(java.util.Calendar.SECOND);
  }

  /**
   * 获取当前时间的毫秒
   *
   * @return 返回毫秒
   */
  public static long getMillis() {
    Date date = new Date();
    java.util.Calendar c = java.util.Calendar.getInstance();
    c.setTime(date);
    return c.getTimeInMillis();
  }

  /**
   * 返回传入日期的小时
   *
   * @param date 日期
   * @return 返回小时
   */
  public static int getHour(java.util.Date date) {
    java.util.Calendar c = java.util.Calendar.getInstance();
    c.setTime(date);
    return c.get(java.util.Calendar.HOUR_OF_DAY);
  }

  /**
   * 返回分钟
   *
   * @param date 日期
   * @return 返回分钟
   */
  public static int getMinute(java.util.Date date) {
    java.util.Calendar c = java.util.Calendar.getInstance();
    c.setTime(date);
    return c.get(java.util.Calendar.MINUTE);
  }

  /**
   * 返回秒钟
   *
   * @param date 日期
   * @return 返回秒钟
   */
  public static int getSecond(java.util.Date date) {
    java.util.Calendar c = java.util.Calendar.getInstance();
    c.setTime(date);
    return c.get(java.util.Calendar.SECOND);
  }

  /**
   * 返回毫秒
   *
   * @param date 日期
   * @return 返回毫秒
   */
  public static long getMillis(java.util.Date date) {
    java.util.Calendar c = java.util.Calendar.getInstance();
    c.setTime(date);
    return c.getTimeInMillis();
  }

  /**
   * 日期相加
   *
   * @param date 日期
   * @param day 天数，可为负数
   * @return 返回相加后的日期
   */
  public static java.util.Date addDate(java.util.Date date, int day) {
    java.util.Calendar c = java.util.Calendar.getInstance();
    c.setTimeInMillis(getMillis(date) + ((long) day) * 24 * 3600 * 1000);
    return c.getTime();
  }

  public static Date addMonth(Date date, int months) {
    if (date == null) {
      return null;
    }
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.add(Calendar.MONTH, months);
    return cal.getTime();
  }

  public static Date addHour(Date date, int hours) {
    if (date == null) {
      return null;
    }
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.add(Calendar.HOUR, hours);
    return cal.getTime();
  }


  public static Date addMinitue(Date date, int minitue) {
    java.util.Calendar c = java.util.Calendar.getInstance();
    c.setTimeInMillis(getMillis(date) + minitue * 60 * 1000);
    return c.getTime();
  }

  public static Date addSecond(Date date, int second) {
    java.util.Calendar c = java.util.Calendar.getInstance();
    c.setTimeInMillis(getMillis(date) + second * 1000);
    return c.getTime();
  }

  public static Date min(Date d1, Date d2) {
    return d1.before(d2) ? d1 : d2;
  }



  /**
   * 日期相减
   *
   * @param date 日期
   * @param date1 日期
   * @return 返回相减后的日期
   */
  public static int diffDate(java.util.Date date, java.util.Date date1) {
    return (int) ((getMillis(date) - getMillis(date1)) / (24 * 3600 * 1000));
  }

  /**
   * 小时相减
   *
   * @param date 日期
   * @param date1 日期
   * @return int 返回相减后的小时
   */
  public static int diffDateToHour(Date date, Date date1) {
    return (int) ((getMillis(date) - getMillis(date1)) / (1000 * 60 * 60));
  }

  /**
   * 秒相减
   *
   * @param date 日期
   * @param date1 日期
   * @return int 返回相减后的秒
   */
  public static int diffDateToMintue(Date date, Date date1) {
    return (int) ((getMillis(date) - getMillis(date1)) / (1000 * 60));
  }

  /**
   * 毫秒相减
   *
   * @param date 日期
   * @param date1 日期
   * @return long 返回相减后的毫秒
   */
  public static long diffDateToMillis(Date date, Date date1) {
    return (long) ((getMillis(date) - getMillis(date1)));
  }

  /**
   * 判断是否在一个时间段内
   *
   * @param time 要判断的时间
   * @param begin 开始时间
   * @param end 结束时间
   * @return boolean true：在开始和结束范围内，false:不在开始和结束范围内
   */
  public static boolean IsTimeIn(Date time, Date begin, Date end) {
    return time.getTime() >= begin.getTime() && time.getTime() <= end.getTime();
  }

  /**
   * 判断输入的时间是否今年
   *
   * @param date 日期字符串
   * @return boolean  true：今年，false：不是今年
   */
  public static boolean isThisYear(String date) {
    int timeYear = Integer.parseInt(date.substring(0, 4));
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(new Date());
    int thisYear = calendar.get(Calendar.YEAR);
    if (timeYear == thisYear) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * 获取当前时间的上个月
   *
   * @return String 日期格式yyyyMM格式的日期字符串
   */
  public static String getPreMonth() {
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.DATE, -1);    //得到前一天
    calendar.add(Calendar.MONTH, -1);    //得到前一个月
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH) + 1;

    String data = "" + year;
    if (month < 10) {
      data += "0" + month;
    } else {
      data += "" + month;
    }

    return data;
  }

  /**
   * 计算两日期之间相差的天数 day1 -day2
   *
   * @param day1 日期字符串
   * @param day2 日期字符串
   * @throws int 相减后的天数
   */
  public static int countDays(String day1, String day2)
      throws ParseException {

    if (day1 != null && day2 != null && day1.length() > 0
        && day2.length() > 0) {
      // 日期相减算出秒的算法
      Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(day1);
      Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse(day2);
      // 日期相减得到相差的日期
      long day = (date1.getTime() - date2.getTime())
          / (24 * 60 * 60 * 1000);
      return (int) day;
    } else {
      return 0;
    }

  }


  /**
   * 判断当前日期是星期几
   *
   * @param dateTimeStr 要判断的时间
   * @return int 当前是星期几
   */
  public static int dayForWeek(String dateTimeStr) throws Exception {
    SimpleDateFormat df = new SimpleDateFormat();
    df.applyPattern("yyyy-MM-dd");
    Calendar c = Calendar.getInstance();
    c.setTime(df.parse(dateTimeStr));
    int dayForWeek = 0;
    if (c.get(Calendar.DAY_OF_WEEK) == 1) {
      dayForWeek = 7;
    } else {
      dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
    }
    return dayForWeek;
  }


  /**
   * 获取某日期所在周的星期天日期(以周一为一周的第一天)
   *
   * @param dateTimeStr 日期字符串
   * @return String 返回日期
   */
  public static String getDataForSunday(String dateTimeStr)
      throws Exception {
    SimpleDateFormat df = new SimpleDateFormat();
    df.applyPattern("yyyy-MM-dd");
    Calendar c = Calendar.getInstance();
    c.setTime(df.parse(dateTimeStr));
    c.setFirstDayOfWeek(Calendar.MONDAY);
    c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6);
    return df.format(c.getTime());
  }

  /**
   * 获取某日期是一年中的第几周
   *
   * @param dateTimeStr 日期字符串
   * @return int 第几周
   */
  public static int getWeekOfYear(String dateTimeStr) throws Exception {
    SimpleDateFormat df = new SimpleDateFormat();
    df.applyPattern("yyyy-MM-dd");
    Calendar c = Calendar.getInstance();
    c.setTime(df.parse(dateTimeStr));
    c.setFirstDayOfWeek(Calendar.MONDAY);
    return c.get(Calendar.WEEK_OF_YEAR);
  }


  /**
   * 时间前推或后推分钟,其中minute表示分钟. +后推 -前推
   *
   * @param date 日期字符串，格式年月日时分秒
   * @param minute 分钟
   * @return String
   */
  public static String getPreTime(String date, Integer minute) {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String mydate1 = "";
    try {
      Date date1 = format.parse(date);
      long Time = (date1.getTime() / 1000) + minute * 60;
      date1.setTime(Time * 1000);
      mydate1 = format.format(date1);
    } catch (Exception e) {
    }
    return mydate1;
  }

  /**
   * 判断时间date1是否在时间date2之前
   *
   * @param date1 时间字符串
   * @param date2 时间字符串
   * @return boolean true:是，false：否
   */
  public static boolean isDateBefore(String date1, String date2) {
    try {
      DateFormat df = DateFormat.getDateTimeInstance();
      return df.parse(date1).before(df.parse(date2));
    } catch (ParseException e) {
      return false;
    }
  }


  /**
   * 判断是否是月末
   *
   * @param date 日期
   * @return boolean true月末,false不是月末
   */
  public static boolean isYueMo(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    if (calendar.get(Calendar.DATE) == calendar.getActualMaximum(Calendar.DAY_OF_MONTH)) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * 判断是否当月
   *
   * @param dateStr 日期
   * @return boolean 是返回true，否返回false
   */
  public static boolean isDangYue(String dateStr) {
    String str = dateStr.substring(5, 7);
    Calendar calendar = Calendar.getInstance();
    String cm = "";
    if (calendar.get(Calendar.MONTH) + 1 <= 9) {
      cm = "0" + (calendar.get(Calendar.MONTH) + 1);
    } else {
      cm = (calendar.get(Calendar.MONTH) + 1) + "";
    }
    if (cm.equals(str)) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * 获得当前日期的前N天或后N天
   *
   * @param oper day 整数是前推N天（即过去某一天），负数是向后推N天（即将来某一天）
   * @return String 相加减后的日期字符串
   */
  public static String getSpecifiedDayBeforeOrAfter(int oper) {
    Calendar c = Calendar.getInstance();
    Date date = new Date();
    c.setTime(date);
    int day = c.get(Calendar.DATE);
    c.set(Calendar.DATE, day - oper);

    String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(c
        .getTime());
    return dayBefore;
  }

  /**
   * 取得当月天数
   *
   * @return int 当月天数
   */
  public static int getCurrentMonthDays() {
    Calendar cal = Calendar.getInstance();
    cal.set(Calendar.DATE, 1);//把日期设置为当月第一天
    cal.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天
    int maxDate = cal.get(Calendar.DATE);
    return maxDate;
  }

  /**
   * 取得本日是本月的第几天
   *
   * @return int 当前月的第几天
   */
  public static int getCurrentDayOfMonth() {
    Calendar cal = Calendar.getInstance();
    int num = cal.get(Calendar.DAY_OF_MONTH);
    return num;
  }

  public static int compareDateTime(String t1, String t2)
      throws ParseException {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Calendar c1 = Calendar.getInstance();
    Calendar c2 = Calendar.getInstance();
    c1.setTime(sdf.parse(t1));
    c2.setTime(sdf.parse(t2));
    Integer result = c1.compareTo(c2);
    return result;

  }

  /**
   * 日期相减
   */
  public static int getMonthSpace(Date date1, Date date2)
      throws ParseException {

    Calendar cal = Calendar.getInstance();

    cal.setTime(date1);
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);
    long time1 = cal.getTimeInMillis();
    cal.setTime(date2);
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);
    long time2 = cal.getTimeInMillis();
    long between_days = (time1 - time2) / (1000 * 3600 * 24);
    return Integer.parseInt(String.valueOf(between_days));
  }

  /**
   * 获取当前年月（格式：yyyyMM）
   */
  public static String getCurrentYearMonth() {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
    Date date = new Date();
    String now = sdf.format(date);
    return now;
  }

  /**
   * 获取当前时间(格式：yyyyMMddHHmmss)
   */
  public static String getCurrentTime() {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    Date date = new Date();
    String now = sdf.format(date);
    return now;
  }


  /**
   * 比较两个日期时间的大小
   *
   * @param t1 格式：yyyy-MM-dd HH:mm:ss
   * @param t2 格式：yyyy-MM-dd HH:mm:ss
   */
  public static Integer compareDateTime(String t1, String t2, String format)
      throws ParseException {
    SimpleDateFormat sdf = new SimpleDateFormat(format);
    Calendar c1 = Calendar.getInstance();
    Calendar c2 = Calendar.getInstance();
    c1.setTime(sdf.parse(t1));
    c2.setTime(sdf.parse(t2));
    Integer result = c1.compareTo(c2);
    return result;
  }

  // public static void main(String[] args){
  // String sbStr = "7.8.6.";
  // sbStr = sbStr.substring(0, sbStr.length()-1);
  // System.err.println(sbStr);
  //
  // }


  /**
   * 计算两个日期之间相差的天数
   *
   * @param smdate 较小的时间
   * @param bdate 较大的时间
   * @return 相差天数
   */
  public static int daysBetween(Date smdate, Date bdate) throws ParseException {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    smdate = sdf.parse(sdf.format(smdate));
    bdate = sdf.parse(sdf.format(bdate));
    Calendar cal = Calendar.getInstance();
    cal.setTime(smdate);
    long time1 = cal.getTimeInMillis();
    cal.setTime(bdate);
    long time2 = cal.getTimeInMillis();
    long between_days = (time2 - time1) / (1000 * 3600 * 24);

    return Integer.parseInt(String.valueOf(between_days));
  }


  public static long getCurrentDateMilliSeconds() {
    return getCurrentDate().getTime();
  }

  public static Date getCurrentDate() {
    return new Date();
  }

  public static void main(String[] args) throws UnsupportedEncodingException {
    Date date = date = DateUtil.getDate(DateUtil.getDate(DateUtil.getCurrentDate()), "yyyy-MM-dd");
//    System.out.println(date);
    System.out.println(DateUtil.addDate(DateUtil.getDate("2017-09-11 13:16:18", "yyyy-MM-dd"),6));

    System.out.println(DateUtil.addDate(DateUtil.getCurrentDate(),1));
    System.out.println(DateUtil.diffDate(DateUtil.addDate(DateUtil.getCurrentDate(),1),DateUtil.getCurrentDate()));

//    System.out.println(URLEncoder.encode("2017-08-09 00:00:00", "utf8"));
    System.out.println(getDate(getDate(datePattern),datePattern));
  }
}