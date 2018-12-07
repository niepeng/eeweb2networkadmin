package com.chengqianyun.eeweb2networkadmin.service;

import com.chengqianyun.eeweb2networkadmin.biz.SystemConstants.Times;
import com.chengqianyun.eeweb2networkadmin.biz.bean.DeviceRecoverBean;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.Area;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.Contacts;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceAlarm;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceInfo;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.SendContacts;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.SendContactsMapper;
import com.chengqianyun.eeweb2networkadmin.biz.enums.SettingEnum;
import com.chengqianyun.eeweb2networkadmin.core.utils.DateUtil;
import com.chengqianyun.eeweb2networkadmin.core.utils.StringUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * @author 聂鹏
 * @version 1.0
 * @date 18/8/22
 */
@Service
@Slf4j
public class SendPhoneService extends BaseService {

  @Autowired
  private SerialService serialService;

  @Autowired
  private MailService mailService;

  /**
   * 设备报警记录,需要发送信息等
   */
  private static BlockingQueue<DeviceAlarm> alarmInfoQueue = new LinkedBlockingQueue<DeviceAlarm>(1000);

  /**
   * 设备恢复记录,需要发送
   */
  private static BlockingQueue<DeviceRecoverBean> recoverInfoQueue = new LinkedBlockingQueue<DeviceRecoverBean>(50);

  private static boolean isRunning;
  private static boolean isRecoverRunning;

  private final static long TIMES = 1 * Times.hour;
  static String smsContent = "设备(%s)%s报警时间:%s";
  static String smsRecoverContent = "设备(%s)%s,恢复时间:%s";

  static String mailTitle = "设备(%s)报警";
  static String mailContent = "设备(%s)%s报警时间:%s";

  static String mailRecoverTitle = "设备(%s)恢复报警";
  static String mailRecoverContent = "设备(%s)%s,恢复时间:%s";

  public void sendAlarmInfo(DeviceAlarm deviceAlarm) {
    /**
     *   一般队列方法:
     *  add(E e):将元素e插入到队列末尾，如果插入成功，则返回true；如果插入失败（即队列已满），则会抛出异常；
     　　remove()：移除队首元素，若移除成功，则返回true；如果移除失败（队列为空），则会抛出异常；
     　　offer(E e)：将元素e插入到队列末尾，如果插入成功，则返回true；如果插入失败（即队列已满），则返回false；
     　　poll()：移除并获取队首元素，若成功，则返回队首元素；否则返回null；
     　　peek()：获取队首元素，若成功，则返回队首元素；否则返回null

        阻塞队列方法:
         put方法用来向队尾存入元素，如果队列满，则等待；
     　　take方法用来从队首取元素，如果队列为空，则等待；
     　　offer方法用来向队尾存入元素，如果队列满，则等待一定的时间，当时间期限达到时，如果还没有插入成功，则返回false；否则返回true；
     　　poll方法用来从队首取元素，如果队列空，则等待一定的时间，当时间期限达到时，如果取到，则返回null；否则返回取得的元素；


     这里使用非阻塞方法,为了防止影响采集,并且把队列长度设置成1000
     */
      alarmInfoQueue.offer(deviceAlarm);
  }

  public void sendAlarmRecoverInfo(DeviceRecoverBean bean) {
    recoverInfoQueue.offer(bean);
  }

  @Scheduled(fixedDelay = TIMES)
  public void sendAlarm() {
    if (isRunning) {
      return;
    }
    isRunning = true;
    DeviceAlarm deviceAlarm;
    try {
      while (true) {
        deviceAlarm = alarmInfoQueue.take();
        optAlarmInfo(deviceAlarm);
      }
    } catch (Exception e) {
    }

    isRunning = false;
  }

  @Scheduled(fixedDelay = TIMES)
  public void sendRecoverAlarm() {
    if (isRecoverRunning) {
      return;
    }
    isRecoverRunning = true;
    DeviceRecoverBean deviceRecoverBean;
    try {
      while (true) {
        deviceRecoverBean = recoverInfoQueue.take();
        optAlarmRecover(deviceRecoverBean);
      }
    } catch (Exception e) {
    }

    isRecoverRunning = false;
  }

  /**
   * 执行恢复报警
   *
   * @param deviceRecoverBean
   */
  private void optAlarmRecover(DeviceRecoverBean deviceRecoverBean) {
    DeviceInfo deviceInfo = deviceRecoverBean.getDeviceInfo();
    List<Contacts> contactsList = getContactsByAreaId(deviceInfo.getAreaId());
    String deviceName = deviceInfo == null ? "" : (StringUtil.isEmpty(deviceInfo.getName()) ? "" : deviceInfo.getName());
    deviceName = StringUtil.isEmpty(deviceName) ? "未定义" : deviceName;

    // 发送邮件
    String recoverMailTitle = String.format(mailRecoverTitle, deviceName);
    String recoverMailContent = String.format(mailRecoverContent, deviceName, deviceRecoverBean.isAll() ? "恢复报警" : deviceRecoverBean.getDeviceAlarm().showRecoverMsg(),
        DateUtil.getDate(deviceRecoverBean.getTime(), DateUtil.dateFullPatternNoSecond));
    sendRecoverEmail(recoverMailTitle, recoverMailContent);

    // 发送短信和电话
    String content = String.format(smsRecoverContent, deviceName,
        deviceRecoverBean.isAll() ? "恢复报警" : deviceRecoverBean.getDeviceAlarm().showRecoverMsg()
        , DateUtil.getDate(deviceRecoverBean.getTime(), DateUtil.dateFullPatternNoSecond));
    sendSms(contactsList, content);

  }


