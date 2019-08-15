package com.chengqianyun.eeweb2networkadmin.service;

import com.chengqianyun.eeweb2networkadmin.biz.bean.PhoneTask;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.SendContacts;
import com.chengqianyun.eeweb2networkadmin.biz.enums.SettingEnum;
import com.chengqianyun.eeweb2networkadmin.core.utils.StringUtil;
import com.chengqianyun.eeweb2networkadmin.core.utils.Tuple2;
import com.chengqianyun.eeweb2networkadmin.data.CallSmsHelper;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * 1.通过阻塞队列实现生产者和消费者模式,处理发送短信和拨打电话
 * 2.串口关闭是通过单独的一个定时任务程序去扫描 PhoneSmsService.closeSerialTime变量,如果到了关闭时间 serialRunning 为 true,那么需要去执行关闭操作
 * 3.每次执行完一个任务, 关闭程序时间 = 当前时间 + 5分钟
 *
 * @version 1.0
 * @email lsb@51huadian.cn
 * @date 19/3/18
 */
@Service
@Slf4j
public class PhoneSmsService extends BaseService {

  // 需要关闭串口的时间
  static AtomicLong closeSerialTime = new AtomicLong();
  // 当前窗口是否已开启
  public static AtomicBoolean serialRunning = new AtomicBoolean(false);

  // 当前系统中的任务(短信和电话)
  static LinkedBlockingQueue<PhoneTask> taskQueue = new LinkedBlockingQueue();

  @Autowired
  private CallSmsHelper callSmsHelper;

  public boolean serialIsAvaliable() {
    if (serialRunning.get()) {
      return true;
    }
    return callSmsHelper.checkSerialAvailable();
  }

  public static void markRunning() {
    if(!serialRunning.get()) {
      serialRunning.set(true);
    }
    closeSerialTime.set(System.currentTimeMillis() + 5 * 60 * 1000);
  }

  /**
   * 开启生产者和消费者模式线程
   */
  @PostConstruct
  public void init() {
    new Thread(new Runnable() {
      @Override
      public void run() {
        while(true) {
          try {
            PhoneTask task = taskQueue.take();
            Tuple2<Boolean, Boolean> tuple2 = callSmsHelper.execute(task);
            if (tuple2 == null) {
              continue;
            }
            if (tuple2.getT1() != null && tuple2.getT1()) {
              insertSendRecord(task.getContractName(), task.getPhone(), "phone", task.getSmsContent());
            }
            if (tuple2.getT2() != null && tuple2.getT2()) {
              insertSendRecord(task.getContractName(), task.getPhone(), "sms", task.getSmsContent());
            }
          } catch (Exception e) {
            log.error("PhoneSmsService.initError", e);
          }
        }
      }
    }).start();
  }

  public void autoCloseSerial() {
    if (!serialRunning.get()) {
      return;
    }

    long now = System.currentTimeMillis();
    if (now > closeSerialTime.get()) {
      closeSerial();
      serialRunning.set(false);
    }
  }

  /**
   * 负责发送短信or电话,有可能2个都有; 发送完成后,还需要入库
   *
   * @param phone  如果不为空,那么需要打电话
   * @param smsContent 如果不为空,需要发送短信
   * @param contractName 接收人的姓名
   * @param needCallPhone  是否拨打电话
   * @param contractName 是否发送短信
   *
   * @return
   */
  public void callPhoneOrSms(String phone, String smsContent, String contractName, boolean needCallPhone, boolean needSendSms) {
    if (StringUtil.isEmpty(phone)) {
      return;
    }

    if(!needCallPhone && !needSendSms) {
      return;
    }

    boolean sendSms = Boolean.valueOf(getData(SettingEnum.alarm_sms));
    boolean callPhone = Boolean.valueOf(getData(SettingEnum.alarm_phone));
    if (!sendSms && !callPhone) {
      return;
    }

    PhoneTask task = new PhoneTask();
    task.setPhone(phone);
    task.setSmsContent(smsContent);
    task.setContractName(contractName);

    callPhone &= needCallPhone;
    sendSms &= (!StringUtil.isEmpty(smsContent) && needSendSms);

    // 打电话 和 短信
    if (callPhone || sendSms) {
      task.setCall(callPhone);
      task.setSms(sendSms);
      taskQueue.offer(task);
    }
  }


  /**
   * 关闭串口:短信连接部分
   */
  public void closeSerial() {
    callSmsHelper.close();
  }


  protected void insertSendRecord(String contractName, String phone, String type, String smsContent) {
    SendContacts s = new SendContacts();
    s.setName(contractName);
    s.setPhone(phone);
    s.setType(type);
    s.setSmsContent(smsContent);
    sendContactsMapper.insert(s);
  }

}