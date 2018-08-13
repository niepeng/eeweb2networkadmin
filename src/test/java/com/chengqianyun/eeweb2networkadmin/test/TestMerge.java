package com.chengqianyun.eeweb2networkadmin.test;


import com.chengqianyun.eeweb2networkadmin.biz.Convert;
import com.chengqianyun.eeweb2networkadmin.biz.bean.DeviceDataHistoryBean;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceDataHistory;
import com.chengqianyun.eeweb2networkadmin.core.utils.DateUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 聂鹏
 * @version 1.0
 * @date 18/8/13
 */

public class TestMerge {

  public static void main(String[] args) {
//    List<DeviceDataHisytoryBean> result = new ArrayList<DeviceDataHisytoryBean>();

    List<DeviceDataHistory> argsList = new ArrayList<DeviceDataHistory>();
    DeviceDataHistory bean1 = new DeviceDataHistory();
    bean1.setTemp(123);
    bean1.setCreatedAt(DateUtil.getDate("2018-08-01 10:00:00", "yyyy-MM-dd HH:mm:ss"));

    DeviceDataHistory bean2 = new DeviceDataHistory();
    bean2.setTemp(124);
    bean2.setCreatedAt(DateUtil.getDate("2018-08-01 10:01:00", "yyyy-MM-dd HH:mm:ss"));

    DeviceDataHistory bean3 = new DeviceDataHistory();
    bean3.setTemp(125);
    bean3.setCreatedAt(DateUtil.getDate("2018-08-01 10:02:00", "yyyy-MM-dd HH:mm:ss"));

    DeviceDataHistory bean4 = new DeviceDataHistory();
    bean4.setTemp(130);
    bean4.setCreatedAt(DateUtil.getDate("2018-08-01 10:03:00", "yyyy-MM-dd HH:mm:ss"));

    DeviceDataHistory bean5 = new DeviceDataHistory();
    bean5.setTemp(131);
    bean5.setCreatedAt(DateUtil.getDate("2018-08-01 10:04:00", "yyyy-MM-dd HH:mm:ss"));


    argsList.add(bean1);
    argsList.add(bean2);
    argsList.add(bean3);
    argsList.add(bean4);
    argsList.add(bean5);

    int deviceType = 3;
    List<DeviceDataHistoryBean> result = calc(argsList, 4, deviceType);
    for(DeviceDataHistoryBean bean : result) {
      System.out.println(bean);
    }

  }

  private static List<DeviceDataHistoryBean> calc(List<DeviceDataHistory> argsList, int distanceTime, int deviceType) {
    List<DeviceDataHistoryBean> result = new ArrayList<DeviceDataHistoryBean>();
    if(distanceTime == 1) {
      for (DeviceDataHistory bean : argsList) {
        DeviceDataHistoryBean tmpBean = Convert.change(bean, deviceType);
        result.add(tmpBean);
      }
      return result;
    }


    int currentNum = 0;
    Date startDate = argsList.get(0).getCreatedAt();
    Date endDate = DateUtil.addMinitue(startDate, distanceTime);



    DeviceDataHistoryBean tmpBean = null;
    for (int i = 0, size = argsList.size(); i < size; i++) {
      if(i == 0 && tmpBean == null) {
        tmpBean = Convert.change(argsList.get(i), deviceType);
        currentNum ++;
        continue;
      }

      if(argsList.get(i).getCreatedAt().getTime() < endDate.getTime()) {
        currentNum ++ ;
        tmpBean.addValue(argsList.get(i));
        continue;
      }

      // 一个小周期结束,计算上面的结果
      tmpBean.calcAvg(currentNum);
      result.add(tmpBean);

      currentNum = 1;
      tmpBean = Convert.change(argsList.get(i), deviceType);
      startDate = argsList.get(i).getCreatedAt();
      endDate = DateUtil.addMinitue(startDate, distanceTime);
    }

    return result;
  }
}