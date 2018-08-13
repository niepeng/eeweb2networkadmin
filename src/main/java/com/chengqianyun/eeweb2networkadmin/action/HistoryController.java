package com.chengqianyun.eeweb2networkadmin.action;


import com.chengqianyun.eeweb2networkadmin.biz.Convert;
import com.chengqianyun.eeweb2networkadmin.biz.bean.DeviceDataHistoryBean;
import com.chengqianyun.eeweb2networkadmin.biz.bean.export.HeaderContentBean;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.Area;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceDataHistory;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceInfo;
import com.chengqianyun.eeweb2networkadmin.biz.enums.DeviceTypeEnum;
import com.chengqianyun.eeweb2networkadmin.biz.enums.MenuEnum;
import com.chengqianyun.eeweb2networkadmin.biz.page.PageResult;
import com.chengqianyun.eeweb2networkadmin.biz.page.PaginationQuery;
import com.chengqianyun.eeweb2networkadmin.core.utils.BizConstant;
import com.chengqianyun.eeweb2networkadmin.core.utils.DateUtil;
import com.chengqianyun.eeweb2networkadmin.core.utils.ExportExcel;
import com.chengqianyun.eeweb2networkadmin.core.utils.ExportPdf;
import com.chengqianyun.eeweb2networkadmin.core.utils.StringUtil;
import com.chengqianyun.eeweb2networkadmin.core.utils.Tuple2;
import com.chengqianyun.eeweb2networkadmin.core.utils.UnitUtil;
import com.chengqianyun.eeweb2networkadmin.service.DeviceService;
import com.chengqianyun.eeweb2networkadmin.service.HistoryService;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 历史管理
 *
 * @author 聂鹏
 * @version 1.0
 * @date 18/4/12
 */
@Controller
@RequestMapping("/history")
public class HistoryController extends BaseController {

//  historyCurveList

  @Autowired
  private DeviceService deviceService;

  @Autowired
  private HistoryService historyService;

  /**
   * 历史数据
   */
  @RequestMapping(value = "/historyList", method = RequestMethod.GET)
  public String hisotryList(
      @RequestParam(value = "deviceId", required = false, defaultValue = "0") long deviceId,
      @RequestParam(value = "startTime", required = false) String startTime,
      @RequestParam(value = "endTime", required = false) String endTime,
      @RequestParam(value = "distanceTime", defaultValue = "1") String distanceTime,
      @RequestParam(value = "dataTypes", defaultValue = "avg") String dataTypes,
      @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex,
      @RequestParam(value = "pageSize", required = false, defaultValue = "15") int pageSize,
      Model model) {

    try {
      addOptMenu(model, MenuEnum.history);
      List<Area> areaList = deviceService.getAreaAndDeviceInfo();
      DeviceInfo deviceInfo = findOneDeviceId(areaList, deviceId);
      if (StringUtil.isEmpty(startTime) || StringUtil.isEmpty(endTime)) {
        Date now = new Date();
        endTime = DateUtil.getDate(now, DateUtil.dateFullPatternNoSecond);
        startTime = DateUtil.getDate(DateUtil.addDate(now, -1), DateUtil.dateFullPatternNoSecond);
      }

      PaginationQuery query = new PaginationQuery();
      query.setPageIndex(pageIndex);
      query.setRowsPerPage(pageSize);
      query.addQueryData("deviceId", String.valueOf(deviceInfo.getId()));
      query.addQueryData("startTime", startTime);
      query.addQueryData("endTime", endTime);

      model.addAttribute("deviceInfo", deviceInfo);
      model.addAttribute("area", deviceService.getArea(deviceInfo.getAreaId()));
      model.addAttribute("distanceTime", distanceTime);
      model.addAttribute("dataTypes", dataTypes);
      model.addAttribute("areaList", areaList);
      model.addAttribute("startTime", startTime);
      model.addAttribute("endTime", endTime);


      // 检查时间段不能超过7天
      Date startTimeDate = DateUtil.getDate(startTime, DateUtil.dateFullPatternNoSecond);
      Date endTimeDate = DateUtil.getDate(endTime, DateUtil.dateFullPatternNoSecond);
      if(startTimeDate.getTime() > endTimeDate.getTime()) {
        throw new Exception("开始时间不能大于结束时间");
      }

      int distanceTimeInt = Integer.parseInt(distanceTime);
      if (endTimeDate.getTime() - startTimeDate.getTime() >  BizConstant.Times.day * 7 * distanceTimeInt) {
        throw new Exception("开始时间和结束时间的 间隔不能超过" + (7 * distanceTimeInt) + "天");
      }


      PageResult<DeviceDataHistory> params = historyService.historyDataList(query, deviceInfo);
      model.addAttribute("result", params);


      return "/history/historyList";
    } catch(Exception ex) {
      model.addAttribute(SUCCESS, false);
      model.addAttribute(MESSAGE, ex.getMessage());
      log.error(ex);
      return "/history/historyList";
    }
  }



