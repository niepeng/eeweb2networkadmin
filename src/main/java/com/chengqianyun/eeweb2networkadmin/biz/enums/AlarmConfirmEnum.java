package com.chengqianyun.eeweb2networkadmin.biz.enums;


import lombok.Getter;

/**
 * 报警是否确认
 *
 * @author 聂鹏
 * @version 1.0
 * @date 18/4/18
 */
@Getter
public enum AlarmConfirmEnum {

  no_confirm(0, "未确认"),
  confirm(1, "已确认"); // 用户主动发起的行为

  final int id;
  final String meaning;

  private AlarmConfirmEnum(int id, String meaning) {
    this.id = id;
    this.meaning = meaning;
  }

  public static AlarmConfirmEnum find(int id) {
    for(AlarmConfirmEnum tmp : AlarmConfirmEnum.values()) {
      if(tmp.getId() == id) {
        return tmp;
      }
    }
    return null;
  }


}