package com.chengqianyun.eeweb2networkadmin.biz.bean;

import com.chengqianyun.eeweb2networkadmin.biz.entitys.Area;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceDataIntime;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceInfo;
import com.chengqianyun.eeweb2networkadmin.biz.enums.DeviceTypeEnum;
import com.chengqianyun.eeweb2networkadmin.biz.enums.StatusEnum;
import com.chengqianyun.eeweb2networkadmin.biz.enums.UpDownEnum;
import com.chengqianyun.eeweb2networkadmin.core.utils.UnitUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
          if (dataIntime.isTempUp()) {
            elementDataBean.setTempStatus(UpDownEnum.up.getId());
          } else if (dataIntime.isTempDown()) {
            elementDataBean.setTempStatus(UpDownEnum.down.getId());
          } else {
            elementDataBean.setStatus(StatusEnum.normal.getId());
          }
        }
        elementDataBean.setTime(dataIntime.getUpdatedAt());
        elementDataBean.setData(UnitUtil.changeTemp(dataIntime.getTemp()));
        elementDataBean.setDeviceOneType(DeviceTypeEnum.temp.getId());
        elementDataBean.setUnit("℃");
        elementDataBeanList.add(elementDataBean);
      }
      if (hasOneDeviceType(DeviceTypeEnum.humi.getId()) && DeviceTypeEnum.hasType(tmpType, DeviceTypeEnum.humi)) {
        ElementDataBean elementDataBean = new ElementDataBean();
        elementDataBean.setDeviceInfo(dataIntime.getDeviceInfo());
        elementDataBean.setArea(dataIntime.getDeviceInfo().getArea());
        elementDataBean.setStatus(dataIntime.getStatus());
        if(dataIntime.getStatus() == StatusEnum.alarm.getId()) {
          if (dataIntime.isHumiUp()) {
            elementDataBean.setHumiStatus(UpDownEnum.up.getId());
          } else if (dataIntime.isHumiDown()) {
            elementDataBean.setHumiStatus(UpDownEnum.down.getId());
          } else {
            elementDataBean.setStatus(StatusEnum.normal.getId());
          }
        }
        elementDataBean.setTime(dataIntime.getUpdatedAt());
        elementDataBean.setData(UnitUtil.changeHumi(dataIntime.getHumi()));
        elementDataBean.setDeviceOneType(DeviceTypeEnum.humi.getId());
        elementDataBean.setUnit("%RH");
        elementDataBeanList.add(elementDataBean);
      }
      if (hasOneDeviceType(DeviceTypeEnum.power.getId()) && DeviceTypeEnum.hasType(tmpType, DeviceTypeEnum.power)) {
        ElementDataBean elementDataBean = new ElementDataBean();
        elementDataBean.setDeviceInfo(dataIntime.getDeviceInfo());
        elementDataBean.setArea(dataIntime.getDeviceInfo().getArea());
        elementDataBean.setStatus(dataIntime.getStatus());
        if(dataIntime.getStatus() == StatusEnum.alarm.getId()) {
//          elementDataBean.setStatus(dataIntime.isShineUp() ? UpDownEnum.up.getId() : UpDownEnum.down.getId());
        }
        elementDataBean.setTime(dataIntime.getUpdatedAt());
        elementDataBean.setData(UnitUtil.changePower(dataIntime.getPower()));
        elementDataBean.setDeviceOneType(DeviceTypeEnum.power.getId());
        elementDataBean.setUnit("V");
        elementDataBeanList.add(elementDataBean);
      }
      if (hasOneDeviceType(DeviceTypeEnum.shine.getId()) && DeviceTypeEnum.hasType(tmpType, DeviceTypeEnum.shine)) {
        ElementDataBean elementDataBean = new ElementDataBean();
        elementDataBean.setDeviceInfo(dataIntime.getDeviceInfo());
        elementDataBean.setArea(dataIntime.getDeviceInfo().getArea());
        elementDataBean.setStatus(dataIntime.getStatus());
        if(dataIntime.getStatus() == StatusEnum.alarm.getId()) {
          if (dataIntime.isShineUp()) {
            elementDataBean.setShineStatus(UpDownEnum.up.getId());
          } else if (dataIntime.isShineDown()) {
            elementDataBean.setShineStatus(UpDownEnum.down.getId());
          } else {
            elementDataBean.setStatus(StatusEnum.normal.getId());
          }
        }
        elementDataBean.setTime(dataIntime.getUpdatedAt());
        elementDataBean.setData(String.valueOf(dataIntime.getShine()));
        elementDataBean.setDeviceOneType(DeviceTypeEnum.shine.getId());
        elementDataBean.setUnit("Lx");
        elementDataBeanList.add(elementDataBean);
      }
      if (hasOneDeviceType(DeviceTypeEnum.pressure.getId()) && DeviceTypeEnum.hasType(tmpType, DeviceTypeEnum.pressure)) {
        ElementDataBean elementDataBean = new ElementDataBean();
        elementDataBean.setDeviceInfo(dataIntime.getDeviceInfo());
        elementDataBean.setArea(dataIntime.getDeviceInfo().getArea());
        elementDataBean.setStatus(dataIntime.getStatus());
        if(dataIntime.getStatus() == StatusEnum.alarm.getId()) {
          if (dataIntime.isPressureUp()) {
            elementDataBean.setPressureStatus(UpDownEnum.up.getId());
          } else if (dataIntime.isPressureDown()) {
            elementDataBean.setPressureStatus(UpDownEnum.down.getId());
          } else {
            elementDataBean.setStatus(StatusEnum.normal.getId());
          }
        }
        elementDataBean.setTime(dataIntime.getUpdatedAt());
        elementDataBean.setData(UnitUtil.changePressure(dataIntime.getPressure()));
        elementDataBean.setDeviceOneType(DeviceTypeEnum.pressure.getId());
        elementDataBean.setUnit("Pa");
        elementDataBeanList.add(elementDataBean);
      }
      if (hasOneDeviceType(DeviceTypeEnum.smoke.getId()) && DeviceTypeEnum.hasType(tmpType, DeviceTypeEnum.smoke)) {
        ElementDataBean elementDataBean = new ElementDataBean();
        elementDataBean.setDeviceInfo(dataIntime.getDeviceInfo());
        elementDataBean.setArea(dataIntime.getDeviceInfo().getArea());
        elementDataBean.setStatus(dataIntime.getStatus());
        elementDataBean.setTime(dataIntime.getUpdatedAt());
        elementDataBean.setData(String.valueOf(dataIntime.getSmoke()));
        elementDataBean.setDeviceOneType(DeviceTypeEnum.smoke.getId());
        elementDataBeanList.add(elementDataBean);
      }
      if (hasOneDeviceType(DeviceTypeEnum.water.getId()) && DeviceTypeEnum.hasType(tmpType, DeviceTypeEnum.water)) {
        ElementDataBean elementDataBean = new ElementDataBean();
        elementDataBean.setDeviceInfo(dataIntime.getDeviceInfo());
        elementDataBean.setArea(dataIntime.getDeviceInfo().getArea());
        elementDataBean.setStatus(dataIntime.getStatus());
        elementDataBean.setTime(dataIntime.getUpdatedAt());
        elementDataBean.setData(String.valueOf(dataIntime.getWater()));
        elementDataBean.setDeviceOneType(DeviceTypeEnum.water.getId());
        elementDataBeanList.add(elementDataBean);
      }
      if (hasOneDeviceType(DeviceTypeEnum.electric.getId()) && DeviceTypeEnum.hasType(tmpType, DeviceTypeEnum.electric)) {
        ElementDataBean elementDataBean = new ElementDataBean();
        elementDataBean.setDeviceInfo(dataIntime.getDeviceInfo());
        elementDataBean.setArea(dataIntime.getDeviceInfo().getArea());
        elementDataBean.setStatus(dataIntime.getStatus());
        elementDataBean.setTime(dataIntime.getUpdatedAt());
        elementDataBean.setData(String.valueOf(dataIntime.getElectric()));
        elementDataBean.setDeviceOneType(DeviceTypeEnum.electric.getId());
        elementDataBeanList.add(elementDataBean);
      }
      if (hasOneDeviceType(DeviceTypeEnum.body.getId()) && DeviceTypeEnum.hasType(tmpType, DeviceTypeEnum.body)) {
        ElementDataBean elementDataBean = new ElementDataBean();
        elementDataBean.setDeviceInfo(dataIntime.getDeviceInfo());
        elementDataBean.setArea(dataIntime.getDeviceInfo().getArea());
        elementDataBean.setStatus(dataIntime.getStatus());
        elementDataBean.setTime(dataIntime.getUpdatedAt());
        elementDataBean.setData(String.valueOf(dataIntime.getBody()));
        elementDataBean.setDeviceOneType(DeviceTypeEnum.body.getId());
        elementDataBeanList.add(elementDataBean);
      }
      if (hasOneDeviceType(DeviceTypeEnum.out.getId()) && DeviceTypeEnum.hasType(tmpType, DeviceTypeEnum.out)) {
        ElementDataBean elementDataBean = new ElementDataBean();
        elementDataBean.setDeviceInfo(dataIntime.getDeviceInfo());
        elementDataBean.setArea(dataIntime.getDeviceInfo().getArea());
        elementDataBean.setStatus(dataIntime.getStatus());
        elementDataBean.setTime(dataIntime.getUpdatedAt());
        elementDataBean.setData(String.valueOf(dataIntime.getOut()));
        elementDataBean.setDeviceOneType(DeviceTypeEnum.out.getId());
        elementDataBeanList.add(elementDataBean);
      }
    }


  }

}