package com.chengqianyun.eeweb2networkadmin.core.utils;


import com.chengqianyun.eeweb2networkadmin.biz.enums.SettingEnum;

/**
 * @author 聂鹏
 * @version 1.0
 * @date 18/3/13
 */

public class PageUtilFactory {

  public static DateUtil dateUtil = new DateUtil();

  public static ApplicationUtil applicationUtil = new ApplicationUtil();

  public static StringUtil stringUtil = new StringUtil();

  public static UnitUtil unitUtil = new UnitUtil();

  public static String platformName = SettingEnum.platform_name.getDefaultValue();

}