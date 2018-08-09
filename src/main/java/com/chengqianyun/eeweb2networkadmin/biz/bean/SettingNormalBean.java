package com.chengqianyun.eeweb2networkadmin.biz.bean;

import lombok.Data;

/**
 * 基本设置:SettingEnum
 *
 * @author 聂鹏
 * @version 1.0
 * @date 18/8/9
 */
@Data
public class SettingNormalBean {

  private String platform_name;

  private String data_cycle_time;

  private String history_data_backup_path;
}