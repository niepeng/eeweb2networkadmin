package com.chengqianyun.eeweb2networkadmin.biz;


import com.chengqianyun.eeweb2networkadmin.biz.bean.DeviceDataHistoryBean;
import com.chengqianyun.eeweb2networkadmin.biz.bean.ElementDataBean;
import com.chengqianyun.eeweb2networkadmin.biz.bean.api.AreaResp;
import com.chengqianyun.eeweb2networkadmin.biz.bean.api.DeviceDataHistoryResp;
import com.chengqianyun.eeweb2networkadmin.biz.bean.api.DeviceResp;
import com.chengqianyun.eeweb2networkadmin.biz.bean.api.ElementDataResp;
import com.chengqianyun.eeweb2networkadmin.biz.bean.export.HistoryListBean;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.Area;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceDataHistory;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceInfo;
import com.chengqianyun.eeweb2networkadmin.biz.enums.DeviceTypeEnum;
import com.chengqianyun.eeweb2networkadmin.core.utils.DateUtil;
import com.chengqianyun.eeweb2networkadmin.core.utils.UnitUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 聂鹏
 * @version 1.0
 * @date 18/6/22
 */

public class Convert {

  public static List<DeviceDataHistoryBean> convertHistory(List<DeviceDataHistory> list, DeviceInfo deviceInfo) {
    int size;
    if (list == null || (size = list.size()) == 0) {
      return new ArrayList<DeviceDataHistoryBean>();
    }

    List<DeviceDataHistoryBean> result = new ArrayList<DeviceDataHistoryBean>(size);
    DeviceDataHistoryBean bean;
    DeviceDataHistory history;
    for (int i = 0; i < size; i++) {
      history = list.get(i);
      bean = new DeviceDataHistoryBean();
      bean.setNum(String.valueOf(i + 1));
      bean.setTime(DateUtil.getDate(history.getCreatedAt(), DateUtil.dateFullPattern));
//      if (DeviceTypeEnum.hasType(deviceInfo.getType(), DeviceTypeEnum.temp)) {
//        bean.setTempMin(UnitUtil.changeTemp(history.getTemp()));
//      }
//      if (DeviceTypeEnum.hasType(deviceInfo.getType(), DeviceTypeEnum.humi)) {
//        bean.setHumiMin(UnitUtil.changeHumi(history.getHumi()));
//      }
//      if (DeviceTypeEnum.hasType(deviceInfo.getType(), DeviceTypeEnum.shine)) {
//        bean.setShineMin(String.valueOf(history.getShine()));
//      }
//      if (DeviceTypeEnum.hasType(deviceInfo.getType(), DeviceTypeEnum.pressure)) {
//        bean.setPressureMin(UnitUtil.changePressure(history.getPressure()));
//      }
//      if (DeviceTypeEnum.hasType(deviceInfo.getType(), DeviceTypeEnum.power)) {
//        bean.setPowerMin(UnitUtil.changePower(history.getPower()));
//      }
      result.add(bean);
    }

    return result;
  }

  public static DeviceDataHistoryBean change(DeviceDataHistory bean, int deviceType) {
    DeviceDataHistoryBean result = new DeviceDataHistoryBean();
    result.setTime(DateUtil.getDate(bean.getCreatedAt(), DateUtil.dateFullPattern));
    if (DeviceTypeEnum.hasType(deviceType, DeviceTypeEnum.temp)) {
      result.setTempMin(bean.getTemp());
      result.setTempMax(bean.getTemp());
      result.setTempAvg(bean.getTemp());
    }
    if (DeviceTypeEnum.hasType(deviceType, DeviceTypeEnum.humi)) {
      result.setHumiMin(bean.getHumi());
      result.setHumiMax(bean.getHumi());
      result.setHumiAvg(bean.getHumi());
    }
    if (DeviceTypeEnum.hasType(deviceType, DeviceTypeEnum.shine)) {
      result.setShineMax(bean.getShine());
      result.setShineMin(bean.getShine());
      result.setShineAvg(bean.getShine());
    }
    if (DeviceTypeEnum.hasType(deviceType, DeviceTypeEnum.pressure)) {
      result.setPressureMax(bean.getPressure());
      result.setPressureMin(bean.getPressure());
      result.setPressureAvg(bean.getPressure());
    }
    if (DeviceTypeEnum.hasType(deviceType, DeviceTypeEnum.power)) {
      result.setPowerMax(bean.getPower());
      result.setPowerMin(bean.getPower());
      result.setPowerAvg(bean.getPower());
    }

    return result;
  }

