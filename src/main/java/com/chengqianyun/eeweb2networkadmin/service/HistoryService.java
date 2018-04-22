package com.chengqianyun.eeweb2networkadmin.service;

import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceDataHistory;
import com.chengqianyun.eeweb2networkadmin.biz.page.PageResult;
import com.chengqianyun.eeweb2networkadmin.biz.page.PaginationQuery;
import java.util.ArrayList;
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

    public PageResult<DeviceDataHistory> historyDataList(PaginationQuery query) {
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
              history.setDeviceInfo(deviceInfoMapper.selectByPrimaryKey(history.getDeviceId()));
              if(history.getDeviceInfo() != null && history.getDeviceInfo().getAreaId() > 0) {
                history.getDeviceInfo().setArea(areaMapper.selectByPrimaryKey(history.getDeviceInfo().getAreaId()));
              }
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