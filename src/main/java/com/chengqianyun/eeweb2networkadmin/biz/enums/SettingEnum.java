package com.chengqianyun.eeweb2networkadmin.biz.enums;


import lombok.Getter;

/**
 * @author 聂鹏
 * @version 1.0
 * @date 18/4/12
 */
@Getter
public enum SettingEnum {

  system_invest("system_invest", "系统投资人"),
  recommend_subject("recommend_subject", "推荐标的");


  final String code;
  final String meaning;

  private SettingEnum(String code, String meaning) {
    this.code = code;
    this.meaning = meaning;
  }

  public static SettingEnum find(String code) {
    for(SettingEnum tmp : SettingEnum.values()) {
      if(tmp.getCode().equals(code)) {
        return tmp;
      }
    }
    return null;
  }


}