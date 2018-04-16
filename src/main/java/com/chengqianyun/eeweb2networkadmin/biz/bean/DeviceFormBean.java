package com.chengqianyun.eeweb2networkadmin.biz.bean;


import com.chengqianyun.eeweb2networkadmin.core.utils.StringUtil;
import lombok.Data;
import lombok.ToString;

/**
 * 设备表单bean
 *
 * @author 聂鹏
 * @version 1.0
 * @date 18/4/16
 */
@Data
@ToString
public class DeviceFormBean {


  /**
   * 所属区域
   */
  private long areaId;

  /**
   * sn号
   */
  private String sn;

  /**
   * 名称
   */
  private String name;

  /**
   * 标签
   */
  private String tag;

  /**
   * 设备地址
   */
  private int address;

  /**
   * 表单中多个设备类型的值
   */
  private String deviceTypes;

  /**
   * 温度上限:23.34,存储2334
   */
  private String tempUpStr;

  /**
   * 温度下限:23.34,存储2334
   */
  private String tempDownStr;

  /**
   * 温度校正值:23.34,存储2334
   */
  private String tempDevStr;

  /**
   * 湿度上限:45.67,存储4567
   */
  private String humiUpStr;

  /**
   * 湿度下限:45.67,存储4567
   */
  private String humiDownStr;

  /**
   * 湿度校正值:45.67,存储4567
   */
  private String humiDevStr;

  /**
   * 光照上限
   */
  private String shineUpStr;

  /**
   * 光照下限
   */
  private String shineDownStr;

  /**
   * 光照校正值
   */
  private String shineDevStr;

  /**
   * 压力上限
   */
  private String pressureUpStr;

  /**
   * 压力下限
   */
  private String pressureDownStr;

  /**
   * 压力校正值
   */
  private String pressureDevStr;

  /**
   * 只有开关量输出才有值:控制通道
   */
  private String controlWay;

  /**
   * 只有开关量输入才有值: 联动地址
   */
  private String relationOutId;

  /**
   * 只有开关量输入才有值:联动开关通道
   */
  private String opencloseWay;

  /**
   * 只有开关量输入才有值:输入通道
   */
  private String inWay;

  // ================= 扩展属性 ======================


  // ================= 扩展方法 ======================

  public int calcDeviceType() {
    String[] types = deviceTypes.split(",");
    int result = 0;
    for (String s : types) {
      result += StringUtil.str2int(s);
    }
    return result;
  }

}