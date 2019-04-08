package com.chengqianyun.eeweb2networkadmin.service;

import com.chengqianyun.eeweb2networkadmin.biz.Convert;
import com.chengqianyun.eeweb2networkadmin.biz.bean.DeviceDataHistoryBean;
import com.chengqianyun.eeweb2networkadmin.biz.bean.export.ExportHelperBean;
import com.chengqianyun.eeweb2networkadmin.biz.bean.export.HeaderContentBean;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.Area;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceDataHistory;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceInfo;
import com.chengqianyun.eeweb2networkadmin.biz.enums.DeviceTypeEnum;
import com.chengqianyun.eeweb2networkadmin.biz.page.PageResult;
import com.chengqianyun.eeweb2networkadmin.biz.page.PaginationQuery;
import com.chengqianyun.eeweb2networkadmin.core.utils.DateUtil;
import com.chengqianyun.eeweb2networkadmin.core.utils.Tuple2;
import com.chengqianyun.eeweb2networkadmin.core.utils.UnitUtil;
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


  public ExportHelperBean<DeviceDataHistoryBean> genExportBean(String startTime, String endTime, int distanceTimeInt, String dataTypes, DeviceInfo deviceInfo) {


    PaginationQuery query = new PaginationQuery();
    query.setPageIndex(1);
    query.setRowsPerPage(24 * 60 * 7);
    query.addQueryData("startTime", startTime);
    query.addQueryData("endTime", endTime);
    query.addQueryData("deviceId", String.valueOf(deviceInfo.getId()));

    List<DeviceDataHistoryBean> dataList = getHistoryDataAll(query, distanceTimeInt, deviceInfo);

    String title = "监控平台历史数据查询结果";
    HeaderContentBean headerContentBean = new HeaderContentBean();
    headerContentBean.setDeviceInfo(deviceInfo);
    headerContentBean.setArea(deviceInfo.getAreaId() > 0 ? areaMapper.selectByPrimaryKey(deviceInfo.getAreaId()) : null);

    headerContentBean.setStartTime(startTime);
    headerContentBean.setEndTime(endTime);
    headerContentBean.setDistanceTimes( distanceTimeInt + "分钟");
    headerContentBean.setRecordNum(dataList.size());
    setDataMinMax2(dataList, headerContentBean, deviceInfo);
    headerContentBean.calcHeadDataList();
    Tuple2<String[], String[]> dataheaderTuple = genDataHeader(deviceInfo, dataTypes);


    ExportHelperBean<DeviceDataHistoryBean> exportHelperBean = new ExportHelperBean<DeviceDataHistoryBean>();
    exportHelperBean.setSheetName(deviceInfo.getName() + "("+deviceInfo.getTag()+")");
    exportHelperBean.setTitle(title);
    exportHelperBean.setHeaderContentBean(headerContentBean);
    exportHelperBean.setDataHeaders(dataheaderTuple.getT1());
    exportHelperBean.setDataCols(dataheaderTuple.getT2());
    exportHelperBean.setDataset(dataList);

    return exportHelperBean;

  }


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

  public void deleteByTime(String maxTime) {
    deviceDataHistoryMapper.deleteByTime(maxTime);
  }


  private void setDataMinMax2(List<DeviceDataHistoryBean>  dataList, HeaderContentBean headerContentBean, DeviceInfo deviceInfo) {
    if (dataList == null || dataList.size() == 0) {
      return;
    }
    int tempMin = dataList.get(0).getTempMin();
    int tempMax = dataList.get(0).getTempMax();
    int humiMin = dataList.get(0).getHumiMin();
    int humiMax = dataList.get(0).getHumiMax();
    int shineMin = dataList.get(0).getShineMin();
    int shineMax = dataList.get(0).getShineMax();
    int pressureMin = dataList.get(0).getPressureMin();
    int pressureMax = dataList.get(0).getPressureMax();

    boolean hasTemp = DeviceTypeEnum.hasType(deviceInfo.getType(), DeviceTypeEnum.temp);
    boolean hasHumi = DeviceTypeEnum.hasType(deviceInfo.getType(), DeviceTypeEnum.humi);
    boolean hasShine = DeviceTypeEnum.hasType(deviceInfo.getType(), DeviceTypeEnum.shine);
    boolean hasPressure = DeviceTypeEnum.hasType(deviceInfo.getType(), DeviceTypeEnum.pressure);

    DeviceDataHistoryBean tmpData = null;
    for (int i = 0, size = dataList.size(); i < size; i++) {
      tmpData = dataList.get(i);
      if (hasTemp) {
        tempMin = Math.min(tempMin, tmpData.getTempMin());
        tempMax = Math.max(tempMax, tmpData.getTempMax());
      }

      if (hasHumi) {
        humiMin = Math.min(humiMin, tmpData.getHumiMin());
        humiMax = Math.max(humiMax, tmpData.getHumiMax());
      }

      if (hasShine) {
        shineMin = Math.min(shineMin, tmpData.getShineMin());
        shineMax = Math.max(shineMax, tmpData.getShineMax());
      }

      if (hasPressure) {
        pressureMin = Math.min(pressureMin, tmpData.getPressureMin());
        pressureMax = Math.max(pressureMax, tmpData.getPressureMax());
      }

    }

    if (hasTemp) {
      headerContentBean.setTempMin(UnitUtil.changeTemp(tempMin));
      headerContentBean.setTempMax(UnitUtil.changeTemp(tempMax));
    }
    if (hasHumi) {
      headerContentBean.setHumiMin(UnitUtil.changeHumi(humiMin));
      headerContentBean.setHumiMax(UnitUtil.changeHumi(humiMax));
    }
    if (hasShine) {
      headerContentBean.setShineMin(String.valueOf(shineMin));
      headerContentBean.setShineMax(String.valueOf(shineMax));
    }
    if (hasPressure) {
      headerContentBean.setPressureMin(UnitUtil.changePressure(pressureMin));
      headerContentBean.setPressureMax(UnitUtil.changeTemp(pressureMax));
    }
  }




  /**
   * 返回结果类似如下:
   * new String[]{"NO", "记录时间", "温度(℃)", "湿度(%RH)"},  new String[]{"num", "time", "temp", "humi"}
   * @param deviceInfo
   * @param dataTypes avg,min,max 至少一个
   * @return
   */
  private Tuple2<String[],String[]> genDataHeader(DeviceInfo deviceInfo, String dataTypes) {
    List<String> list1 = new ArrayList<String>();
    List<String> list2 = new ArrayList<String>();

    boolean hasAvg = dataTypes.indexOf("avg") >= 0;
    boolean hasMin = dataTypes.indexOf("min") >= 0;
    boolean hasMax = dataTypes.indexOf("max") >= 0;

    list1.add("NO");
    list1.add("记录时间");

    list2.add("num");
    list2.add("time");
    if (DeviceTypeEnum.hasType(deviceInfo.getType(), DeviceTypeEnum.temp)) {
      if(hasAvg) {
        list1.add(DeviceTypeEnum.temp.getName() + "平均值(" + DeviceTypeEnum.temp.getUnit() + ")");
        list2.add("tempAvgStr");
      }
      if(hasMin) {
        list1.add(DeviceTypeEnum.temp.getName() + "最小值(" + DeviceTypeEnum.temp.getUnit() + ")");
        list2.add("tempMinStr");
      }
      if(hasMax) {
        list1.add(DeviceTypeEnum.temp.getName() + "最大值(" + DeviceTypeEnum.temp.getUnit() + ")");
        list2.add("tempMaxStr");
      }

    }
    if (DeviceTypeEnum.hasType(deviceInfo.getType(), DeviceTypeEnum.humi)) {
      if(hasAvg) {
        list1.add(DeviceTypeEnum.humi.getName() + "平均值(" + DeviceTypeEnum.humi.getUnit() + ")");
        list2.add("humiAvgStr");
      }
      if(hasMin) {
        list1.add(DeviceTypeEnum.humi.getName() + "最小值(" + DeviceTypeEnum.humi.getUnit() + ")");
        list2.add("humiMinStr");
      }
      if(hasMax) {
        list1.add(DeviceTypeEnum.humi.getName() + "最大值(" + DeviceTypeEnum.humi.getUnit() + ")");
        list2.add("humiMaxStr");
      }

    }
    if (DeviceTypeEnum.hasType(deviceInfo.getType(), DeviceTypeEnum.shine)) {
      if(hasAvg) {
        list1.add(DeviceTypeEnum.shine.getName() + "平均值(" + DeviceTypeEnum.shine.getUnit() + ")");
        list2.add("shineAvgStr");
      }
      if(hasMin) {
        list1.add(DeviceTypeEnum.shine.getName() + "最小值(" + DeviceTypeEnum.shine.getUnit() + ")");
        list2.add("shineMinStr");
      }
      if(hasMax) {
        list1.add(DeviceTypeEnum.shine.getName() + "最大值(" + DeviceTypeEnum.shine.getUnit() + ")");
        list2.add("shineMaxStr");
      }

    }
    if (DeviceTypeEnum.hasType(deviceInfo.getType(), DeviceTypeEnum.pressure)) {
      if(hasAvg) {
        list1.add(DeviceTypeEnum.pressure.getName() + "平均值(" + DeviceTypeEnum.pressure.getUnit() + ")");
        list2.add("pressureAvgStr");
      }
      if(hasMin) {
        list1.add(DeviceTypeEnum.pressure.getName() + "最大值(" + DeviceTypeEnum.pressure.getUnit() + ")");
        list2.add("pressureMinStr");
      }
      if(hasMax) {
        list1.add(DeviceTypeEnum.pressure.getName() + "最小值(" + DeviceTypeEnum.pressure.getUnit() + ")");
        list2.add("pressureMaxStr");
      }

    }
    if (DeviceTypeEnum.hasType(deviceInfo.getType(), DeviceTypeEnum.power)) {
      if(hasAvg) {
        list1.add(DeviceTypeEnum.power.getName() + "平均值(" + DeviceTypeEnum.power.getUnit() + ")");
        list2.add("powerAvgStr");
      }
      if(hasMin) {
        list1.add(DeviceTypeEnum.power.getName() + "最小值(" + DeviceTypeEnum.power.getUnit() + ")");
        list2.add("powerMinStr");
      }
      if(hasMax) {
        list1.add(DeviceTypeEnum.power.getName() + "最大值(" + DeviceTypeEnum.power.getUnit() + ")");
        list2.add("powerMaxStr");
      }

    }

    String[] strings1 = new String[list1.size()];
    String[] strings2 = new String[list2.size()];
    list1.toArray(strings1);
    list2.toArray(strings2);
    return new Tuple2<String[], String[]>(strings1, strings2);
  }


}