  public static List<HistoryListBean> convertHistoryList(List<DeviceDataHistoryBean> list, DeviceInfo deviceInfo) {
    int size;
    if (list == null || (size = list.size()) == 0) {
      return new ArrayList<HistoryListBean>();
    }

    List<HistoryListBean> result = new ArrayList<HistoryListBean>(size);
    HistoryListBean bean;
    DeviceDataHistoryBean history;
    for (int i = 0; i < size; i++) {
      history = list.get(i);
      bean = new HistoryListBean();
      bean.setNum(String.valueOf(i + 1));
//      bean.setTime(DateUtil.getDate(history.getCreatedAt(), DateUtil.dateFullPattern));
//      if (DeviceTypeEnum.hasType(deviceInfo.getType(), DeviceTypeEnum.temp)) {
//        bean.setTemp(UnitUtil.changeTemp(history.getTemp()));
//      }
//      if (DeviceTypeEnum.hasType(deviceInfo.getType(), DeviceTypeEnum.humi)) {
//        bean.setHumi(UnitUtil.changeHumi(history.getHumi()));
//      }
//      if (DeviceTypeEnum.hasType(deviceInfo.getType(), DeviceTypeEnum.shine)) {
//        bean.setShine(String.valueOf(history.getShine()));
//      }
//      if (DeviceTypeEnum.hasType(deviceInfo.getType(), DeviceTypeEnum.pressure)) {
//        bean.setPressure(UnitUtil.changePressure(history.getPressure()));
//      }
//      if (DeviceTypeEnum.hasType(deviceInfo.getType(), DeviceTypeEnum.power)) {
//        bean.setPower(UnitUtil.changePower(history.getPower()));
//      }
      result.add(bean);
    }

    return result;
  }

  public static List<AreaResp> changeAreaList(List<Area> list) {
    List<AreaResp> result = new ArrayList<AreaResp>();
    if (list == null || list.size() == 0) {
      return result;
    }
    for (Area area : list) {
      AreaResp resp = new AreaResp();
      resp.setAreaId(area.getId());
      resp.setAreaName(area.getName());
      resp.setAreaNote(area.getNote());
      result.add(resp);
    }
    return result;
  }

  public static List<DeviceResp> changeDeviceList(List<DeviceInfo> list) {
    List<DeviceResp> result = new ArrayList<DeviceResp>();
    if (list == null || list.size() == 0) {
      return result;
    }
    for (DeviceInfo deviceInfo : list) {
      DeviceResp resp = new DeviceResp();
      resp.setDeviceId(deviceInfo.getId());
      resp.setAreaId(deviceInfo.getAreaId());
      resp.setSn(deviceInfo.getSn());
      resp.setName(deviceInfo.getName());
      resp.setTag(deviceInfo.getTag());
      resp.setAddress(deviceInfo.getAddress());
      resp.setType(deviceInfo.getType());

      resp.setTempUp(UnitUtil.changeTemp(deviceInfo.getTempUp()));
      resp.setTempDown(UnitUtil.changeTemp(deviceInfo.getTempDown()));
      resp.setTempDev(UnitUtil.changeTemp(deviceInfo.getTempDev()));

      resp.setHumiUp(UnitUtil.changeHumi(deviceInfo.getHumiUp()));
      resp.setHumiDown(UnitUtil.changeHumi(deviceInfo.getHumiDown()));
      resp.setHumiDev(UnitUtil.changeHumi(deviceInfo.getHumiDev()));

      resp.setShineUp(String.valueOf(deviceInfo.getShineUp()));
      resp.setShineDown(String.valueOf(deviceInfo.getShineDown()));
      resp.setShineDev(String.valueOf(deviceInfo.getShineDev()));

      resp.setPressureUp(UnitUtil.changePressure(deviceInfo.getPressureUp()));
      resp.setPressureDown(UnitUtil.changePressure(deviceInfo.getPressureDown()));
      resp.setPressureDev(UnitUtil.changePressure(deviceInfo.getPressureDev()));

      result.add(resp);
    }
    return result;
  }

