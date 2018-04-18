package com.chengqianyun.eeweb2networkadmin.biz.bean;


import com.chengqianyun.eeweb2networkadmin.biz.entitys.Area;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceInfo;
import com.chengqianyun.eeweb2networkadmin.biz.enums.DeviceTypeEnum;
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

  private DeviceTypeEnum deviceOneTypeEnum;

  // =============== 扩展方法 =====================


}