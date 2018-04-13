package com.chengqianyun.eeweb2networkadmin.biz.enums;


import lombok.Getter;

/**
 * @author 聂鹏
 * @version 1.0
 * @date 18/4/12
 */
@Getter
public enum MenuEnum {

  intime("intime", 1, "实时管理"),
  alarm("alarm", 1, "报警记录"),
  history("history", 1, "历史管理"),
  device("device", 1, "设备管理"),
  setting("setting", 1, "系统设置");

  private final String name;

  private final int level;

  private final String meaning;


  private MenuEnum(String name, int level, String meaning) {
    this.name = name;
    this.level = level;
    this.meaning = meaning;
  }



}