  /**
   * 历史曲线
   */
  @RequestMapping(value = "/historyCurveList", method = RequestMethod.GET)
  public String historyCurveList(
      @RequestParam(value = "deviceId", required = false, defaultValue = "0") long deviceId,
      @RequestParam(value = "startTime", required = false) String startTime,
      @RequestParam(value = "endTime", required = false) String endTime,
      @RequestParam(value = "distanceTime", required = true, defaultValue="1") String distanceTime,  /* 间隔 */
      @RequestParam(value = "dataType", required = true, defaultValue = "avg") String dataType,  /* 统计类型,注意曲线只能单选 */
      Model model) {

    try {
      addOptMenu(model, MenuEnum.history);
      List<Area> areaList = deviceService.getAreaAndDeviceInfo();
      DeviceInfo deviceInfo = findOneDeviceId(areaList, deviceId);
      if (StringUtil.isEmpty(startTime) || StringUtil.isEmpty(endTime)) {
        Date now = new Date();
        endTime = DateUtil.getDate(now, DateUtil.dateFullPatternNoSecond);
        startTime = DateUtil.getDate(DateUtil.addDate(now, -1), DateUtil.dateFullPatternNoSecond);
      }

      model.addAttribute("deviceInfo", deviceInfo);
      model.addAttribute("area", deviceService.getArea(deviceInfo.getAreaId()));
      model.addAttribute("areaList", areaList);
      model.addAttribute("startTime", startTime);
      model.addAttribute("endTime", endTime);
      model.addAttribute("distanceTime", distanceTime);
      model.addAttribute("dataType", dataType);

      int distanceTimeInt = Integer.parseInt(distanceTime);
      // 检查时间段不能超过 7 * distanceTimeInt 天(1分钟是7天,倍数增加)
      Date startTimeDate = DateUtil.getDate(startTime, DateUtil.dateFullPatternNoSecond);
      Date endTimeDate = DateUtil.getDate(endTime, DateUtil.dateFullPatternNoSecond);
      if (startTimeDate.getTime() > endTimeDate.getTime()) {
        throw new Exception("开始时间不能大于结束时间");
      }

      if (endTimeDate.getTime() - startTimeDate.getTime() >  BizConstant.Times.day * 7 * distanceTimeInt) {
        throw new Exception("开始时间和结束时间的 间隔不能超过" + (7 * distanceTimeInt) + "天");
      }

//      List<DeviceDataHistory> dataHistoryList = historyService.historyDataList(deviceInfo.getId(), startTime, endTime);

      PaginationQuery query = new PaginationQuery();
      query.setPageIndex(1);
      query.setRowsPerPage(24 * 60 * 7);
      query.addQueryData("startTime", startTime);
      query.addQueryData("endTime", endTime);
      query.addQueryData("deviceId", String.valueOf(deviceId));
      List<DeviceDataHistoryBean> dataList = historyService.getHistoryDataAll(query, distanceTimeInt, deviceInfo);

//      List<DeviceDataHistory> dataHistoryList = historyService.historyDataList(deviceInfo.getId(), startTime, endTime);
      List<DeviceDataHistory> dataHistoryList = Convert.historyBean2DataByDatatype(dataList, dataType);
      model.addAttribute("dataHistoryList", dataHistoryList);
      addColor(model);

      return "/history/historyCurveList";
    } catch (Exception ex) {
      model.addAttribute(SUCCESS, false);
      model.addAttribute(MESSAGE, ex.getMessage());
      log.error(ex);
      return "/history/historyCurveList";
    }
  }

