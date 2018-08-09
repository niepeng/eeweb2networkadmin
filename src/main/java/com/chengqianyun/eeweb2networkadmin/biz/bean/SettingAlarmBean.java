package com.chengqianyun.eeweb2networkadmin.biz.bean;


import lombok.Data;
import lombok.ToString;

/**
 * 查看SettingEnum
 *
 * @author 聂鹏
 * @version 1.0
 * @date 18/7/12
 */
@Data
@ToString
public class SettingAlarmBean {

  private boolean alarm_sms;

  private String sms_cnmi_type;

  private String sms_center;

  private boolean alarm_song;

  private String alarm_song_config;

}