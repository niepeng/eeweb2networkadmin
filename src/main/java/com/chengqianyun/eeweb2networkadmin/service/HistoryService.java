package com.chengqianyun.eeweb2networkadmin.service;

import com.chengqianyun.eeweb2networkadmin.biz.Convert;
import com.chengqianyun.eeweb2networkadmin.biz.bean.DeviceDataHistoryBean;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceDataHistory;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceInfo;
import com.chengqianyun.eeweb2networkadmin.biz.page.PageResult;
import com.chengqianyun.eeweb2networkadmin.biz.page.PaginationQuery;
import com.chengqianyun.eeweb2networkadmin.core.utils.DateUtil;
import com.chengqianyun.eeweb2networkadmin.core.utils.Tuple2;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author 聂鹏
 * @version 1.0
 * @date 18/4/20
 */

@Service
@Slf4j
public class HistoryService extends BaseService {


  public List<DeviceDataHistoryBean> getHistoryDataAll(PaginationQuery query, int distanceTimeInt, DeviceInfo deviceInfo) {
    List<DeviceDataHistoryBean> result = new ArrayList<DeviceDataHistoryBean>();
    int pageSize = query.getRowsPerPage();


    int currentNum = 0;
    Date startDate = null;
    Date endDate = null;
    DeviceDataHistoryBean tmpBean = null;

    int pageIndex = 1;
    do {
      query.setPageIndex(pageIndex++);
      int startRecord = (query.getPageIndex() - 1) * query.getRowsPerPage();
      query.addQueryData("startRecord", Integer.toString(startRecord));
      query.addQueryData("endRecord", Integer.toString(query.getRowsPerPage()));
      List<DeviceDataHistory> dbList = deviceDataHistoryMapper.findPage(query.getQueryData());


      if (distanceTimeInt == 1) {
        for (DeviceDataHistory bean : dbList) {
          DeviceDataHistoryBean tmpBean1 = Convert.change(bean, deviceInfo.getType());
          result.add(tmpBean1);
        }
        return result;
      }

      if(dbList.size() == 0) {
        break;
      }

      // 第一页查询初始化,否则值就是上一页结束的值
      if(tmpBean == null) {
        startDate = dbList.get(0).getCreatedAt();
        endDate = DateUtil.addMinitue(startDate, distanceTimeInt);
      }

      for (int i = 0, size = dbList.size(); i < size; i++) {
        if (tmpBean == null) {
          tmpBean = Convert.change(dbList.get(0), deviceInfo.getType());
          currentNum++;
          continue;
        }

        if (dbList.get(i).getCreatedAt().getTime() < endDate.getTime()) {
          currentNum++;
          tmpBean.addValue(dbList.get(i));
          continue;
        }

        // 一个小周期结束,计算的结果
        tmpBean.calcAvg(currentNum);
        result.add(tmpBean);

        // 初始化数据,进行下一个周期结果
        currentNum = 1;
        tmpBean = Convert.change(dbList.get(i), deviceInfo.getType());
        startDate = dbList.get(i).getCreatedAt();
        endDate = DateUtil.addMinitue(startDate, distanceTimeInt);
      }


      if(dbList.size() < pageSize) {
        break;
      }

    } while(true);




    return result;

  }

  public PageResult<DeviceDataHistory> historyDataList(PaginationQuery query, DeviceInfo deviceInfo) {
      PageResult<DeviceDataHistory> result = null;
      try {
        Integer count = deviceDataHistoryMapper.findPageCount(query.getQueryData());

        if (null != count && count.intValue() > 0) {
          int startRecord = (query.getPageIndex() - 1) * query.getRowsPerPage();
          query.addQueryData("startRecord", Integer.toString(startRecord));
          query.addQueryData("endRecord", Integer.toString(query.getRowsPerPage()));
          List<DeviceDataHistory> list = deviceDataHistoryMapper.findPage(query.getQueryData());
          if(list != null) {
            for(DeviceDataHistory history : list) {
              history.setDeviceInfo(deviceInfo);
            }
          }
          result = new PageResult<DeviceDataHistory>(list, count, query);
        }
      } catch (Exception e) {
        log.error("HistoryService.getDeviceInfoList,Error", e);
      }
    return result;
  }

  public List<DeviceDataHistory> historyDataList(long deviceId, String startTime, String endTime) {
    Map<String, String> map = new HashMap<String, String>();
    map.put("deviceId", String.valueOf(deviceId));
    map.put("startTime", startTime);
    map.put("endTime", endTime);
    List<DeviceDataHistory> list = deviceDataHistoryMapper.findPageAll(map);
    return list;
  }
}