package com.chengqianyun.eeweb2networkadmin.service;


import com.chengqianyun.eeweb2networkadmin.biz.entitys.Area;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.AreaMapper;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.ConsoleLoginAccountMapper;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.Contacts;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.ContactsMapper;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceAlarmMapper;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceDataHistoryMapper;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceDataIntime;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceDataIntimeMapper;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceInfoMapper;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.OutConditionMapper;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.SendContactsMapper;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.Setting;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.SettingMapper;
import com.chengqianyun.eeweb2networkadmin.biz.enums.SettingEnum;
import com.chengqianyun.eeweb2networkadmin.core.utils.StringUtil;
import java.util.ArrayList;
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
  protected DeviceDataIntimeMapper deviceDataIntimeMapper;

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

  @Autowired
  protected ConsoleLoginAccountMapper consoleLoginAccountMapper;

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

  /**
   * 获取所有需要发送的号码
   *
   * @param areaId
   * @return
   */
  public List<Contacts> getContactsByAreaId(long areaId) {
    Area area = areaMapper.selectByPrimaryKey(areaId);
    if (area == null || StringUtil.isEmpty(area.getContactsIds())) {
      return null;
    }
    return findContactsList(area.getContactsIds());
  }


  private List<Contacts> findContactsList(String contactsIds) {
    List<Contacts> result = new ArrayList<Contacts>();
    List<Contacts> allContacts = contactsMapper.selectAll();
    String[] contactsIdArray = contactsIds.split(",");

    long idLong;
    for (String id : contactsIdArray) {
      idLong = StringUtil.str2long(id);
      if (idLong <= 0) {
        continue;
      }
      for (Contacts contacts : allContacts) {
        if (idLong == contacts.getId().longValue()) {
          result.add(contacts);
          break;
        }
      }
    }
    return result;
  }

}