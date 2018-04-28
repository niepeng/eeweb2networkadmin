package com.chengqianyun.eeweb2networkadmin.data;


import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceAlarm;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceAlarmMapper;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceDataIntime;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceDataIntimeMapper;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceInfo;
import com.chengqianyun.eeweb2networkadmin.biz.enums.AlarmTypeEnum;
import com.chengqianyun.eeweb2networkadmin.biz.enums.DeviceTypeEnum;
import com.chengqianyun.eeweb2networkadmin.biz.enums.StatusEnum;
import com.chengqianyun.eeweb2networkadmin.biz.enums.UpDownEnum;
import com.chengqianyun.eeweb2networkadmin.core.utils.Tuple2;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 聂鹏
 * @version 1.0
 * @date 18/4/28
 */
@Service
@Slf4j
public class OptDataHelper {

  @Autowired
  private DeviceDataIntimeMapper dataIntimeMapper;

  @Autowired
  private DeviceAlarmMapper deviceAlarmMapper;


  /**
   * 处理采集的数据: 实时数据,报警数据
   * 历史数据通过定时任务去处理
   *
   * @param dataIntime 环境设备信息和开关量输入设备数据
   * @param outInfo 开关量输出设备数据
   * @param deviceInfo 设备信息
   */
  public void optData(DeviceDataIntime dataIntime, Tuple2<StatusEnum, Boolean> outInfo, DeviceInfo deviceInfo) {

    // 1.记录实时数据
    dataIntime.setDeviceInfo(deviceInfo);
    dataIntime.setDeviceId(deviceInfo.getId());
    dataIntime.configStatus();
    dataIntime.configInStatus();
    if (outInfo != null) {
      dataIntime.setOutStatus(outInfo.getT1().getId());
      if (outInfo.getT1() != StatusEnum.offline) {
        dataIntime.setOut((short) (outInfo.getT2() ? 1 : 0));
      }
    }

    dataIntimeMapper.insert(dataIntime);

    // 2.记录报警数据
    recordEnvAlarm(dataIntime);
    recordInAlarm(dataIntime);

  }


  /**
   * 记录环境传感器:报警信息
   * @param dataIntime
   */
  private void recordEnvAlarm(DeviceDataIntime dataIntime) {
    if (dataIntime.getStatus() == StatusEnum.normal.getId() || dataIntime.getStatus() == 0) {
      return;
    }

    DeviceInfo deviceInfo = dataIntime.getDeviceInfo();
    boolean isOffline = dataIntime.getStatus() == StatusEnum.offline.getId();
    AlarmTypeEnum alarmTypeEnum = isOffline ? AlarmTypeEnum.offline : AlarmTypeEnum.main_alram;

    UpDownEnum tmpUpDownEnum;
    if (deviceInfo.hasTemp() && (isOffline || (dataIntime.isTempUp() || dataIntime.isTempDown()))) {
      tmpUpDownEnum = null;
      tmpUpDownEnum = dataIntime.isTempUp() ? UpDownEnum.up : tmpUpDownEnum;
      tmpUpDownEnum = dataIntime.isTempDown() ? UpDownEnum.down : tmpUpDownEnum;
      recordAlarm(dataIntime, isOffline, alarmTypeEnum, DeviceTypeEnum.temp, dataIntime.getTemp(), deviceInfo.getTempScope(), tmpUpDownEnum);
    }

    if (deviceInfo.hasHumi() && (isOffline || (dataIntime.isHumiUp() || dataIntime.isHumiDown()))) {
      tmpUpDownEnum = null;
      tmpUpDownEnum = dataIntime.isHumiUp() ? UpDownEnum.up : tmpUpDownEnum;
      tmpUpDownEnum = dataIntime.isHumiDown() ? UpDownEnum.down : tmpUpDownEnum;
      recordAlarm(dataIntime, isOffline, alarmTypeEnum, DeviceTypeEnum.humi, dataIntime.getHumi(), deviceInfo.getHumiScope(), tmpUpDownEnum);
    }

    if (deviceInfo.hasShine() && (isOffline || (dataIntime.isShineUp() || dataIntime.isShineDown()))) {
      tmpUpDownEnum = null;
      tmpUpDownEnum = dataIntime.isShineUp() ? UpDownEnum.up : tmpUpDownEnum;
      tmpUpDownEnum = dataIntime.isShineDown() ? UpDownEnum.down : tmpUpDownEnum;
      recordAlarm(dataIntime, isOffline, alarmTypeEnum, DeviceTypeEnum.shine, dataIntime.getShine(), deviceInfo.getShineScope(), tmpUpDownEnum);
    }

    if (deviceInfo.hasPower() && (isOffline || (dataIntime.isPowerUp() || dataIntime.isPowerDown()))) {
      tmpUpDownEnum = null;
      tmpUpDownEnum = dataIntime.isPowerUp() ? UpDownEnum.up : tmpUpDownEnum;
      tmpUpDownEnum = dataIntime.isPowerDown() ? UpDownEnum.down : tmpUpDownEnum;
      recordAlarm(dataIntime, isOffline, alarmTypeEnum, DeviceTypeEnum.power, dataIntime.getPower(), deviceInfo.getPowerScope(), tmpUpDownEnum);
    }

    if (deviceInfo.hasPressure() && (isOffline || (dataIntime.isPressureUp() || dataIntime.isPressureDown()))) {
      tmpUpDownEnum = null;
      tmpUpDownEnum = dataIntime.isPressureUp() ? UpDownEnum.up : tmpUpDownEnum;
      tmpUpDownEnum = dataIntime.isPressureDown() ? UpDownEnum.down : tmpUpDownEnum;
      recordAlarm(dataIntime, isOffline, alarmTypeEnum, DeviceTypeEnum.pressure, dataIntime.getPressure(), deviceInfo.getPressureScope(), tmpUpDownEnum);
    }

  }


