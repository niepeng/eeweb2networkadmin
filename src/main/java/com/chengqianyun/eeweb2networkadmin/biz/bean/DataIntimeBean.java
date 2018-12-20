package com.chengqianyun.eeweb2networkadmin.biz.bean;

import com.chengqianyun.eeweb2networkadmin.biz.entitys.Area;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceDataIntime;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceInfo;
import com.chengqianyun.eeweb2networkadmin.biz.enums.DeviceTypeEnum;
import com.chengqianyun.eeweb2networkadmin.biz.enums.StatusEnum;
import com.chengqianyun.eeweb2networkadmin.biz.enums.UpDownEnum;
import com.chengqianyun.eeweb2networkadmin.core.utils.UnitUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.ToString;

/**
 * @author 聂鹏
 * @version 1.0
 * @date 18/4/17
 */
@Data
@ToString
public class DataIntimeBean {

  /**
   * 选中的传感器
   */
  private List<DeviceTypeEnum> deviceTypeList;
  /**
   * 选中的状态
   */
  private List<StatusEnum> statusList;
  /**
   * 选中的区域
   */
  private List<Area> areaList;
  /**
   * 筛选的设备id列表
   */
  private Map<Long,Object> deviceIdMap;

  /**
   * 筛选结果数据
   */
  private List<DeviceDataIntime> dataList;
  /**
   * 筛选结果数据:单个类型展示的列表
   */
  private List<ElementDataBean> elementDataBeanList;


  // ================ 扩展属性 ================

  private String deviceTypes;

  private String statuses;

  private String areaIds;

  // ================ 扩展方法 ================

  /**
   * 1.time 时间排序就是默认的
   * 2.alarm 按照报警优先排序
   */
  public void sort(String type) {
    if("time".equals(type)) {
      return;
    }

    if(elementDataBeanList == null || elementDataBeanList.size() <= 1) {
      return;
    }

    Collections.sort(elementDataBeanList, new Comparator<ElementDataBean>(){
      @Override
      public int compare(ElementDataBean o1, ElementDataBean o2) {
        if(o1.getStatus() == StatusEnum.normal.getId()) {
          return 1;
        }
        if(o2.getStatus() == StatusEnum.normal.getId()) {
          return -1;
        }
        return 0;
      }
    });
  }

  public boolean hasArea(long areaId) {
    if (areaList == null) {
      return false;
    }
    for (Area area : areaList) {
      if (area.getId() == areaId) {
        return true;
      }
    }
    return false;
  }

  public boolean hasStatus(int id) {
    if (statusList == null) {
      return false;
    }
    for (StatusEnum statusEnum : statusList) {
      if (id == statusEnum.getId()) {
        return true;
      }
    }
    return false;
  }

  public boolean needDevice(Long deviceId) {
    if (deviceIdMap == null) {
      return true;
    }
    if (deviceIdMap.containsKey(deviceId)) {
      return true;
    }
    return false;
  }

  public boolean hasOneDeviceType(int type) {
    if (deviceTypeList == null) {
      return false;
    }

    for (DeviceTypeEnum deviceTypeEnum : deviceTypeList) {
      if (DeviceTypeEnum.hasType(type, deviceTypeEnum)) {
        return true;
      }
    }
    return false;
  }

