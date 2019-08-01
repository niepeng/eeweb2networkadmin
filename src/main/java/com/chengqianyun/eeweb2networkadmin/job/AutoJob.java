package com.chengqianyun.eeweb2networkadmin.job;


import com.chengqianyun.eeweb2networkadmin.biz.SystemConstants.Times;
import com.chengqianyun.eeweb2networkadmin.biz.bean.DeviceDataHistoryBean;
import com.chengqianyun.eeweb2networkadmin.biz.bean.export.ExportHelperBean;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.Area;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.AreaMapper;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceDataIntime;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceDataIntimeMapper;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceInfo;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceInfoMapper;
import com.chengqianyun.eeweb2networkadmin.biz.enums.DeviceTypeEnum;
import com.chengqianyun.eeweb2networkadmin.biz.enums.SettingEnum;
import com.chengqianyun.eeweb2networkadmin.core.utils.DateUtil;
import com.chengqianyun.eeweb2networkadmin.core.utils.ExportExcel;
import com.chengqianyun.eeweb2networkadmin.core.utils.StringUtil;
import com.chengqianyun.eeweb2networkadmin.service.HistoryService;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.chengqianyun.eeweb2networkadmin.service.MailService;
import com.chengqianyun.eeweb2networkadmin.service.PhoneSmsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author 聂鹏
 * @version 1.0
 * @date 18/6/6
 */
@Slf4j
@Component
public class AutoJob {

  private final static long CLEAR_TIMES = 1 * Times.hour;


  private final static long CHECK_SERIAL_SMS = 1 * Times.hour;


  /**
   * 秒 分 小时 天 月 星期 年
   * 秒 分 小时 天 月 年
   *
   * 凌晨0点10分0秒执行一次
   */
  private final static String AUTO_BACKUP_DEVICE_HISTORY_TIME_EXP = "0 10 0 * * ?";


  @Autowired
  private DeviceInfoMapper deviceInfoMapper;

  @Autowired
  private AreaMapper areaMapper;

  @Autowired
  private DeviceDataIntimeMapper deviceDataIntimeMapper;

  @Autowired
  private HistoryService historyService;

  @Autowired
  private PhoneSmsService phoneSmsService;

  @Autowired
  private MailService mailService;


  @Scheduled(fixedDelay = CLEAR_TIMES)
  public void clearData() {
    log.info("clearDataStart............................");

    /**
     * 1.实时数据清理
     * 获取设备列表,获取每个设备数据的最新100条,删除小于该记录id的数据(即:一个设备只需要保存最新的一条)
     */
    Map<String, String> map = new HashMap<String, String>();
    map.put("startRecord", "0");
    map.put("endRecord", "1000");
    List<DeviceInfo> list = deviceInfoMapper.findPage(map);
    optClearIntime(list);

    log.info("clearDataEnd............................");
  }

  @Scheduled(fixedDelay = CHECK_SERIAL_SMS)
  public void checkSerialSms() {
    log.info("checkSerialSmsStart............................");

    /**
     * 监控短信通道：如果短信开启，邮件报警开启，一段时间去检测短信通道是否可使用，如果不正常，发送邮件报警
     */
    executeCheckSerialSms();
    log.info("checkSerialSmsEnd............................");
  }

