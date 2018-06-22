package com.chengqianyun.eeweb2networkadmin.biz;


import com.chengqianyun.eeweb2networkadmin.biz.bean.export.HistoryListBean;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceDataHistory;
import com.chengqianyun.eeweb2networkadmin.core.utils.UnitUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 聂鹏
 * @version 1.0
 * @date 18/6/22
 */

public class Convert {

  public static List<HistoryListBean> convertHistoryList(List<DeviceDataHistory> list) {
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
      bean.setDeviceName("test" + i);
      bean.setHumi(UnitUtil.changeHumi(history.getHumi()));
      bean.setTemp(UnitUtil.changeTemp(history.getTemp()));
      result.add(bean);
    }

    return result;
  }

}