  public void mergeElementData() {
    elementDataBeanList = new ArrayList<ElementDataBean>();

    if (dataList == null) {
      return;
    }

    int tmpType;
    for (DeviceDataIntime dataIntime : dataList) {
      tmpType = dataIntime.getDeviceInfo().getType();

      if (hasOneDeviceType(DeviceTypeEnum.temp.getId()) && DeviceTypeEnum.hasType(tmpType, DeviceTypeEnum.temp)) {
        ElementDataBean elementDataBean = new ElementDataBean();
        elementDataBean.setDeviceInfo(dataIntime.getDeviceInfo());
        elementDataBean.setArea(dataIntime.getDeviceInfo().getArea());
        elementDataBean.setStatus(dataIntime.getStatus());
        if(dataIntime.getStatus() == StatusEnum.alarm.getId()) {
          if (dataIntime.isTempUp() || dataIntime.isTempDown()) {
//            elementDataBean.setStatus(StatusEnum.alarm.getId());
            elementDataBean.setStatus(dataIntime.isTempUp() ? StatusEnum.alarm_up.getId() : StatusEnum.alarm_down.getId());
          } else {
            elementDataBean.setStatus(StatusEnum.normal.getId());
          }
        }
        elementDataBean.setTime(dataIntime.getUpdatedAt());
        elementDataBean.setData(UnitUtil.changeTemp(dataIntime.getTemp()));
        elementDataBean.setDeviceOneTypeEnum(DeviceTypeEnum.temp);
        elementDataBeanList.add(elementDataBean);
      }
      if (hasOneDeviceType(DeviceTypeEnum.humi.getId()) && DeviceTypeEnum.hasType(tmpType, DeviceTypeEnum.humi)) {
        ElementDataBean elementDataBean = new ElementDataBean();
        elementDataBean.setDeviceInfo(dataIntime.getDeviceInfo());
        elementDataBean.setArea(dataIntime.getDeviceInfo().getArea());
        elementDataBean.setStatus(dataIntime.getStatus());
        if(dataIntime.getStatus() == StatusEnum.alarm.getId()) {
          if (dataIntime.isHumiUp() || dataIntime.isHumiDown()) {
//            elementDataBean.setStatus(StatusEnum.alarm.getId());
            elementDataBean.setStatus(dataIntime.isHumiUp() ? StatusEnum.alarm_up.getId() : StatusEnum.alarm_down.getId());
          } else {
            elementDataBean.setStatus(StatusEnum.normal.getId());
          }
        }
        elementDataBean.setTime(dataIntime.getUpdatedAt());
        elementDataBean.setData(UnitUtil.changeHumi(dataIntime.getHumi()));
        elementDataBean.setDeviceOneTypeEnum(DeviceTypeEnum.humi);
        elementDataBeanList.add(elementDataBean);
      }
      if (hasOneDeviceType(DeviceTypeEnum.power.getId()) && DeviceTypeEnum.hasType(tmpType, DeviceTypeEnum.power)) {
        ElementDataBean elementDataBean = new ElementDataBean();
        elementDataBean.setDeviceInfo(dataIntime.getDeviceInfo());
        elementDataBean.setArea(dataIntime.getDeviceInfo().getArea());
        elementDataBean.setStatus(dataIntime.getStatus());
        if(dataIntime.getStatus() == StatusEnum.alarm.getId()) {
          if (dataIntime.isPowerUp() || dataIntime.isPowerDown()) {
//            elementDataBean.setStatus(StatusEnum.alarm.getId());
            elementDataBean.setStatus(dataIntime.isPowerUp() ? StatusEnum.alarm_up.getId() : StatusEnum.alarm_down.getId());
            elementDataBean.setStatus(StatusEnum.alarm.getId());
          } else {
            elementDataBean.setStatus(StatusEnum.normal.getId());
          }
        }
        elementDataBean.setTime(dataIntime.getUpdatedAt());
        elementDataBean.setData(UnitUtil.changePower(dataIntime.getPower()));
        elementDataBean.setDeviceOneTypeEnum(DeviceTypeEnum.power);
        elementDataBeanList.add(elementDataBean);
      }
      if (hasOneDeviceType(DeviceTypeEnum.shine.getId()) && DeviceTypeEnum.hasType(tmpType, DeviceTypeEnum.shine)) {
        ElementDataBean elementDataBean = new ElementDataBean();
        elementDataBean.setDeviceInfo(dataIntime.getDeviceInfo());
        elementDataBean.setArea(dataIntime.getDeviceInfo().getArea());
        elementDataBean.setStatus(dataIntime.getStatus());
        if(dataIntime.getStatus() == StatusEnum.alarm.getId()) {
          if (dataIntime.isShineUp() || dataIntime.isShineDown()) {
//            elementDataBean.setStatus(StatusEnum.alarm.getId());
            elementDataBean.setStatus(dataIntime.isShineUp() ? StatusEnum.alarm_up.getId() : StatusEnum.alarm_down.getId());
          } else {
            elementDataBean.setStatus(StatusEnum.normal.getId());
          }
        }
        elementDataBean.setTime(dataIntime.getUpdatedAt());
        elementDataBean.setData(String.valueOf(dataIntime.getShine()));
        elementDataBean.setDeviceOneTypeEnum(DeviceTypeEnum.shine);
        elementDataBeanList.add(elementDataBean);
      }
      if (hasOneDeviceType(DeviceTypeEnum.pressure.getId()) && DeviceTypeEnum.hasType(tmpType, DeviceTypeEnum.pressure)) {
        ElementDataBean elementDataBean = new ElementDataBean();
        elementDataBean.setDeviceInfo(dataIntime.getDeviceInfo());
        elementDataBean.setArea(dataIntime.getDeviceInfo().getArea());
        elementDataBean.setStatus(dataIntime.getStatus());
        if(dataIntime.getStatus() == StatusEnum.alarm.getId()) {
          if (dataIntime.isPressureUp() || dataIntime.isPressureDown()) {
//            elementDataBean.setStatus(StatusEnum.alarm.getId());
            elementDataBean.setStatus(dataIntime.isPressureUp() ? StatusEnum.alarm_up.getId() : StatusEnum.alarm_down.getId());
          } else {
            elementDataBean.setStatus(StatusEnum.normal.getId());
          }
        }
        elementDataBean.setTime(dataIntime.getUpdatedAt());
        elementDataBean.setData(UnitUtil.changePressure(dataIntime.getPressure()));
        elementDataBean.setDeviceOneTypeEnum(DeviceTypeEnum.pressure);
        elementDataBeanList.add(elementDataBean);
      }


      if (hasOneDeviceType(DeviceTypeEnum.smoke.getId()) && DeviceTypeEnum.hasType(tmpType, DeviceTypeEnum.smoke)) {
        ElementDataBean elementDataBean = new ElementDataBean();
        elementDataBean.setDeviceInfo(dataIntime.getDeviceInfo());
        elementDataBean.setArea(dataIntime.getDeviceInfo().getArea());
        elementDataBean.setStatus(dataIntime.getInStatus());
        if(dataIntime.getInStatus() == StatusEnum.alarm.getId()) {
          if (dataIntime.isSmokeAlarm()) {
            elementDataBean.setStatus(StatusEnum.alarm.getId());
          } else {
            elementDataBean.setStatus(StatusEnum.normal.getId());
          }
        }
        elementDataBean.setTime(dataIntime.getUpdatedAt());
        elementDataBean.setData(String.valueOf(dataIntime.getSmoke()));
        elementDataBean.setDeviceOneTypeEnum(DeviceTypeEnum.smoke);
        elementDataBeanList.add(elementDataBean);
      }
      if (hasOneDeviceType(DeviceTypeEnum.water.getId()) && DeviceTypeEnum.hasType(tmpType, DeviceTypeEnum.water)) {
        ElementDataBean elementDataBean = new ElementDataBean();
        elementDataBean.setDeviceInfo(dataIntime.getDeviceInfo());
        elementDataBean.setArea(dataIntime.getDeviceInfo().getArea());
        elementDataBean.setStatus(dataIntime.getInStatus());
        if(dataIntime.getInStatus() == StatusEnum.alarm.getId()) {
          if (dataIntime.isWaterAlarm()) {
            elementDataBean.setStatus(StatusEnum.alarm.getId());
          } else {
            elementDataBean.setStatus(StatusEnum.normal.getId());
          }
        }
        elementDataBean.setTime(dataIntime.getUpdatedAt());
        elementDataBean.setData(String.valueOf(dataIntime.getWater()));
        elementDataBean.setDeviceOneTypeEnum(DeviceTypeEnum.water);
        elementDataBeanList.add(elementDataBean);
      }
      if (hasOneDeviceType(DeviceTypeEnum.electric.getId()) && DeviceTypeEnum.hasType(tmpType, DeviceTypeEnum.electric)) {
        ElementDataBean elementDataBean = new ElementDataBean();
        elementDataBean.setDeviceInfo(dataIntime.getDeviceInfo());
        elementDataBean.setArea(dataIntime.getDeviceInfo().getArea());
        elementDataBean.setStatus(dataIntime.getInStatus());
        if(dataIntime.getInStatus() == StatusEnum.alarm.getId()) {
          if (dataIntime.isElectricAlarm()) {
            elementDataBean.setStatus(StatusEnum.alarm.getId());
          } else {
            elementDataBean.setStatus(StatusEnum.normal.getId());
          }
        }
        elementDataBean.setTime(dataIntime.getUpdatedAt());
        elementDataBean.setData(String.valueOf(dataIntime.getElectric()));
        elementDataBean.setDeviceOneTypeEnum(DeviceTypeEnum.electric);
        elementDataBeanList.add(elementDataBean);
      }
      if (hasOneDeviceType(DeviceTypeEnum.body.getId()) && DeviceTypeEnum.hasType(tmpType, DeviceTypeEnum.body)) {
        ElementDataBean elementDataBean = new ElementDataBean();
        elementDataBean.setDeviceInfo(dataIntime.getDeviceInfo());
        elementDataBean.setArea(dataIntime.getDeviceInfo().getArea());
        elementDataBean.setStatus(dataIntime.getInStatus());
        if(dataIntime.getInStatus() == StatusEnum.alarm.getId()) {
          if (dataIntime.isBodyAlarm()) {
            elementDataBean.setStatus(StatusEnum.alarm.getId());
          } else {
            elementDataBean.setStatus(StatusEnum.normal.getId());
          }
        }
        elementDataBean.setTime(dataIntime.getUpdatedAt());
        elementDataBean.setData(String.valueOf(dataIntime.getBody()));
        elementDataBean.setDeviceOneTypeEnum(DeviceTypeEnum.body);
        elementDataBeanList.add(elementDataBean);
      }
      if (hasOneDeviceType(DeviceTypeEnum.out.getId()) && DeviceTypeEnum.hasType(tmpType, DeviceTypeEnum.out)) {
        ElementDataBean elementDataBean = new ElementDataBean();
        elementDataBean.setDeviceInfo(dataIntime.getDeviceInfo());
        elementDataBean.setArea(dataIntime.getDeviceInfo().getArea());
        elementDataBean.setStatus(dataIntime.getOutStatus());
        elementDataBean.setTime(dataIntime.getUpdatedAt());
        elementDataBean.setData(String.valueOf(dataIntime.getOut()));
        elementDataBean.setDeviceOneTypeEnum(DeviceTypeEnum.out);
        elementDataBeanList.add(elementDataBean);
      }
    }


  }

}