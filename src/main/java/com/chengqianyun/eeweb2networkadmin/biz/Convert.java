package com.chengqianyun.eeweb2networkadmin.biz;


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

  public static List<HistoryListBean> convertHistoryList(List<DeviceDataHistory> list, DeviceInfo deviceInfo) {
    int size;
    if (list == null || (size = list.size()) == 0) {
      return new ArrayList<HistoryListBean>();
    }

    List<HistoryListBean> result = new ArrayList<HistoryListBean>(size);
    HistoryListBean bean;
    DeviceDataHistory history;
    for (int i = 0; i < size; i++) {
      history = list.get(i);
      bean = new HistoryListBean();
      bean.setNum(String.valueOf(i + 1));
      bean.setTime(DateUtil.getDate(history.getCreatedAt(), DateUtil.dateFullPattern));
      if (DeviceTypeEnum.hasType(deviceInfo.getType(), DeviceTypeEnum.temp)) {
        bean.setTemp(UnitUtil.changeTemp(history.getTemp()));
      }
      if (DeviceTypeEnum.hasType(deviceInfo.getType(), DeviceTypeEnum.humi)) {
        bean.setHumi(UnitUtil.changeHumi(history.getHumi()));
      }
      if (DeviceTypeEnum.hasType(deviceInfo.getType(), DeviceTypeEnum.shine)) {
        bean.setShine(String.valueOf(history.getShine()));
      }
      if (DeviceTypeEnum.hasType(deviceInfo.getType(), DeviceTypeEnum.pressure)) {
        bean.setPressure(UnitUtil.changePressure(history.getPressure()));
      }
      if (DeviceTypeEnum.hasType(deviceInfo.getType(), DeviceTypeEnum.power)) {
        bean.setPower(UnitUtil.changePower(history.getPower()));
      }
      result.add(bean);
    }

    return result;
  }

}