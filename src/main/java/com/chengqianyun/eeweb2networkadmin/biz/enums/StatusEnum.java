package com.chengqianyun.eeweb2networkadmin.biz.enums;


import lombok.Getter;

/**
 * @author 聂鹏
 * @version 1.0
 * @date 18/4/17
 */

@Getter
public enum StatusEnum {

  normal(1, "正常"),
  alarm(2, "报警"),
  offline(3, "离线"),


  alarm_down(21, "偏低"),
  alarm_up(22,"偏高")
  ;


  final int id;
  final String meaning;

  private StatusEnum(int id, String meaning) {
    this.id = id;
    this.meaning = meaning;
  }

  public static StatusEnum find(int id) {
    for(StatusEnum tmp : StatusEnum.values()) {
      if(tmp.getId() == id) {
        return tmp;
      }
    }
    return null;
  }


}