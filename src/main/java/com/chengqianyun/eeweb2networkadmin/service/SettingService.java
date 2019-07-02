package com.chengqianyun.eeweb2networkadmin.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chengqianyun.eeweb2networkadmin.biz.bean.EmailBean;
import com.chengqianyun.eeweb2networkadmin.biz.bean.SettingAlarmBean;
import com.chengqianyun.eeweb2networkadmin.biz.bean.SettingNormalBean;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.Area;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.ConsoleLoginAccount;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceInfo;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.OutCondition;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.SendContacts;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.Setting;
import com.chengqianyun.eeweb2networkadmin.biz.enums.SettingEnum;
import com.chengqianyun.eeweb2networkadmin.core.utils.PageUtilFactory;
import com.chengqianyun.eeweb2networkadmin.core.utils.StringUtil;
import com.chengqianyun.eeweb2networkadmin.core.utils.ThreeDes;
import com.chengqianyun.eeweb2networkadmin.data.ServerConnectionManager;
import java.util.ArrayList;
import java.util.List;
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
    result.setAlarm_phone(Boolean.valueOf(getData(SettingEnum.alarm_phone)));
    result.setSms_cnmi_type(getData(SettingEnum.sms_cnmi_type));
    result.setSms_center(getData(SettingEnum.sms_center));

    result.setAlarm_song(Boolean.valueOf(getData(SettingEnum.alarm_song)));
    result.setAlarm_song_config(getData(SettingEnum.alarm_song_config));

    return result;
  }

  public EmailBean email() {
    EmailBean bean = new EmailBean();

    bean.setAlarm_email(Boolean.valueOf(getData(SettingEnum.alarm_email)));
    bean.setMail_smtp_auth(Boolean.valueOf(getData(SettingEnum.mail_smtp_auth)));
    bean.setMail_transport_protocol(getData(SettingEnum.mail_transport_protocol));
    bean.setMail_send_charset(getData(SettingEnum.mail_send_charset));
    bean.setMail_smtp_port(Integer.parseInt(getData(SettingEnum.mail_smtp_port)));
    bean.setMail_is_ssl(Boolean.valueOf(getData(SettingEnum.mail_is_ssl)));
    bean.setMail_host(getData(SettingEnum.mail_host));
    bean.setMail_auth_name(getData(SettingEnum.mail_auth_name));
    bean.setMail_auth_password(getData(SettingEnum.mail_auth_password));
    bean.setMail_smtp_timeout(getData(SettingEnum.mail_smtp_timeout));
    bean.setReceiveEmails(getData(SettingEnum.receiveEmails));

    return bean;
  }

  public void save(SettingAlarmBean bean) {
    saveData(SettingEnum.alarm_sms, String.valueOf(Boolean.valueOf(bean.isAlarm_sms())));
    saveData(SettingEnum.alarm_phone, String.valueOf(Boolean.valueOf(bean.isAlarm_phone())));
    saveData(SettingEnum.sms_cnmi_type, bean.getSms_cnmi_type());
    saveData(SettingEnum.sms_center, bean.getSms_center());

    saveData(SettingEnum.alarm_song, String.valueOf(Boolean.valueOf(bean.isAlarm_song())));
    saveData(SettingEnum.alarm_song_config, bean.getAlarm_song_config());
  }





  public SettingNormalBean normal() {
    SettingNormalBean bean = new SettingNormalBean();
    bean.setData_cycle_time(getData(SettingEnum.data_cycle_time));
    bean.setPlatform_name(getData(SettingEnum.platform_name));
    bean.setHistory_data_backup_path(getData(SettingEnum.history_data_backup_path));
    bean.setIndex_reflush_data_time(getData(SettingEnum.index_reflush_data_time));
    return bean;
  }

  public void save(SettingNormalBean bean) {
    saveData(SettingEnum.data_cycle_time, bean.getData_cycle_time());
    saveData(SettingEnum.platform_name, bean.getPlatform_name());
    saveData(SettingEnum.history_data_backup_path, bean.getHistory_data_backup_path());
    saveData(SettingEnum.index_reflush_data_time, bean.getIndex_reflush_data_time());


    PageUtilFactory.platformName = bean.getPlatform_name();
    try {
      int tmp = Integer.parseInt(bean.getData_cycle_time());
      if(ServerConnectionManager.GET_DATA_CYCLE != tmp && tmp >= 10) {
        ServerConnectionManager.GET_DATA_CYCLE = tmp;
      }
    } catch(Exception e) {

    }
  }

  public void save(EmailBean bean) {

    checkEmailBean(bean);

    String newserName = "";
    String newPassword = "";
    if (StringUtil.isEmpty(bean.getMail_auth_name()) || SettingEnum.mail_auth_name.getDefaultValue().equalsIgnoreCase(bean.getMail_auth_name())) {
      saveData(SettingEnum.mail_auth_name, SettingEnum.mail_auth_name.getDefaultValue());
      saveData(SettingEnum.mail_auth_password, SettingEnum.mail_auth_password.getDefaultValue());
      newserName = SettingEnum.mail_auth_name.getDefaultValue();
      newPassword = ThreeDes.decrypt(SettingEnum.mail_auth_password.getDefaultValue());
    } else {
      saveData(SettingEnum.mail_auth_name, bean.getMail_auth_name());
      saveData(SettingEnum.mail_auth_password, ThreeDes.encrypt(bean.getMail_auth_password()));
      newserName = bean.getMail_auth_name();
      newPassword = bean.getMail_auth_password();
    }
    saveData(SettingEnum.alarm_email, String.valueOf(bean.isAlarm_email()));
    saveData(SettingEnum.mail_smtp_auth, String.valueOf(bean.isMail_smtp_auth()));
    saveData(SettingEnum.mail_is_ssl, String.valueOf(bean.isMail_is_ssl()));
    saveData(SettingEnum.mail_transport_protocol, bean.getMail_transport_protocol());
    saveData(SettingEnum.mail_smtp_port, String.valueOf(bean.getMail_smtp_port()));
    saveData(SettingEnum.mail_host, bean.getMail_host());
    saveData(SettingEnum.mail_smtp_timeout, bean.getMail_smtp_timeout());
    saveData(SettingEnum.receiveEmails, bean.getReceiveEmails());

    MailService.auth = String.valueOf(bean.isMail_smtp_auth());
    MailService.host = bean.getMail_host();
    MailService.protocol = bean.getMail_transport_protocol();
    MailService.port = bean.getMail_smtp_port();
    MailService.authName = newserName;
    MailService.password = newPassword;
    MailService.charset = SettingEnum.mail_send_charset.getDefaultValue();
    MailService.isSSL = bean.isMail_is_ssl();
    MailService.timeout = bean.getMail_smtp_timeout();
  }

  public String exportConfig() {
    // 区域,设备,条件,区域关联人,系统设置
    List<Area> areaList = areaMapper.listAll();
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("areaList", JSON.toJSONString(areaList));

    List<DeviceInfo> deviceInfoList = deviceInfoMapper.findAll();
    jsonObject.put("deviceInfoList", JSON.toJSONString(deviceInfoList));

    List<OutCondition> outConditionList = outConditionMapper.findAll();
    jsonObject.put("outConditionList", JSON.toJSONString(outConditionList));

    List<SendContacts> sendContactsList = sendContactsMapper.selectAll();
    jsonObject.put("sendContactsList", JSON.toJSONString(sendContactsList));

    List<Setting> settingList = settingMapper.findAll();
    jsonObject.put("settingList", JSON.toJSONString(settingList));

    List<ConsoleLoginAccount> accountList = consoleLoginAccountMapper.selectAll();
    jsonObject.put("accountList", JSON.toJSONString(accountList));

    return ThreeDes.encrypt(jsonObject.toString());
  }

  public void importConfig(String fileContent) {
    String jsonValue = ThreeDes.decrypt(fileContent);
    JSONObject jsonObject;
    try {
      jsonObject = JSON.parseObject(jsonValue);
      if (jsonObject == null) {
        throw new RuntimeException("导入文件内容不正确");
      }
    } catch (Exception e) {
      throw new RuntimeException("导入文件内容不正确");
    }

    List<Area> areaList = JSON.parseArray(jsonObject.getString("areaList"), Area.class);
    areaMapper.deleteForExport();
    for (Area area : areaList) {
      areaMapper.insertSelective(area);
    }

    List<DeviceInfo> deviceInfoList = JSON.parseArray(jsonObject.getString("deviceInfoList"), DeviceInfo.class);
    deviceInfoMapper.deleteForExport();
    for (DeviceInfo deviceInfo : deviceInfoList) {
      deviceInfoMapper.insertSelective(deviceInfo);
    }

    List<OutCondition> outConditionList = JSON.parseArray(jsonObject.getString("outConditionList"), OutCondition.class);
    outConditionMapper.deleteForExport();
    for (OutCondition outCondition : outConditionList) {
      outConditionMapper.insertSelective(outCondition);
    }

    List<SendContacts> sendContactsList = JSON.parseArray(jsonObject.getString("sendContactsList"), SendContacts.class);
    sendContactsMapper.deleteForExport();
    for (SendContacts sendContacts : sendContactsList) {
      sendContactsMapper.insertForImport(sendContacts);
    }

    List<Setting> settingList = JSON.parseArray(jsonObject.getString("settingList"), Setting.class);
    settingMapper.deleteForExport();
    for (Setting setting : settingList) {
      settingMapper.insertSelective(setting);
    }

    List<ConsoleLoginAccount> accountList = JSON.parseArray(jsonObject.getString("accountList"), ConsoleLoginAccount.class);
    consoleLoginAccountMapper.deleteForExport();
    for (ConsoleLoginAccount account : accountList) {
      consoleLoginAccountMapper.insertByImport(account);
    }

  }

  private void checkEmailBean(EmailBean bean) {
    if (StringUtil.isEmpty(bean.getMail_transport_protocol())) {
      throw new RuntimeException("传输协议不能为空");
    }

    if (bean.getMail_smtp_port() < 1) {
      throw new RuntimeException("端口必须是大于0的整数");
    }

    try {
      Integer.parseInt(bean.getMail_smtp_timeout());
    } catch (Exception e) {
      throw new RuntimeException("超时时间必须是大于0的整数");
    }

    String receiveEmails = bean.getReceiveEmails();
    if(Boolean.valueOf(bean.isAlarm_email()).booleanValue()) {
      if(StringUtil.isEmpty(receiveEmails)) {
        throw new RuntimeException("启用邮件报警,接收人不能为空");
      }
      if(receiveEmails.indexOf("@") < 1) {
        throw new RuntimeException("接收人格式不正确");
      }
    }

    if(receiveEmails == null) {
      receiveEmails = "";
    }
    bean.setReceiveEmails(receiveEmails.replaceAll(" ", "").replaceAll("，",","));
  }


}