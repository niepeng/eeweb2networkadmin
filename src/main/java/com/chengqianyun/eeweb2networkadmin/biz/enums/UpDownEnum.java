package com.chengqianyun.eeweb2networkadmin.biz.enums;


import lombok.Getter;

/**
 *
 * @author 聂鹏
 * @version 1.0
 * @date 18/4/17
 */
@Getter
public enum UpDownEnum {

  up(1, "偏高"),
  down(2, "偏低");

  final int id;
  final String meaning;

  private UpDownEnum(int id, String meaning) {
    this.id = id;
    this.meaning = meaning;
  }

  public static UpDownEnum find(int id) {
    for(UpDownEnum tmp : UpDownEnum.values()) {
      if(tmp.getId() == id) {
        return tmp;
      }
    }
    return null;
  }


}