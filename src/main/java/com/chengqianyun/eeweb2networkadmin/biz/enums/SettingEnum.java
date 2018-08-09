package com.chengqianyun.eeweb2networkadmin.biz.enums;


import lombok.Getter;

/**
 * @author 聂鹏
 * @version 1.0
 * @date 18/4/12
 */
@Getter
public enum SettingEnum {

  alarm_sms("alarm_sms", "短信报警:true启用,false:禁用", "false"),
  sms_cnmi_type("sms_cnmi_type", "短信CNMI类型", "AT+CNMI=2,2"),
  sms_center("sms_center", "短信中心号码", "13800571500"),

  alarm_song("alarm_song", "声音报警:true启用,false:禁用", "false"),
  alarm_song_config("alarm_song_config", "声音报警歌曲", "cqAlarm1.mp3"),

  platform_name("platform_name", "平台名称", "管理系统"),
  data_cycle_time("data_cycle_time", "数据更新周期:单位秒", "10"),
  history_data_backup_path("history_data_backup_path", "历史数据备份路径", "d:/eefield/backup"),
  ;


  final String code;
  final String meaning;
  final String defaultValue;

  private SettingEnum(String code, String meaning, String object) {
    this.code = code;
    this.meaning = meaning;
    this.defaultValue = object;
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