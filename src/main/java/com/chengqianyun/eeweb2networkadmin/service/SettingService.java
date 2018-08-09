package com.chengqianyun.eeweb2networkadmin.service;

import com.chengqianyun.eeweb2networkadmin.biz.bean.SettingAlarmBean;
import com.chengqianyun.eeweb2networkadmin.biz.bean.SettingNormalBean;
import com.chengqianyun.eeweb2networkadmin.biz.enums.SettingEnum;
import com.chengqianyun.eeweb2networkadmin.core.utils.PageUtilFactory;
import com.chengqianyun.eeweb2networkadmin.data.ServerConnectionManager;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author 聂鹏
 * @version 1.0
 * @date 18/7/12
 */
@Service
@Slf4j
public class SettingService extends BaseService {

  @PostConstruct
  public void init() {

    PageUtilFactory.platformName = getData(SettingEnum.platform_name);
    String times = getData(SettingEnum.data_cycle_time);
    try {
      ServerConnectionManager.GET_DATA_CYCLE = Integer.parseInt(times);
    } catch (Exception e) {
    }
  }

  public SettingAlarmBean alarm() {
    SettingAlarmBean result = new SettingAlarmBean();
    result.setAlarm_sms(Boolean.valueOf(getData(SettingEnum.alarm_sms)));
    result.setAlarm_song(Boolean.valueOf(getData(SettingEnum.alarm_song)));
    result.setAlarm_song_config(getData(SettingEnum.alarm_song_config));

    return result;
  }

  public void save(SettingAlarmBean bean) {
    saveData(SettingEnum.alarm_sms, String.valueOf(Boolean.valueOf(bean.isAlarm_sms())));
    saveData(SettingEnum.alarm_song, String.valueOf(Boolean.valueOf(bean.isAlarm_song())));
    saveData(SettingEnum.alarm_song_config, bean.getAlarm_song_config());
  }





  public SettingNormalBean normal() {
    SettingNormalBean bean = new SettingNormalBean();
    bean.setData_cycle_time(getData(SettingEnum.data_cycle_time));
    bean.setPlatform_name(getData(SettingEnum.platform_name));
    bean.setHistory_data_backup_path(getData(SettingEnum.history_data_backup_path));
    return bean;
  }

  public void save(SettingNormalBean bean) {
    saveData(SettingEnum.data_cycle_time, bean.getData_cycle_time());
    saveData(SettingEnum.platform_name, bean.getPlatform_name());
    saveData(SettingEnum.history_data_backup_path, bean.getHistory_data_backup_path());


    PageUtilFactory.platformName = bean.getPlatform_name();
    try {
      int tmp = Integer.parseInt(bean.getData_cycle_time());
      if(ServerConnectionManager.GET_DATA_CYCLE != tmp && tmp >= 10) {
        ServerConnectionManager.GET_DATA_CYCLE = tmp;
      }
    } catch(Exception e) {

    }


  }





}