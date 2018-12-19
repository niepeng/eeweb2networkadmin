package com.chengqianyun.eeweb2networkadmin.core.utils;


import com.chengqianyun.eeweb2networkadmin.biz.enums.AlarmConfirmEnum;
import com.chengqianyun.eeweb2networkadmin.biz.enums.AlarmTypeEnum;
import com.chengqianyun.eeweb2networkadmin.biz.enums.DeviceTypeEnum;
import com.chengqianyun.eeweb2networkadmin.biz.enums.RoleEnum;
import com.chengqianyun.eeweb2networkadmin.biz.enums.StatusEnum;
import com.chengqianyun.eeweb2networkadmin.biz.enums.UpDownEnum;
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



  public static RoleEnum getRole(Long roleId) {
    RoleEnum roleEnum = RoleEnum.find(roleId.intValue());
    return roleEnum;
  }

  public static DeviceTypeEnum getOneById(int id) {
    return DeviceTypeEnum.getOneById(id);
  }

  public static List<DeviceTypeEnum> getAllDataDeviceTypes() {
    List<DeviceTypeEnum> list = new ArrayList<DeviceTypeEnum>();
    for(DeviceTypeEnum tmp : DeviceTypeEnum.values()) {
      if(tmp.isDataDevice() || tmp.isIn()) {
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

  public static StatusEnum[] getStatus() {
    return StatusEnum.values();
  }


  public static StatusEnum getStatus(int id) {
    return StatusEnum.find(id);
  }

  public static UpDownEnum getUpDownStatus(int id) {
    return UpDownEnum.find(id);
  }


  public static AlarmTypeEnum[] getAllAlarmTypes() {
    return AlarmTypeEnum.values();
  }

  public static AlarmConfirmEnum[] getAllConfirms() {
    return AlarmConfirmEnum.values();
  }

}