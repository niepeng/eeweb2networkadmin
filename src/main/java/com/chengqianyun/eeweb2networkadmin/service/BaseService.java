package com.chengqianyun.eeweb2networkadmin.service;


import com.chengqianyun.eeweb2networkadmin.biz.entitys.Area;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.AreaMapper;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.ContactsMapper;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceAlarmMapper;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceDataHistoryMapper;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceDataIntimeMapper;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceInfoMapper;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.OutConditionMapper;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.SendContactsMapper;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.Setting;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.SettingMapper;
import com.chengqianyun.eeweb2networkadmin.biz.enums.SettingEnum;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author 聂鹏
 * @version 1.0
 * @date 18/3/13
 */
@Slf4j
public class BaseService {

  @Autowired
  protected AreaMapper areaMapper;

  @Autowired
  protected DeviceInfoMapper deviceInfoMapper;

  @Autowired
  protected OutConditionMapper outConditionMapper;

  @Autowired
  protected DeviceDataIntimeMapper dataIntimeMapper;

  @Autowired
  protected DeviceAlarmMapper deviceAlarmMapper;

  @Autowired
  protected DeviceDataHistoryMapper deviceDataHistoryMapper;

  @Autowired
  protected SettingMapper settingMapper;

  @Autowired
  protected ContactsMapper contactsMapper;

  @Autowired
  protected SendContactsMapper sendContactsMapper;

  public String getData(SettingEnum settingEnum) {
    Setting setting = settingMapper.selectByCode(settingEnum.getCode());
    if(setting == null) {
      return  settingEnum.getDefaultValue();
    }
    return setting.getParamValue();
  }


  public void saveData(SettingEnum settingEnum, String value) {
    Setting setting = settingMapper.selectByCode(settingEnum.getCode());
    if (setting == null) {
      setting = new Setting();
      setting.setParamCode(settingEnum.getCode());
      setting.setParamValue(value);
      settingMapper.insert(setting);
      return;
    }

    if (setting.getParamValue() != null && setting.getParamValue().equalsIgnoreCase(value)) {
      return;
    }
    setting.setParamValue(value);
    settingMapper.updateByPrimaryKey(setting);
  }


}