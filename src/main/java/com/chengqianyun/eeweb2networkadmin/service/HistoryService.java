package com.chengqianyun.eeweb2networkadmin.service;

import com.chengqianyun.eeweb2networkadmin.biz.Convert;
import com.chengqianyun.eeweb2networkadmin.biz.bean.DeviceDataHistoryBean;
import com.chengqianyun.eeweb2networkadmin.biz.bean.ExportBatchDataBean;
import com.chengqianyun.eeweb2networkadmin.biz.bean.export.ExportBatchBean;
import com.chengqianyun.eeweb2networkadmin.biz.bean.export.ExportHelperBean;
import com.chengqianyun.eeweb2networkadmin.biz.bean.export.HeaderContentBean;
import com.chengqianyun.eeweb2networkadmin.biz.bean.export.MarkStyleBean;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.Area;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceDataHistory;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceInfo;
import com.chengqianyun.eeweb2networkadmin.biz.enums.DeviceTypeEnum;
import com.chengqianyun.eeweb2networkadmin.biz.enums.MaxMinEnum;
import com.chengqianyun.eeweb2networkadmin.biz.enums.StatusEnum;
import com.chengqianyun.eeweb2networkadmin.biz.page.PageResult;
import com.chengqianyun.eeweb2networkadmin.biz.page.PaginationQuery;
import com.chengqianyun.eeweb2networkadmin.core.utils.*;

import java.util.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * @author 聂鹏
 * @version 1.0
 * @date 18/4/20
 */

@Service
@Slf4j
public class HistoryService extends BaseService {

  public ExportBatchBean genExportBatchBean(String startTime, String endTime, int distanceTimeInt, String dataTypes, String deviceIds) {
    ExportBatchBean exportHelperBean = new ExportBatchBean();

    DeviceTypeEnum[] exportOptTypeEnums = new DeviceTypeEnum[]{DeviceTypeEnum.temp, DeviceTypeEnum.humi, DeviceTypeEnum.shine, DeviceTypeEnum.pressure};
    List<DeviceInfo> deviceInfoList = findDeviceInfo(deviceIds, exportOptTypeEnums);
    if(CollectionUtils.isEmpty(deviceInfoList)) {
      return exportHelperBean;
    }

    List<DeviceTypeEnum> typeEnums = findDeviceTypes(deviceInfoList, exportOptTypeEnums);
    List<MaxMinEnum> maxMinEnums = findMaxMinEnums(dataTypes);

    /**
     *  温度，湿度，光照，压强: DeviceTypeEnum
     *  平均值，最大值，最小值: MaxMinEnum
     * 展示数据标题部分顺序是：根据温度，湿度，光照，压力
     * 根据开始时间，然后加上间隔，去获取数据，得到最大，最小，平均值
     */
//    String[] headerList = new String[] {"数据类型：温度湿度", "间隔：" + distanceTime , "区域1-涉笔1","区域1-涉笔1","区域1-涉笔2","区域1-涉笔2" };
//    String[] dataHeaderList = new String[]{"NO", "记录时间", "温度平均值(℃)", "湿度平均值(%RH)", "温度平均值(℃)", "湿度平均值(%RH)"};

    String distanceTime = "间隔：" +  (distanceTimeInt < 60 ?  distanceTimeInt + "分钟" : distanceTimeInt/60 + "小时");

    List<String> headerList = Lists.newArrayList();
    headerList.add("类型：" + getDeviceTypeNames(typeEnums));
    headerList.add(distanceTime);

    List<String> dataHeaderList = Lists.newArrayList();
    dataHeaderList.add("NO");
    dataHeaderList.add("记录时间");

    addHeaderAndDataTitle(headerList, dataHeaderList, typeEnums, maxMinEnums, deviceInfoList);


    String sheetName = "设备数据";
    String title = "监控平台历史数据查询结果( " + startTime + " ---- " + endTime + " )";

    exportHelperBean.setSheetName(sheetName);
    exportHelperBean.setTitle(title);
    exportHelperBean.setTitleMergeCol(dataHeaderList.size());
    exportHelperBean.setHeaders(null);

    String[] dataHeaderArray = new String[dataHeaderList.size()];
    dataHeaderList.toArray(dataHeaderArray);
    exportHelperBean.setDataHeaders(dataHeaderArray);
    List<String[]> headersList = Lists.newArrayList();

    String[] headerArray = new String[headerList.size()];
    headerList.toArray(headerArray);
    headersList.add(headerArray);
    exportHelperBean.setHeaders(headersList);

    // 数据处理
    List<String[]> dataValueList = Lists.newArrayList();
    // 标记颜色处理
    Map<String, MarkStyleBean> markStyleBeanMap = Maps.newHashMap();

    Date startDate = DateUtil.getDate(startTime,DateUtil.dateFullPatternNoSecond);
    Date endDate = DateUtil.getDate(endTime,DateUtil.dateFullPatternNoSecond);

    String currentDeviceIds = findDeviceIds(deviceInfoList);
    Date tmpStart = startDate;
    Date tmpEnd = null;
    List<ExportBatchDataBean> currentDataBeanList;
    boolean needBreak = false;
    Map<String, String> dbParams = Maps.newHashMap();
    while(!needBreak) {
      tmpEnd = DateUtil.addMinitue(tmpStart, distanceTimeInt);
      if(!tmpEnd.before(endDate)) {
        tmpEnd = endDate;
        needBreak = true;
      }

      // 取： 【tmpStart,tmpEnd) 这段时间的数据
      dbParams.put("deviceIds", currentDeviceIds);
      dbParams.put("startTime", DateUtil.getDate(tmpStart, DateUtil.dateFullPatternNoSecond));
      dbParams.put("endTime", DateUtil.getDate(tmpEnd, DateUtil.dateFullPatternNoSecond));
      currentDataBeanList = deviceDataHistoryMapper.exportAvgInfo(dbParams);
      if(!CollectionUtils.isEmpty(currentDataBeanList)) {
        optData4export(dataValueList, currentDataBeanList, tmpStart, typeEnums, maxMinEnums, deviceInfoList, markStyleBeanMap);
      }
      dbParams.clear();
      tmpStart = tmpEnd;
    }



//    String[] s1 = new String[] {"2019-06-20","33.56","24.56","25.56","26.56"};
//    dataValueList.add(s1);
    exportHelperBean.setDataValue(dataValueList);
//    MarkStyleBean markStyleBean = new MarkStyleBean(0, 2, 1);
//    markStyleBeanMap.put(markStyleBean.genKey(), markStyleBean);
    exportHelperBean.setMarkStyleBeanMap(markStyleBeanMap);
    return exportHelperBean;
  }



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