  private void optAlarmInfo(DeviceAlarm deviceAlarm) {
    // 发邮件
    sendEmail(deviceAlarm);

    // 这里实现拨打电话和发送短信功能
    boolean phoneFlag = Boolean.valueOf(getData(SettingEnum.alarm_phone));
    boolean smsFlag = Boolean.valueOf(getData(SettingEnum.alarm_sms));

    if (!phoneFlag && !smsFlag) {
      return;
    }

    List<Contacts> contactsList = getContactsByAreaId(deviceAlarm.getAreaId());
    if(contactsList == null || contactsList.size() == 0) {
      return;
    }


    DeviceInfo deviceInfo = deviceInfoMapper.selectByPrimaryKey(deviceAlarm.getDeviceId());
    String deviceName = deviceInfo == null ? "" : (StringUtil.isEmpty(deviceInfo.getName()) ? "" : deviceInfo.getName());
    String content = String.format(smsContent, StringUtil.isEmpty(deviceName) ? "未定义" : deviceName, deviceAlarm.showAlarmMsg(), DateUtil.getDate(deviceAlarm.getRecentlyAlarmTime(), DateUtil.dateFullPatternNoSecond));

    if (phoneFlag) {
      try {
        for (Contacts contacts : contactsList) {
          boolean flag = serialService.callPhoneOrSms(contacts.getPhone(), null, false);
          if (flag) {
            insertSendRecord(contacts, "phone", null);
          }
        }
      } catch (Exception e) {
        log.error("callPhone_error", e);
      }
    }

    sendSms(contactsList, content);

  }



  private void sendEmail(DeviceAlarm deviceAlarm) {
    try {
      Boolean alarmEmail = Boolean.valueOf(getData(SettingEnum.alarm_email));
      if (!alarmEmail) {
        log.info("current config do not need send email");
        return;
      }

      if (StringUtil.isEmpty(MailService.authName) || StringUtil.isEmpty(MailService.password)) {
        log.info("mail user or password is null");
        return;
      }

      String receiveEmails = getData(SettingEnum.receiveEmails);
      if (StringUtil.isEmpty(receiveEmails)) {
        log.info("receive email address is null");
        return;
      }

      DeviceInfo deviceInfo = deviceInfoMapper.selectByPrimaryKey(deviceAlarm.getDeviceId());
      String deviceName = deviceInfo == null ? "" : (StringUtil.isEmpty(deviceInfo.getName()) ? "" : deviceInfo.getName());
      deviceName = StringUtil.isEmpty(deviceName) ? "未定义" : deviceName;
      String content = String.format(mailContent, deviceName, deviceAlarm.showAlarmMsg(), DateUtil.getDate(deviceAlarm.getRecentlyAlarmTime(), DateUtil.dateFullPatternNoSecond));
      String title = String.format(mailTitle, deviceName);

      log.info("send alarm emails to {}", receiveEmails);
      mailService.sendEmail(title, receiveEmails.split(","), null, content, null);

    } catch (Exception e) {
      log.error("send alarm error", e);
    }
  }

  private void sendRecoverEmail(String title, String content) {
    try {
      Boolean alarmEmail = Boolean.valueOf(getData(SettingEnum.alarm_email));
      if (!alarmEmail) {
        log.info("recover alarm email : current config do not need send email");
        return;
      }

      if (StringUtil.isEmpty(MailService.authName) || StringUtil.isEmpty(MailService.password)) {
        log.info("recover alarm email : mail user or password is null");
        return;
      }

      String receiveEmails = getData(SettingEnum.receiveEmails);
      if (StringUtil.isEmpty(receiveEmails)) {
        log.info("recover alarm email : receive email address is null");
        return;
      }

      log.info("send recover alarm emails to {}", receiveEmails);
      mailService.sendEmail(title, receiveEmails.split(","), null, content, null);
    } catch (Exception e) {
      log.error("send recover alarm error", e);
    }
  }

  public void sendSms(List<Contacts> contactsList, String content) {
    boolean smsFlag = Boolean.valueOf(getData(SettingEnum.alarm_sms));
    if(!smsFlag) {
      return;
    }

    try {
      for (Contacts contacts : contactsList) {
        boolean flag = serialService.callPhoneOrSms(contacts.getPhone(), content, true);
        if (flag) {
          insertSendRecord(contacts, "sms", content);
        }
      }
    } catch (Exception e) {
      log.error("sendSms_error", e);
    }

  }

  private void insertSendRecord(Contacts contacts, String type, String smsContent) {
    SendContacts s = new SendContacts();
    s.setName(contacts.getName());
    s.setPhone(contacts.getPhone());
    s.setType(type);
    s.setSmsContent(smsContent);
    sendContactsMapper.insert(s);
  }

}