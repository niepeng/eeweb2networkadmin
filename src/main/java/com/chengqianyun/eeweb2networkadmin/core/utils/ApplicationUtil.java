package com.chengqianyun.eeweb2networkadmin.core.utils;


import com.chengqianyun.eeweb2networkadmin.biz.enums.DeviceTypeEnum;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 聂鹏
 * @version 1.0
 * @date 18/3/13
 */

public class ApplicationUtil {

//  public static List<String> types(int deviceType) {
//    List<String> list = new ArrayList<String>();
//
//
//    return list;
//  }


  public static DeviceTypeEnum getOneById(int id) {
    return DeviceTypeEnum.getOneById(id);
  }

  public static List<DeviceTypeEnum> getAllDataDeviceTypes() {
    List<DeviceTypeEnum> list = new ArrayList<DeviceTypeEnum>();
    for(DeviceTypeEnum tmp : DeviceTypeEnum.values()) {
      if(tmp.isDataDevice()) {
        list.add(tmp);
      }
    }
    return list;
  }

  public boolean hasDeviceType(int value, int type) {
    return DeviceTypeEnum.hasType(value, DeviceTypeEnum.getOneById(type));
  }

  public static DeviceTypeEnum[] getAllDeviceTypes() {
    return DeviceTypeEnum.values();
  }


}