  @Scheduled(cron = AUTO_BACKUP_DEVICE_HISTORY_TIME_EXP)
  public void autoBackupDeviceHistory() {
    /**
     * 每天自动备份（指定文件夹 D:\cqlweb\backup）
     eg. backup\2018年\1月\10日  excel形式，一个区域一个文件，excel里的tab栏是不同的设备。
     文件名 ：数据备份 区域 + 日期 .XLS  ，tab栏名称为设备名称
     */
    Date date = DateUtil.addDate(new Date(), -1);
    String year = DateUtil.getDate(date, "yyyy");
    String month = DateUtil.getDate(date, "MM");
    String day = DateUtil.getDate(date, "dd");
    String dir = "D:/cqlweb/backup/" + year + "年/" + month + "月/" + day + "日";
    File fileFolder = new File(dir);
    fileFolder.mkdirs();

    List<Area> arealist = areaMapper.listAll();
    if (arealist == null || arealist.size() == 0) {
      return;
    }
    String startTime = DateUtil.getDate(DateUtil.formatCurrentMin(date), DateUtil.dateFullPatternNoSecond);
    String endTime = DateUtil.getDate(DateUtil.formatCurrentMax(date), DateUtil.dateFullPatternNoSecond);
    int distanceTimeInt = 1;
    String dataTypes = "avg";

    for (Area area : arealist) {
      List<DeviceInfo> deviceInfoList = deviceInfoMapper.findByAreaId(area.getId());
      if (deviceInfoList == null || deviceInfoList.size() == 0) {
        continue;
      }

      try {
        String fileName = "数据备份_" + area.getName() + "_" + DateUtil.getDate(date, DateUtil.datePattern) + ".xls";
        File file = new File(fileFolder.getAbsolutePath(), fileName);
        file.createNewFile();
        ExportExcel<DeviceDataHistoryBean> exportExcel = new ExportExcel<DeviceDataHistoryBean>();
        HSSFWorkbook workbook = new HSSFWorkbook();
        boolean hasEnv = false;
        for (DeviceInfo deviceInfo : deviceInfoList) {
          if (!DeviceTypeEnum.hasEnv(deviceInfo.getType())) {
            continue;
          }
          hasEnv = true;
          ExportHelperBean<DeviceDataHistoryBean> exportHelperBean = historyService.genExportBean(startTime, endTime, distanceTimeInt, dataTypes, deviceInfo);
          exportExcel.exportExcel2(workbook, exportHelperBean);
        }

        if (hasEnv) {
          exportExcel.write(workbook, new FileOutputStream(file));
        }
      } catch (Exception e) {
        log.error("autoBackupDeviceHistoryError", e);
      }
    }

    try {
      // 删除超过1年的历史数据
      Date maxTimeDate = DateUtil.addDate(date, -365);
      String maxTime = DateUtil.getDate(maxTimeDate, DateUtil.datePattern);
      historyService.deleteByTime(maxTime);
    } catch (Exception e) {
      log.error("删除超过1年的历史数据出错", e);
    }
  }


  private void optClearIntime(List<DeviceInfo> list) {
    if (list == null || list.size() == 0) {
      return;
    }

    List<DeviceDataIntime> tmpList;
    for (DeviceInfo deviceInfo : list) {
      tmpList = deviceDataIntimeMapper.listDataOneDevice(deviceInfo.getId(), 100);
      if (tmpList == null || tmpList.size() == 0) {
        continue;
      }

      if(tmpList.size() >= 100) {
        deviceDataIntimeMapper.deleteByDeviceId(deviceInfo.getId(), tmpList.get(99).getId());
      }

    }
  }

  private void executeCheckSerialSms() {
    Boolean alarmPhone = Boolean.valueOf(phoneSmsService.getData(SettingEnum.alarm_phone));
    if (!alarmPhone) {
      log.info("current config do not need alarm phone for sms");
      return;
    }

    Boolean alarmEmail = Boolean.valueOf(phoneSmsService.getData(SettingEnum.alarm_email));
    if (!alarmEmail) {
      log.info("current config do not need send email");
      return;
    }

    String receiveEmails = phoneSmsService.getData(SettingEnum.receiveEmails);
    if (StringUtil.isEmpty(receiveEmails)) {
      log.info("receive email address is null");
      return;
    }

    boolean smsIsAvaliable = phoneSmsService.serialIsAvaliable();
    if(smsIsAvaliable) {
      log.info("smsIsAvaliable=true");
      return;
    }

    String time = DateUtil.getDate(new Date(), DateUtil.dateFullPattern);
    String subject = "短信模块异常提醒-" + time;
    String content = String.format("您的短信模块还未连接或通讯出现异常，请及时确认！\n \t \t \t %s", time);
    mailService.sendEmail(subject, receiveEmails.split(","), null, content, null);
  }




}