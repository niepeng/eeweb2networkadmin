package com.chengqianyun.eeweb2networkadmin.data;


import com.chengqianyun.eeweb2networkadmin.biz.bean.DeviceRecoverBean;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceAlarm;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceAlarmMapper;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceDataHistory;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceDataHistoryMapper;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceDataIntime;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceDataIntimeMapper;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceInfo;
import com.chengqianyun.eeweb2networkadmin.biz.enums.AlarmTypeEnum;
import com.chengqianyun.eeweb2networkadmin.biz.enums.DeviceTypeEnum;
import com.chengqianyun.eeweb2networkadmin.biz.enums.StatusEnum;
import com.chengqianyun.eeweb2networkadmin.biz.enums.UpDownEnum;
import com.chengqianyun.eeweb2networkadmin.core.utils.DateUtil;
import com.chengqianyun.eeweb2networkadmin.core.utils.SpringContextHolder;
import com.chengqianyun.eeweb2networkadmin.core.utils.Tuple2;
import com.chengqianyun.eeweb2networkadmin.service.SendPhoneService;
import java.util.Date;
import java.util.List;
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

  @Autowired
  private DeviceDataHistoryMapper deviceDataHistoryMapper;

  @Autowired
  private SendPhoneService sendPhoneService;


  /**
   * 处理采集的数据: 实时数据,报警数据,历史数据
   * 另外:通过定时任务清理 实时数据
   *
   * @param dataIntime 环境设备信息和开关量输入设备数据
   * @param outInfo 开关量输出设备数据
   * @param deviceInfo 设备信息
   */
  public void optData(DeviceDataIntime dataIntime, Tuple2<StatusEnum, Boolean> outInfo, DeviceInfo deviceInfo) {

    Date now = new Date();
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


    /**
     * 3.历史数据,如果当前分钟存在不处理
     * 如果不存在,插入
     */
    recordHistoryData(dataIntime, now);

  }

  private void recordHistoryData(DeviceDataIntime dataIntime, Date now) {
    String dateStr = DateUtil.getDate(now, DateUtil.dateFullPatternNoSecond) + ":00";
    Long historyId = deviceDataHistoryMapper.findData(dataIntime.getDeviceId(), dateStr);
    if (historyId != null && historyId > 0) {
      return;
    }
    DeviceDataHistory historyRecord = new DeviceDataHistory();
    historyRecord.setDeviceId(dataIntime.getDeviceId());
    historyRecord.setStatus(dataIntime.getStatus());
    historyRecord.setTemp(dataIntime.getTemp());
    historyRecord.setHumi(dataIntime.getHumi());
    historyRecord.setPower(dataIntime.getPower());
    historyRecord.setShine(dataIntime.getShine());
    historyRecord.setPressure(dataIntime.getPressure());
    historyRecord.setSmoke(dataIntime.getSmoke());
    historyRecord.setWater(dataIntime.getWater());
    historyRecord.setElectric(dataIntime.getElectric());
    historyRecord.setBody(dataIntime.getBody());
    historyRecord.setOut(dataIntime.getOut());
    historyRecord.setCreatedAt(DateUtil.getDate(dateStr, DateUtil.dateFullPattern));
    deviceDataHistoryMapper.insert(historyRecord);
  }


  /**
   * 记录环境传感器:报警信息
   * 如果这里没有报警,那么需要把最新的一条数据如果是报警的,把它处理掉,包括下面的某一类没有报警,那么需要确认之前的报警
   * @param dataIntime
   */
  private void recordEnvAlarm(DeviceDataIntime dataIntime) {
    if (dataIntime.getStatus() == StatusEnum.normal.getId() || dataIntime.getStatus() == 0) {
      if(dataIntime.getStatus() == StatusEnum.normal.getId()) {
        resetAlarmAll(dataIntime);
      }
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
    if(deviceInfo.hasTemp() && !dataIntime.isTempUp() && ! dataIntime.isTempDown()) {
      resetAlarm(dataIntime, DeviceTypeEnum.temp);
    }


    if (deviceInfo.hasHumi() && (isOffline || (dataIntime.isHumiUp() || dataIntime.isHumiDown()))) {
      tmpUpDownEnum = null;
      tmpUpDownEnum = dataIntime.isHumiUp() ? UpDownEnum.up : tmpUpDownEnum;
      tmpUpDownEnum = dataIntime.isHumiDown() ? UpDownEnum.down : tmpUpDownEnum;
      recordAlarm(dataIntime, isOffline, alarmTypeEnum, DeviceTypeEnum.humi, dataIntime.getHumi(), deviceInfo.getHumiScope(), tmpUpDownEnum);
    }
    if(deviceInfo.hasHumi() && !dataIntime.isHumiUp() && ! dataIntime.isHumiDown()) {
      resetAlarm(dataIntime, DeviceTypeEnum.humi);
    }



    if (deviceInfo.hasShine() && (isOffline || (dataIntime.isShineUp() || dataIntime.isShineDown()))) {
      tmpUpDownEnum = null;
      tmpUpDownEnum = dataIntime.isShineUp() ? UpDownEnum.up : tmpUpDownEnum;
      tmpUpDownEnum = dataIntime.isShineDown() ? UpDownEnum.down : tmpUpDownEnum;
      recordAlarm(dataIntime, isOffline, alarmTypeEnum, DeviceTypeEnum.shine, dataIntime.getShine(), deviceInfo.getShineScope(), tmpUpDownEnum);
    }
    if(deviceInfo.hasShine() && !dataIntime.isShineUp() && ! dataIntime.isShineDown()) {
      resetAlarm(dataIntime, DeviceTypeEnum.shine);
    }



    if (deviceInfo.hasPower() && (isOffline || (dataIntime.isPowerUp() || dataIntime.isPowerDown()))) {
      tmpUpDownEnum = null;
      tmpUpDownEnum = dataIntime.isPowerUp() ? UpDownEnum.up : tmpUpDownEnum;
      tmpUpDownEnum = dataIntime.isPowerDown() ? UpDownEnum.down : tmpUpDownEnum;
      recordAlarm(dataIntime, isOffline, alarmTypeEnum, DeviceTypeEnum.power, dataIntime.getPower(), deviceInfo.getPowerScope(), tmpUpDownEnum);
    }
    if(deviceInfo.hasPower() && !dataIntime.isPowerUp() && ! dataIntime.isPowerDown()) {
      resetAlarm(dataIntime, DeviceTypeEnum.power);
    }



    if (deviceInfo.hasPressure() && (isOffline || (dataIntime.isPressureUp() || dataIntime.isPressureDown()))) {
      tmpUpDownEnum = null;
      tmpUpDownEnum = dataIntime.isPressureUp() ? UpDownEnum.up : tmpUpDownEnum;
      tmpUpDownEnum = dataIntime.isPressureDown() ? UpDownEnum.down : tmpUpDownEnum;
      recordAlarm(dataIntime, isOffline, alarmTypeEnum, DeviceTypeEnum.pressure, dataIntime.getPressure(), deviceInfo.getPressureScope(), tmpUpDownEnum);
    }
    if(deviceInfo.hasPressure() && !dataIntime.isPressureUp() && ! dataIntime.isPressureDown()) {
      resetAlarm(dataIntime, DeviceTypeEnum.pressure);
    }

  }

  private void resetAlarmAll(DeviceDataIntime dataIntime) {
    List<DeviceAlarm> alarmList = deviceAlarmMapper.selectAlarmByDeviceId(dataIntime.getDeviceInfo().getId());
    if (alarmList == null || alarmList.size() == 0) {
      return;
    }

    for (DeviceAlarm deviceAlarm : alarmList) {
      // 记录恢复报警信息
      DeviceRecoverBean bean = new DeviceRecoverBean();
      bean.setDeviceInfo(dataIntime.getDeviceInfo());
      bean.setAll(false);
      bean.setTime(new Date());
      bean.setDeviceTypeEnum(DeviceTypeEnum.getOneById(deviceAlarm.getDeviceOneType()));
      bean.setDeviceAlarm(deviceAlarm);
      sendPhoneService.sendAlarmRecoverInfo(bean);
    }

    DeviceAlarm deviceAlarm = new DeviceAlarm();
    DeviceInfo deviceInfo = dataIntime.getDeviceInfo();
    deviceAlarm.setDeviceId(deviceInfo.getId());
    deviceAlarmMapper.resetDeviceAlarmAll(deviceAlarm);
  }

  private void resetAlarm(DeviceDataIntime dataIntime, DeviceTypeEnum deviceTypeEnum) {
    Long id = deviceAlarmMapper.hasAlarmDataByDeviceIdAndOneType(dataIntime.getDeviceInfo().getId(), deviceTypeEnum.getId());
    if (id != null && id > 0) {
      // 记录恢复报警信息
      DeviceRecoverBean bean = new DeviceRecoverBean();
      bean.setDeviceInfo(dataIntime.getDeviceInfo());
      bean.setAll(false);
      bean.setTime(new Date());
      bean.setDeviceTypeEnum(deviceTypeEnum);
      bean.setDeviceAlarm(deviceAlarmMapper.selectByPrimaryKey(id));
      sendPhoneService.sendAlarmRecoverInfo(bean);
    }

    DeviceAlarm deviceAlarm = new DeviceAlarm();
    DeviceInfo deviceInfo = dataIntime.getDeviceInfo();
    deviceAlarm.setDeviceId(deviceInfo.getId());
    deviceAlarm.setDeviceOneType(deviceTypeEnum.getId());
    deviceAlarmMapper.resetDeviceAlarm(deviceAlarm);
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

  /**
   * @return  true 插入,  false 修改
   */
  private boolean recordAlarm(DeviceDataIntime dataIntime, boolean isOffline, AlarmTypeEnum alarmTypeEnum, DeviceTypeEnum deviceTypeEnum, int data, String scope, UpDownEnum tmpUpDownEnum) {
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

    // 如果已经存在没有确认过的,同样设备传感器,同样的报警类型,那么不需要插入,更新时间和数值
    Long id = deviceAlarmMapper.hasData(deviceAlarm);
    if(id != null && id > 0) {
      DeviceAlarm fromDB = deviceAlarmMapper.selectByPrimaryKey(id);
      fromDB.setData(data);
      fromDB.setDataScope(scope);
      fromDB.setRecentlyAlarmTime(new Date());
      deviceAlarmMapper.updateByPrimaryKey(fromDB);
      return false;
    }
    deviceAlarmMapper.insert(deviceAlarm);
    SpringContextHolder.getBean(SendPhoneService.class).sendAlarmInfo(deviceAlarm);
    return true;
  }
}