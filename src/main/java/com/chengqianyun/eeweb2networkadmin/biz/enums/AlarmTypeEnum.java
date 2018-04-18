package com.chengqianyun.eeweb2networkadmin.biz.enums;

import lombok.Getter;

/**
 * @author 聂鹏
 * @version 1.0
 * @date 18/4/18
 */
@Getter
public enum AlarmTypeEnum {

  main_alram(1, "传感器超限"),
  smoke(2, "烟感"),
  water(3, "跑冒滴漏"),
  electric(4, "停电来电"),
  body(5, "人体感应"),
  offline(6, "离线")
  ;

//  传感器超限:    温度偏高，当前温度30℃，请及时处理！
//  烟感:              烟感报警，请及时处理！
//  跑冒滴漏：     跑冒滴漏传感器报警，请及时处理！
//  停电来电：     现场停电，请及时处理！
//  人体感应：     有人入侵，请及时处理！       人体感应报警状态是很短的
//  开关机：         设备离线，请及时处理！

  final int id;
  final String meaning;

  private AlarmTypeEnum(int id, String meaning) {
    this.id = id;
    this.meaning = meaning;
  }

  public static AlarmTypeEnum find(int id) {
    for(AlarmTypeEnum tmp : AlarmTypeEnum.values()) {
      if(tmp.getId() == id) {
        return tmp;
      }
    }
    return null;
  }


}