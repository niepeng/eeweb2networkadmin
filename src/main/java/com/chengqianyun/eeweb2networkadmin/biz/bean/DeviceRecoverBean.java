package com.chengqianyun.eeweb2networkadmin.biz.bean;


import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceAlarm;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceInfo;
import com.chengqianyun.eeweb2networkadmin.biz.enums.DeviceTypeEnum;
import java.util.Date;
import lombok.Data;

/**
 * 设备恢复报警信息
 *
 * @author 聂鹏
 * @version 1.0
 * @date 18/8/23
 */
@Data
public class DeviceRecoverBean {

  private DeviceInfo deviceInfo;

  /**
   * 是否是设备所有传感器恢复:true 是, false不是
   * 2018-09-05  改造后,已经没有所有恢复的概念,每一条报警记录,恢复后都需要发送短信
   */
  private boolean isAll;

  private Date time;

  private DeviceTypeEnum deviceTypeEnum;

  private DeviceAlarm deviceAlarm;

}