  // historyListExcel

  @RequestMapping(value = "/historyListExport", method = RequestMethod.GET)
  public String historyListExcel(
      @RequestParam(value = "deviceId", required = false, defaultValue = "0") long deviceId,
      @RequestParam(value = "startTime", required = false) String startTime,
      @RequestParam(value = "endTime", required = false) String endTime,
      @RequestParam(value = "distanceTime", required = false) String distanceTime,  /* 间隔 */
      @RequestParam(value = "dataTypes", required = false) String dataTypes,  /* 统计类型,注意可以多选 */
      /**
       * excel or pdf
       */
      @RequestParam(value = "exportType", required = false) String exportType,
      @RequestParam(required = false, defaultValue = "") String exportFlag,
      HttpServletResponse response,
      Model model) {
    try {
      if (!"true".equals(exportFlag)) {
        throw new Exception("参数错误,请刷新重试");
      }

      if(!"excel".equals(exportType) && !"pdf".equals(exportType)) {
        throw new Exception("参数错误,请刷新重试");
      }


      List<Area> areaList = deviceService.getAreaAndDeviceInfo();
      DeviceInfo deviceInfo = findOneDeviceId(areaList, deviceId);
      if (deviceInfo == null) {
        throw new Exception("请先选择设备");
      }

      if (StringUtil.isEmpty(startTime) || StringUtil.isEmpty(endTime)) {
        Date now = new Date();
        endTime = DateUtil.getDate(now, DateUtil.dateFullPatternNoSecond);
        startTime = DateUtil.getDate(DateUtil.addDate(now, -1), DateUtil.dateFullPatternNoSecond);
      }

      int distanceTimeInt = Integer.parseInt(distanceTime);
      // 检查时间段不能超过7天(1分钟间隔情况,如果间隔多些,那么时间就成正比)
      Date startTimeDate = DateUtil.getDate(startTime, DateUtil.dateFullPatternNoSecond);
      Date endTimeDate = DateUtil.getDate(endTime, DateUtil.dateFullPatternNoSecond);

      if (startTimeDate.getTime() > endTimeDate.getTime()) {
        throw new Exception("开始时间不能大于结束时间");
      }
      if (endTimeDate.getTime() - startTimeDate.getTime() >  BizConstant.Times.day * 7 * distanceTimeInt) {
        throw new Exception("开始时间和结束时间的 间隔不能超过"+(7 * distanceTimeInt)+"天");
      }

      PaginationQuery query = new PaginationQuery();
      query.setPageIndex(1);
      query.setRowsPerPage(24 * 60 * 7);
      query.addQueryData("startTime", startTime);
      query.addQueryData("endTime", endTime);
      query.addQueryData("deviceId", String.valueOf(deviceId));

//      Tuple2< ,List<DeviceDataHistory>> tuple2 =
      List<DeviceDataHistoryBean> dataList = historyService.getHistoryDataAll(query, distanceTimeInt, deviceInfo);

//      List<HistoryListBean> dataList = Convert.convertHistoryList(historyDataList, deviceInfo);

      String fileName = "data_" + DateUtil.getDate(new Date(), DateUtil.PATTERN_YYYYMMDDANDHHMMSS) + ("excel".equals(exportType) ?  ".xls" : ".pdf");
      String title = "监控平台历史数据查询结果";
      HeaderContentBean headerContentBean = new HeaderContentBean();
      headerContentBean.setDeviceInfo(deviceInfo);
      if (deviceInfo.getAreaId() > 0 && areaList != null) {
        for (Area area : areaList) {
          if (deviceInfo.getAreaId() == area.getId().longValue()) {
            headerContentBean.setArea(area);
            break;
          }
        }
      }

      headerContentBean.setStartTime(startTime);
      headerContentBean.setEndTime(endTime);
      headerContentBean.setDistanceTimes( distanceTime + "分钟");
      headerContentBean.setRecordNum(dataList.size());
      setDataMinMax2(dataList, headerContentBean, deviceInfo);
      headerContentBean.calcHeadDataList();


      try {
        OutputStream os = response.getOutputStream();
        response.reset();
        response.setHeader("Content-disposition", "attachment; filename=" + new String(fileName.getBytes("GB2312"), "ISO8859-1"));
        response.setContentType( "pdf".equals(exportType) ? "application/pdf" :  "application/msexcel");// 定义输出类型
        Tuple2<String[], String[]> dataheaderTuple = genDataHeader(deviceInfo, dataTypes);

        if("excel".equals(exportType)) {
          ExportExcel<DeviceDataHistoryBean> exportExcel = new ExportExcel<DeviceDataHistoryBean>();
          exportExcel.exportExcel2(title, headerContentBean,  dataheaderTuple.getT1(),  dataheaderTuple.getT2(), dataList, os);
          return null;
        }

        if("pdf".equals(exportType)) {
//          ExportPdf<HistoryListBean> exportPdf = new ExportPdf<HistoryListBean>();
          ExportPdf<DeviceDataHistoryBean> exportPdf = new ExportPdf<DeviceDataHistoryBean>();
          exportPdf.exportPdf(title, headerContentBean,  dataheaderTuple.getT1(),  dataheaderTuple.getT2(), dataList, os);
          return null;
        }

      } catch (Exception e) {
        log.info("export excel,{}",e);
      }



      return null;
    } catch (Exception ex) {
//      model.addAttribute("realname", realname);
//      model.addAttribute("billCode", billCode);
//      model.addAttribute("investId", investId);
//      model.addAttribute("status", status);
//      model.addAttribute("startTimeStr", startTimeStr);
//      model.addAttribute("endTimeStr", endTimeStr);
//      model.addAttribute("startAmountStr", startAmountStr);
//      model.addAttribute("endAmountStr", endAmountStr);
      model.addAttribute(SUCCESS, false);
      model.addAttribute(MESSAGE, ex.getMessage());
      log.error(ex);
      return "/history/historyList";
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

  private void setDataMinMax(List<DeviceDataHistory>  dataList, HeaderContentBean headerContentBean, DeviceInfo deviceInfo) {
    if (dataList == null || dataList.size() == 0) {
      return;
    }
    int tempMin = dataList.get(0).getTemp();
    int tempMax = dataList.get(0).getTemp();
    int humiMin = dataList.get(0).getHumi();
    int humiMax = dataList.get(0).getHumi();
    int shineMin = dataList.get(0).getShine();
    int shineMax = dataList.get(0).getShine();
    int pressureMin = dataList.get(0).getPressure();
    int pressureMax = dataList.get(0).getPressure();

    boolean hasTemp = DeviceTypeEnum.hasType(deviceInfo.getType(), DeviceTypeEnum.temp);
    boolean hasHumi = DeviceTypeEnum.hasType(deviceInfo.getType(), DeviceTypeEnum.humi);
    boolean hasShine = DeviceTypeEnum.hasType(deviceInfo.getType(), DeviceTypeEnum.shine);
    boolean hasPressure = DeviceTypeEnum.hasType(deviceInfo.getType(), DeviceTypeEnum.pressure);

    DeviceDataHistory tmpData = null;
    for (int i = 0, size = dataList.size(); i < size; i++) {
      tmpData = dataList.get(i);
      if (hasTemp) {
        tempMin = Math.min(tempMin, tmpData.getTemp());
        tempMax = Math.max(tempMax, tmpData.getTemp());
      }

      if (hasHumi) {
        humiMin = Math.min(humiMin, tmpData.getHumi());
        humiMax = Math.max(humiMax, tmpData.getHumi());
      }

      if (hasShine) {
        shineMin = Math.min(shineMin, tmpData.getShine());
        shineMax = Math.max(shineMax, tmpData.getShine());
      }

      if (hasPressure) {
        pressureMin = Math.min(pressureMin, tmpData.getPressure());
        pressureMax = Math.max(pressureMax, tmpData.getPressure());
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


}