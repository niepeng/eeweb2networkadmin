package com.chengqianyun.eeweb2networkadmin.core.utils;


import com.chengqianyun.eeweb2networkadmin.biz.bean.export.HistoryListBean;
import java.lang.reflect.Method;

/**
 * @author 聂鹏
 * @version 1.0
 * @date 18/7/25
 */

public class ReflectUtil {

  public static String getStringValue(Object object, String fieldName) {
    Object value = getValue(object, fieldName);
    if (value instanceof String) {
      return (String) value;
    }
    return "";
  }


  public static Object getValue(Object object, String fieldName) {
    String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    Class tCls = object.getClass();
    try {
      Method getMethod = tCls.getMethod(getMethodName, new Class[]{});
      return getMethod.invoke(object, new Object[]{});
    } catch (Exception e) {
    }
    return null;
  }

  public static void main(String[] args) {
    HistoryListBean historyListBean = new HistoryListBean();
    historyListBean.setPower("12.12");
    System.out.println(getValue(historyListBean, "power"));
  }
}