package com.chengqianyun.eeweb2networkadmin.service;

import com.chengqianyun.eeweb2networkadmin.biz.bean.EmailBean;
import com.chengqianyun.eeweb2networkadmin.biz.bean.SettingAlarmBean;
import com.chengqianyun.eeweb2networkadmin.biz.bean.SettingNormalBean;
import com.chengqianyun.eeweb2networkadmin.biz.enums.SettingEnum;
import com.chengqianyun.eeweb2networkadmin.core.utils.PageUtilFactory;
import com.chengqianyun.eeweb2networkadmin.core.utils.StringUtil;
import com.chengqianyun.eeweb2networkadmin.core.utils.ThreeDes;
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