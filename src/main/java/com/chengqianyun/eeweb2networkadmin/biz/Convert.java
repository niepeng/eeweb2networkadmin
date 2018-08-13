package com.chengqianyun.eeweb2networkadmin.biz;


import com.chengqianyun.eeweb2networkadmin.biz.bean.DeviceDataHistoryBean;
import com.chengqianyun.eeweb2networkadmin.biz.bean.export.HistoryListBean;
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