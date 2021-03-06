package com.chengqianyun.eeweb2networkadmin.service;


import com.chengqianyun.eeweb2networkadmin.biz.entitys.Area;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceAlarm;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.Setting;
import com.chengqianyun.eeweb2networkadmin.biz.enums.AlarmConfirmEnum;
import com.chengqianyun.eeweb2networkadmin.biz.enums.SettingEnum;
import com.chengqianyun.eeweb2networkadmin.biz.page.PageResult;
import com.chengqianyun.eeweb2networkadmin.biz.page.PaginationQuery;
import com.chengqianyun.eeweb2networkadmin.core.utils.StringUtil;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 聂鹏
 * @version 1.0
 * @date 18/4/17
 */
@Service
@Slf4j
public class AlarmService extends BaseService {


  public boolean hasAlarmData() {
    String value = getData(SettingEnum.alarm_song);
    if (!Boolean.valueOf(value)) {
      return false;
    }
    Long id = deviceAlarmMapper.hasAlarmData();
    return id != null && id > 0;
  }


  public PageResult<DeviceAlarm> getAlarmList(PaginationQuery query) {
    PageResult<DeviceAlarm> result = null;
    try {

      Integer count = deviceAlarmMapper.findPageCount(query.getQueryData());

      if (null != count && count.intValue() > 0) {
        int startRecord = (query.getPageIndex() - 1) * query.getRowsPerPage();
        query.addQueryData("startRecord", Integer.toString(startRecord));
        query.addQueryData("endRecord", Integer.toString(query.getRowsPerPage()));
        List<DeviceAlarm> list = deviceAlarmMapper.findPage(query.getQueryData());
        if (list != null) {
          for (DeviceAlarm deviceAlarm : list) {
            deviceAlarm.setDeviceInfo(deviceInfoMapper.selectByPrimaryKey(deviceAlarm.getDeviceId()));
          }
        }
        result = new PageResult<DeviceAlarm>(list, count, query);
      }
    } catch (Exception e) {
      log.error("AlarmService.getAlarmList,Error", e);
    }
    return result;
  }

  public void markRead(long alarmId, String newNote) {
    DeviceAlarm deviceAlarm = deviceAlarmMapper.selectByPrimaryKey(alarmId);
    if (deviceAlarm == null) {
      return;
    }

    if (AlarmConfirmEnum.find(deviceAlarm.getConfirm()) == AlarmConfirmEnum.no_confirm) {
      throw new RuntimeException("当前报警还未结束,不能编写备注");
    }

    if (deviceAlarm.getUserConfirmTime() == null) {
      deviceAlarm.setUserConfirmTime(new Date());
    }

    if (!StringUtil.isEmpty(newNote)) {
      deviceAlarm.setNote(newNote);
    }
    deviceAlarmMapper.updateByPrimaryKeySelective(deviceAlarm);
  }

  public void deleteAlarm(Long id) {
    deviceAlarmMapper.deleteByPrimaryKey(id);
  }


}