package com.chengqianyun.eeweb2networkadmin.biz.enums;

import lombok.Getter;

/**
 * @author 聂鹏
 * @version 1.0
 * @date 18/4/18
 */
@Getter
public enum AlarmTypeEnum {

  alarm(1, "报警"),
  system_confirm(2, "系统确认"),
  user_confirm(3, "人工确认")
  ;

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