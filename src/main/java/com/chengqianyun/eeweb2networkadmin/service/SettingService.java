package com.chengqianyun.eeweb2networkadmin.service;

import com.chengqianyun.eeweb2networkadmin.biz.bean.SettingAlarmBean;
import com.chengqianyun.eeweb2networkadmin.biz.enums.SettingEnum;
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

  public SettingAlarmBean alarm() {
    SettingAlarmBean result = new SettingAlarmBean();
    result.setAlarm_song(Boolean.valueOf(getData(SettingEnum.alarm_song)));
    result.setAlarm_song_config(getData(SettingEnum.alarm_song_config));


    return result;
  }


  public void save(SettingAlarmBean bean) {
    saveData(SettingEnum.alarm_song, String.valueOf(Boolean.valueOf(bean.isAlarm_song())));
    saveData(SettingEnum.alarm_song_config, bean.getAlarm_song_config());
  }


}