  public static List<ElementDataResp> changeElementDataList(List<ElementDataBean> list) {
    List<ElementDataResp> result = new ArrayList<ElementDataResp>();
    if (list == null || list.size() == 0) {
      return result;
    }

    for (ElementDataBean dataBean : list) {
      ElementDataResp resp = new ElementDataResp();
      resp.setDeviceId(dataBean.getDeviceInfo().getId());
      resp.setAreaId(dataBean.getArea() != null ? dataBean.getArea().getId() : 0);
      resp.setDeviceOneType(dataBean.getDeviceOneTypeEnum().getId());
      resp.setData(dataBean.getData());
      resp.setStatus(dataBean.getStatus());
      resp.setDate(DateUtil.getDate(dataBean.getTime(), DateUtil.dateFullPattern));
      result.add(resp);
    }
    return result;
  }

  public static List<DeviceDataHistoryResp> changeHistoryRespList(List<DeviceDataHistoryBean> beanList, String dataTypes) {
    List<DeviceDataHistoryResp> result = new ArrayList<DeviceDataHistoryResp>();
    if (beanList == null || beanList.size() == 0) {
      return result;
    }

    boolean hasAvg = dataTypes.indexOf("avg") >= 0;
    boolean hasMin = dataTypes.indexOf("min") >= 0;
    boolean hasMax = dataTypes.indexOf("max") >= 0;

    for (DeviceDataHistoryBean dataBean : beanList) {
      DeviceDataHistoryResp resp = new DeviceDataHistoryResp();
      resp.setTime(dataBean.getTime());

      if (hasAvg) {
        resp.setTempAvg(dataBean.getTempAvgStr());
        resp.setHumiAvg(dataBean.getHumiAvgStr());
        resp.setShineAvg(dataBean.getShineAvgStr());
        resp.setPowerAvg(dataBean.getPowerAvgStr());
        resp.setPressureAvg(dataBean.getPressureAvgStr());
      }

      if (hasMin) {
        resp.setTempMin(dataBean.getTempMinStr());
        resp.setHumiMin(dataBean.getHumiMinStr());
        resp.setShineMin(dataBean.getShineMinStr());
        resp.setPowerMin(dataBean.getPowerMinStr());
        resp.setPressureMin(dataBean.getPressureMinStr());
      }

      if (hasMax) {
        resp.setTempMax(dataBean.getTempMaxStr());
        resp.setHumiMax(dataBean.getHumiMaxStr());
        resp.setShineMax(dataBean.getShineMaxStr());
        resp.setPowerMax(dataBean.getPowerMaxStr());
        resp.setPressureMax(dataBean.getPressureMaxStr());
      }
      result.add(resp);
    }
    return result;
  }


  public static List<DeviceDataHistory> historyBean2DataByDatatype(List<DeviceDataHistoryBean> list, String dataType) {
    int size = list.size();
    if (size == 0) {
      return new ArrayList<DeviceDataHistory>();
    }

    boolean isAvg = "avg".equals(dataType);
    boolean isMax = "max".equals(dataType);
    boolean isMin = "min".equals(dataType);

    List<DeviceDataHistory> result = new ArrayList<DeviceDataHistory>(size);
    DeviceDataHistory tmp = null;
    for (DeviceDataHistoryBean bean : list) {
      tmp = new DeviceDataHistory();
      tmp.setCreatedAt(DateUtil.getDate(bean.getTime(), DateUtil.dateFullPatternNoSecond));

      if (isAvg) {
        tmp.setTemp(bean.getTempAvg());
        tmp.setHumi(bean.getHumiAvg());
        tmp.setShine(bean.getShineAvg());
        tmp.setPower(bean.getPowerAvg());
        tmp.setPressure(bean.getPowerAvg());
        result.add(tmp);
        continue;
      }

      if (isMax) {
        tmp.setTemp(bean.getTempMax());
        tmp.setHumi(bean.getHumiMax());
        tmp.setShine(bean.getShineMax());
        tmp.setPower(bean.getPowerMax());
        tmp.setPressure(bean.getPressureMax());
        result.add(tmp);
        continue;
      }

      if (isMin) {
        tmp.setTemp(bean.getTempMin());
        tmp.setHumi(bean.getHumiMin());
        tmp.setShine(bean.getShineMin());
        tmp.setPower(bean.getPowerMin());
        tmp.setPressure(bean.getPressureMin());
        result.add(tmp);
        continue;
      }
    }

    return result;
  }

}