  public PageResult<DeviceDataHistory> historyDataList(PaginationQuery query, DeviceInfo deviceInfo, List<Area> areaDeviceList) {
    PageResult<DeviceDataHistory> result = null;
    try {
      Integer count = deviceDataHistoryMapper.findPageCount(query.getQueryData());

      if (null != count && count.intValue() > 0) {
        int startRecord = (query.getPageIndex() - 1) * query.getRowsPerPage();
        query.addQueryData("startRecord", Integer.toString(startRecord));
        query.addQueryData("endRecord", Integer.toString(query.getRowsPerPage()));
        List<DeviceDataHistory> list = deviceDataHistoryMapper.findPage(query.getQueryData());
        if (list != null) {
          for (DeviceDataHistory history : list) {
            if (deviceInfo != null && history.getDeviceId() == deviceInfo.getId()) {
              history.setDeviceInfo(deviceInfo);
              continue;
            }
            history.setDeviceInfo(findDeviceInfo(history.getDeviceId(), areaDeviceList));
            // 设置区域信息
            if (history.getDeviceInfo() != null) {
              history.setArea(areaMapper.selectByPrimaryKey(history.getDeviceInfo().getAreaId()));
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

  private DeviceInfo findDeviceInfo(long deviceId, List<Area> areaList) {
    if (areaList == null) {
      return null;
    }
    List<DeviceInfo> deviceInfoList = null;
    for (Area area : areaList) {
      deviceInfoList = area.getDeviceInfoList();
      if (deviceInfoList == null) {
        continue;
      }
      for (DeviceInfo tmp : deviceInfoList) {
        if (deviceId == tmp.getId()) {
          return tmp;
        }
      }
    }
    return null;
  }

  private List<DeviceInfo> findDeviceInfo(String deviceIds, DeviceTypeEnum[] deviceTypeEnums) {
    if (StringUtil.isEmpty(deviceIds)) {
      return null;
    }

    List<DeviceInfo> list = Lists.newArrayList();
    DeviceInfo deviceInfo;
    for (String tmp : deviceIds.split(BizConstant.SPLIT)) {
      deviceInfo = deviceInfoMapper.selectByPrimaryKey(Long.valueOf(tmp));
      if (deviceInfo == null) {
        continue;
      }

      for (DeviceTypeEnum deviceTypeEnum : deviceTypeEnums) {
        if (DeviceTypeEnum.hasType(deviceInfo.getType(), deviceTypeEnum)) {
          list.add(deviceInfo);
          break;
        }
      }

    }

    for(DeviceInfo tmp : list) {
      tmp.setArea(areaMapper.selectByPrimaryKey(tmp.getAreaId()));
    }

    return list;
  }

  private List<DeviceTypeEnum> findDeviceTypes(List<DeviceInfo> list, DeviceTypeEnum[] exportOptTypeEnums) {
    Map<DeviceTypeEnum, Object> map = Maps.newHashMap();
    for (DeviceInfo deviceInfo : list) {
      for (DeviceTypeEnum tmpEnum : exportOptTypeEnums) {
        if (DeviceTypeEnum.hasType(deviceInfo.getType(), tmpEnum) && !map.containsKey(tmpEnum)) {
          map.put(tmpEnum, null);
          if (map.size() == exportOptTypeEnums.length) {
            break;
          }
        }
      }
      if (map.size() == exportOptTypeEnums.length) {
        break;
      }
    }
    List<DeviceTypeEnum> result =  new ArrayList<DeviceTypeEnum>(map.keySet());
//    result.sort((a,b) -> a.getId() - b.getId());
    result.sort(new Comparator<DeviceTypeEnum>() {
      @Override
      public int compare(DeviceTypeEnum o1, DeviceTypeEnum o2) {
        return o1.getId() - o2.getId();
      }
    });
    return result;
  }

  private List<MaxMinEnum> findMaxMinEnums(String dataTypes) {
    List<MaxMinEnum> list = Lists.newArrayList();
    if (dataTypes.indexOf("avg") >= 0) {
      list.add(MaxMinEnum.avg);
    }
    if (dataTypes.indexOf("max") >= 0) {
      list.add(MaxMinEnum.max);
    }
    if (dataTypes.indexOf("min") >= 0) {
      list.add(MaxMinEnum.min);
    }
    return list;
  }

  private void  addHeaderAndDataTitle(List<String> headerList, List<String> dataHeaderList, List<DeviceTypeEnum> typeEnums, List<MaxMinEnum> maxMinEnums, List<DeviceInfo> deviceInfoList) {
    for (DeviceTypeEnum typeEnum : typeEnums) {
      for (MaxMinEnum maxMinEnum : maxMinEnums) {
        for (DeviceInfo deviceInfo : deviceInfoList) {
          if (!DeviceTypeEnum.hasType(deviceInfo.getType(), typeEnum)) {
            continue;
          }
          headerList.add(deviceInfo.showName());
          dataHeaderList.add(typeEnum.getName() + maxMinEnum.getName() + "(" + typeEnum.getUnit() + ")");
        }
      }

    }

  }

  private String findDeviceIds(List<DeviceInfo> list) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0, size = list.size(); i < size; i++) {
      if(i > 0) {
        sb.append(BizConstant.SPLIT);
      }
      sb.append(String.valueOf(list.get(i).getId()));
    }
    return sb.toString();
  }

  private void optData4export(List<String[]> dataValueList, List<ExportBatchDataBean> currentDataBeanList, Date tmpStart, List<DeviceTypeEnum> typeEnums, List<MaxMinEnum> maxMinEnums, List<DeviceInfo> deviceInfoList, Map<String, MarkStyleBean> markStyleBeanMap) {
    List<String> dataList = Lists.newArrayList();
    dataList.add(DateUtil.getDate(tmpStart, DateUtil.dateFullPatternNoSecond));
    int lineIndex = dataValueList.size();

    Map<Long, ExportBatchDataBean> currentDataBeanMap = Maps.newHashMap();
    for(ExportBatchDataBean tmp : currentDataBeanList) {
      currentDataBeanMap.put(tmp.getDeviceId(), tmp);
    }


    ExportBatchDataBean tmpDataBean;
    for (DeviceTypeEnum typeEnum : typeEnums) {
      for (MaxMinEnum maxMinEnum : maxMinEnums) {
        for (DeviceInfo deviceInfo : deviceInfoList) {
          if (!DeviceTypeEnum.hasType(deviceInfo.getType(), typeEnum)) {
            continue;
          }

          tmpDataBean = currentDataBeanMap.get(deviceInfo.getId());




          if(tmpDataBean == null) {
            dataList.add("--");
            continue;
          }
          if(maxMinEnum == MaxMinEnum.avg && typeEnum == DeviceTypeEnum.temp) {
            optStatusMarkStyleTemp(lineIndex, dataList.size(), tmpDataBean.getAvgTemp(), markStyleBeanMap, deviceInfo);
            dataList.add(trim0(tmpDataBean.getAvgTemp()));
            continue;
          }
          if(maxMinEnum == MaxMinEnum.max && typeEnum == DeviceTypeEnum.temp) {
            optStatusMarkStyleTemp(lineIndex, dataList.size(), tmpDataBean.getMaxTemp(), markStyleBeanMap, deviceInfo);
            dataList.add(trim0(tmpDataBean.getMaxTemp()));
            continue;
          }
          if(maxMinEnum == MaxMinEnum.min && typeEnum == DeviceTypeEnum.temp) {
            optStatusMarkStyleTemp(lineIndex, dataList.size(), tmpDataBean.getMinTemp(), markStyleBeanMap, deviceInfo);
            dataList.add(trim0(tmpDataBean.getMinTemp()));
            continue;
          }


          if(maxMinEnum == MaxMinEnum.avg && typeEnum == DeviceTypeEnum.humi) {
            optStatusMarkStyleHumi(lineIndex, dataList.size(), tmpDataBean.getMinHumi(), markStyleBeanMap, deviceInfo);
            dataList.add(trim0(tmpDataBean.getAvgHumi()));
            continue;
          }
          if(maxMinEnum == MaxMinEnum.max && typeEnum == DeviceTypeEnum.humi) {
            optStatusMarkStyleHumi(lineIndex, dataList.size(), tmpDataBean.getMaxHumi(), markStyleBeanMap, deviceInfo);
            dataList.add(trim0(tmpDataBean.getMaxHumi()));
            continue;
          }
          if(maxMinEnum == MaxMinEnum.min && typeEnum == DeviceTypeEnum.humi) {
            optStatusMarkStyleHumi(lineIndex, dataList.size(), tmpDataBean.getMinHumi(), markStyleBeanMap, deviceInfo);
            dataList.add(trim0(tmpDataBean.getMinHumi()));
            continue;
          }


          if(maxMinEnum == MaxMinEnum.avg && typeEnum == DeviceTypeEnum.shine) {
            optStatusMarkStyleShine(lineIndex, dataList.size(), tmpDataBean.getAvgShine(), markStyleBeanMap, deviceInfo);
            dataList.add(trim0(tmpDataBean.getAvgShine()));
            continue;
          }
          if(maxMinEnum == MaxMinEnum.max && typeEnum == DeviceTypeEnum.shine) {
            optStatusMarkStyleShine(lineIndex, dataList.size(), tmpDataBean.getMaxShine(), markStyleBeanMap, deviceInfo);
            dataList.add(trim0(tmpDataBean.getMaxShine()));
            continue;
          }
          if(maxMinEnum == MaxMinEnum.min && typeEnum == DeviceTypeEnum.shine) {
            optStatusMarkStyleShine(lineIndex, dataList.size(), tmpDataBean.getMinShine(), markStyleBeanMap, deviceInfo);
            dataList.add(trim0(tmpDataBean.getMinShine()));
            continue;
          }

          if(maxMinEnum == MaxMinEnum.avg && typeEnum == DeviceTypeEnum.pressure) {
            optStatusMarkStylePressure(lineIndex, dataList.size(), tmpDataBean.getAvgPressure(), markStyleBeanMap, deviceInfo);
            dataList.add(trim0(tmpDataBean.getAvgPressure()));
            continue;
          }
          if(maxMinEnum == MaxMinEnum.max && typeEnum == DeviceTypeEnum.pressure) {
            optStatusMarkStylePressure(lineIndex, dataList.size(), tmpDataBean.getMaxPressure(), markStyleBeanMap, deviceInfo);
            dataList.add(trim0(tmpDataBean.getMaxPressure()));
            continue;
          }
          if(maxMinEnum == MaxMinEnum.min && typeEnum == DeviceTypeEnum.pressure) {
            optStatusMarkStylePressure(lineIndex, dataList.size(), tmpDataBean.getMinPressure(), markStyleBeanMap, deviceInfo);
            dataList.add(trim0(tmpDataBean.getMinPressure()));
            continue;
          }
        }
      }
    }



    String[] result = new String[dataList.size()];
    dataList.toArray(result);
    dataValueList.add(result);
  }

  private String trim0(Double d) {
    if(d == 0.0) {
      return "--";
    }
    return String.valueOf(d);
  }

  private String getDeviceTypeNames(List<DeviceTypeEnum> list) {
    StringBuilder sb = new StringBuilder();
    for(DeviceTypeEnum tmp : list) {
      sb.append(tmp.getName());
    }
    return sb.toString();
  }

  private void optStatusMarkStyleTemp(int lineIndex, int rowIndex, Double value, Map<String, MarkStyleBean> markStyleBeanMap, DeviceInfo deviceInfo) {
    int min = deviceInfo.getTempDown() + deviceInfo.getTempDev();
    int max = deviceInfo.getTempUp() + deviceInfo.getTempDev();
    StatusEnum tmp = findStatus(value, UnitUtil.chu100Double(min), UnitUtil.chu100Double(max));
    if (tmp == null) {
      return;
    }
    optStatusMarkStyle(lineIndex, rowIndex, markStyleBeanMap, tmp);
  }

  private void optStatusMarkStyleHumi(int lineIndex, int rowIndex, Double value, Map<String, MarkStyleBean> markStyleBeanMap, DeviceInfo deviceInfo) {
    int min = deviceInfo.getHumiDown() + deviceInfo.getHumiDev();
    int max = deviceInfo.getHumiUp() + deviceInfo.getHumiDev();
    StatusEnum tmp = findStatus(value, UnitUtil.chu100Double(min), UnitUtil.chu100Double(max));
    if (tmp == null) {
      return;
    }
    optStatusMarkStyle(lineIndex, rowIndex, markStyleBeanMap, tmp);
  }

  private void optStatusMarkStyleShine(int lineIndex, int rowIndex, Double value, Map<String, MarkStyleBean> markStyleBeanMap, DeviceInfo deviceInfo) {
    int min = deviceInfo.getShineDown() + deviceInfo.getShineDev();
    int max = deviceInfo.getShineUp() + deviceInfo.getShineDev();
    StatusEnum tmp = findStatus(value, min, max);
    if (tmp == null) {
      return;
    }
    optStatusMarkStyle(lineIndex, rowIndex, markStyleBeanMap, tmp);
  }

  private void optStatusMarkStylePressure(int lineIndex, int rowIndex, Double value, Map<String, MarkStyleBean> markStyleBeanMap, DeviceInfo deviceInfo) {
    int min = deviceInfo.getPressureDown() + deviceInfo.getPressureDev();
    int max = deviceInfo.getPressureUp() + deviceInfo.getPressureDev();
    StatusEnum tmp = findStatus(value, min, max);
    if (tmp == null) {
      return;
    }
    optStatusMarkStyle(lineIndex, rowIndex, markStyleBeanMap, tmp);
  }



  private void optStatusMarkStyle(int lineIndex, int rowIndex, Map<String, MarkStyleBean> markStyleBeanMap, StatusEnum tmp) {
    MarkStyleBean markStyleBean = new MarkStyleBean(lineIndex, rowIndex, 0);
    if (StatusEnum.alarm_down == tmp) {
      markStyleBean.setMarkType(1);
    }
    if (StatusEnum.alarm_up == tmp) {
      markStyleBean.setMarkType(2);
    }
    markStyleBeanMap.put(markStyleBean.genKey(), markStyleBean);
  }

  private StatusEnum findStatus(Double value, double min, double max) {
    if (value < min) {
      return StatusEnum.alarm_down;
    }
    if (value > max) {
      return StatusEnum.alarm_up;
    }
    return null;
  }

}