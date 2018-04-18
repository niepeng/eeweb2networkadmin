package com.chengqianyun.eeweb2networkadmin.biz.bean;


import com.chengqianyun.eeweb2networkadmin.biz.entitys.Area;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceInfo;
import com.chengqianyun.eeweb2networkadmin.biz.enums.UpDownEnum;
import java.util.Date;
import lombok.Data;
import lombok.ToString;

/**
 * 单个框的元素
 *
 * @author 聂鹏
 * @version 1.0
 * @date 18/4/17
 */
@Data
@ToString
public class ElementDataBean {

  private DeviceInfo deviceInfo;

  private Area area;

  private String data;

  /**
  * StatusEnum
  */
  private int status;

  private Date time;

  private int deviceOneType;

  private String unit;

  /**
   * 默认为0, UpDownEnum
   */
  private int tempStatus;

  /**
   * 默认为0, UpDownEnum
   */
  private int humiStatus;

  /**
   * 默认为0, UpDownEnum
   */
  private int powerStatus;

  /**
   * 默认为0, UpDownEnum
   */
  private int shineStatus;

  /**
   * 默认为0, UpDownEnum
   */
  private int pressureStatus;

  // =============== 扩展方法 =====================

  public boolean isUp() {
    return tempStatus + humiStatus + powerStatus + shineStatus + pressureStatus == UpDownEnum.up.getId();
  }

  public boolean isDown() {
    return tempStatus + humiStatus + powerStatus + shineStatus + pressureStatus == UpDownEnum.down.getId();
  }


}