  /**
   * 记录开关量输入:报警信息
   * @param dataIntime
   */
  private void recordInAlarm(DeviceDataIntime dataIntime) {
    if (dataIntime.getInStatus() == StatusEnum.normal.getId() || dataIntime.getInStatus() == 0) {
      return;
    }

    DeviceInfo deviceInfo = dataIntime.getDeviceInfo();
    AlarmTypeEnum alarmTypeEnum = null;
    boolean isOffline = dataIntime.getInStatus() == StatusEnum.offline.getId();
    if (deviceInfo.hasSmoke() && (isOffline || dataIntime.isSmokeAlarm())) {
      alarmTypeEnum = isOffline ? AlarmTypeEnum.offline : AlarmTypeEnum.smoke;
      recordAlarm(dataIntime, isOffline, alarmTypeEnum, DeviceTypeEnum.smoke, dataIntime.getSmoke(), null, null);
    }

    if (deviceInfo.hasWater() && (isOffline || dataIntime.isWaterAlarm())) {
      alarmTypeEnum = isOffline ? AlarmTypeEnum.offline : AlarmTypeEnum.water;
      recordAlarm(dataIntime, isOffline, alarmTypeEnum, DeviceTypeEnum.water, dataIntime.getWater(), null, null);
    }

    if (deviceInfo.hasElectric() && (isOffline || dataIntime.isElectricAlarm())) {
      alarmTypeEnum = isOffline ? AlarmTypeEnum.offline : AlarmTypeEnum.electric;
      recordAlarm(dataIntime, isOffline, alarmTypeEnum, DeviceTypeEnum.electric, dataIntime.getElectric(), null, null);
    }

    if (deviceInfo.hasBody() && (isOffline || dataIntime.isBodyAlarm())) {
      alarmTypeEnum = isOffline ? AlarmTypeEnum.offline : AlarmTypeEnum.body;
      recordAlarm(dataIntime, isOffline, alarmTypeEnum, DeviceTypeEnum.body, dataIntime.getBody(), null, null);
    }

  }

  private void recordAlarm(DeviceDataIntime dataIntime, boolean isOffline, AlarmTypeEnum alarmTypeEnum, DeviceTypeEnum deviceTypeEnum, int data, String scope, UpDownEnum tmpUpDownEnum) {
    DeviceInfo deviceInfo = dataIntime.getDeviceInfo();
    DeviceAlarm deviceAlarm = new DeviceAlarm();
    deviceAlarm.setDeviceId(deviceInfo.getId());
    deviceAlarm.setAreaId(deviceInfo.getAreaId());
    deviceAlarm.setAlarmType(alarmTypeEnum.getId());
    deviceAlarm.setDeviceOneType(deviceTypeEnum.getId());
    deviceAlarm.setData(data);
    deviceAlarm.setDataScope(scope);
    deviceAlarm.setRecentlyAlarmTime(new Date());
    if (!isOffline && tmpUpDownEnum != null) {
      deviceAlarm.setUpDown((short) tmpUpDownEnum.getId());
    }
    deviceAlarmMapper.insert(deviceAlarm);
  }
}