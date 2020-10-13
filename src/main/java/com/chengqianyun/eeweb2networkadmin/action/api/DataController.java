package com.chengqianyun.eeweb2networkadmin.action.api;

/**
 * @author 聂鹏
 * @version 1.0
 * @date 18/12/14
 */

import com.alibaba.fastjson.JSONObject;
import com.chengqianyun.eeweb2networkadmin.action.BaseController;
import com.chengqianyun.eeweb2networkadmin.biz.Convert;
import com.chengqianyun.eeweb2networkadmin.biz.bean.DataIntimeBean;
import com.chengqianyun.eeweb2networkadmin.biz.bean.DeviceDataHistoryBean;
import com.chengqianyun.eeweb2networkadmin.biz.bean.ElementDataBean;
import com.chengqianyun.eeweb2networkadmin.biz.bean.api.ApiResp;
import com.chengqianyun.eeweb2networkadmin.biz.bean.api.DeviceDataHistoryResp;
import com.chengqianyun.eeweb2networkadmin.biz.bean.api.DeviceResp;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.AreaMapper;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceInfo;
import com.chengqianyun.eeweb2networkadmin.biz.page.PageResult;
import com.chengqianyun.eeweb2networkadmin.biz.page.PaginationQuery;
import com.chengqianyun.eeweb2networkadmin.core.utils.BizConstant;
import com.chengqianyun.eeweb2networkadmin.core.utils.BizConstant.ApiCode;
import com.chengqianyun.eeweb2networkadmin.core.utils.DateUtil;
import com.chengqianyun.eeweb2networkadmin.core.utils.StringUtil;
import com.chengqianyun.eeweb2networkadmin.core.utils.Tuple2;
import com.chengqianyun.eeweb2networkadmin.service.DeviceIntimeService;
import com.chengqianyun.eeweb2networkadmin.service.DeviceService;
import com.chengqianyun.eeweb2networkadmin.service.HistoryService;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author 聂鹏
 * @version 1.0
 * @date 18/12/14
 */
@Controller
@RequestMapping("/api/data")
public class DataController extends BaseController {

  @Autowired
  private AreaMapper areaMapper;

  @Autowired
  private DeviceService deviceService;

  @Autowired
  private DeviceIntimeService deviceIntimeService;

  @Autowired
  private HistoryService historyService;


  @ResponseBody
  @RequestMapping(value = "/areaList", method = RequestMethod.GET)
  public ApiResp areaList(@RequestHeader(value = "Authorization") String token) {
    Tuple2<Boolean, ApiResp> tuple = checkToken(token);
    if (!tuple.getT1()) {
      return tuple.getT2();
    }
    ApiResp resp = new ApiResp();
    resp.setData(Convert.changeAreaList(areaMapper.listAll()));
    return resp;
  }


  @ResponseBody
  @RequestMapping(value = "/deviceList", method = RequestMethod.GET)
  public ApiResp deviceList(@RequestHeader(value = "Authorization") String token, @RequestParam(value = "areaId", required = false) String areaId) {
    Tuple2<Boolean, ApiResp> tuple = checkToken(token);
    if (!tuple.getT1()) {
      return tuple.getT2();
    }
    ApiResp resp = new ApiResp();
    PaginationQuery query = new PaginationQuery();
    query.setPageIndex(1);
    query.setRowsPerPage(500);
    query.addQueryData("areaId", areaId);
    PageResult<DeviceInfo> params = deviceService.getDeviceInfoList(query);
    if (params != null) {
      List<DeviceInfo> deviceList = params.getRows();
      resp.setData(Convert.changeDeviceList(deviceList));
    } else {
      resp.setData(new ArrayList<DeviceResp>());
    }
    return resp;
  }

//  deviceTypes
//      areaIds


  @ResponseBody
  @RequestMapping(value = "/intime", method = RequestMethod.GET)
  public ApiResp intime(@RequestHeader(value = "Authorization") String token,
      @RequestParam(value = "deviceTypes", required = false) String deviceTypes,
      @RequestParam(value = "deviceIds", required = false) String deviceIds,
      @RequestParam(value = "areaIds", required = false) String areaIds) {

    Tuple2<Boolean, ApiResp> tuple = checkToken(token);
    if (!tuple.getT1()) {
      return tuple.getT2();
    }

    DataIntimeBean dataIntimeBean = new DataIntimeBean();
    dataIntimeBean.setDeviceTypes(deviceTypes);
    dataIntimeBean.setAreaIds(areaIds);
    if (!StringUtil.isEmpty(deviceIds)) {
      Map<Long, Object> deviceIdMap = new HashMap<Long, Object>();
      for (String s : deviceIds.split(",")) {
        deviceIdMap.put(Long.valueOf(s), null);
      }
      dataIntimeBean.setDeviceIdMap(deviceIdMap);
    }
    deviceIntimeService.deviceDataIntime(dataIntimeBean);
    List<ElementDataBean> list = dataIntimeBean.getElementDataBeanList();

    ApiResp resp = new ApiResp();
    resp.setData(Convert.changeElementDataList(list));
    return resp;
  }


  @ResponseBody
  @RequestMapping(value = "/historyList", method = RequestMethod.GET)
  public ApiResp history(@RequestHeader(value = "Authorization") String token,
      @RequestParam(value = "deviceId", required = false, defaultValue = "") String deviceId,
      @RequestParam(value = "startTime", required = false, defaultValue = "") String startTime,
      @RequestParam(value = "endTime", required = false, defaultValue = "") String endTime,
      @RequestParam(value = "distanceTime", required = false, defaultValue = "1") String distanceTime,
      @RequestParam(value = "dataTypes", defaultValue = "avg") String dataTypes,
      @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex,
      @RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize
  ) {

    Tuple2<Boolean, ApiResp> tuple = checkToken(token);
    if (!tuple.getT1()) {
      return tuple.getT2();
    }

    if (StringUtil.isEmpty(deviceId)) {
      return defaultApiError("请先指定设备id", ApiCode.empty);
    }

    DeviceInfo deviceInfo = deviceService.getDeviceInfo(Long.valueOf(deviceId));
    if (deviceInfo == null) {
      return defaultApiError("当前设备不存在", ApiCode.check_fail);
    }

    Date starDate = DateUtil.getDate(startTime, DateUtil.dateFullPatternNoSecond);
    Date endDate = DateUtil.getDate(endTime, DateUtil.dateFullPatternNoSecond);

    if (starDate == null || endDate == null) {
      return defaultApiError("开始时间和结束时间不能为空或格式错误", ApiCode.check_fail);
    }
    if (starDate.getTime() > endDate.getTime()) {
      return defaultApiError("开始时间不能大于结束时间", ApiCode.check_fail);
    }

    int distanceTimeInt = 1;
    if (!StringUtil.isEmpty(distanceTime)) {
      distanceTimeInt = Integer.parseInt(distanceTime);
    }

    if (distanceTimeInt > 360) {
      return defaultApiError("间隔时间超出范围", ApiCode.check_fail);
    }

    if (endDate.getTime() - starDate.getTime() > BizConstant.Times.day * 7 * distanceTimeInt) {
      return defaultApiError("所选时间段超出查询范围", ApiCode.check_fail);
    }

    if (pageSize > 100) {
      return defaultApiError("每页大小超出范围", ApiCode.check_fail);
    }

    ApiResp resp = new ApiResp();

    PaginationQuery query = new PaginationQuery();
    query.setPageIndex(pageIndex);
    query.setRowsPerPage(pageSize);
    query.addQueryData("deviceId", String.valueOf(deviceInfo.getId()));
    query.addQueryData("startTime", startTime);
    query.addQueryData("endTime", endTime);

    // 获取出所有数据,然后根据筛选条件获取需要的部分
    Tuple2<List<DeviceDataHistoryBean>, Integer> beanList = historyService.getHistoryDataAllForApi(query, distanceTimeInt, deviceInfo, starDate, endDate);
//    List<DeviceDataHistoryResp> dataList = subList(Convert.changeHistoryRespList(beanList, dataTypes), pageIndex, pageSize);

    JSONObject jsonObject = new JSONObject();
    jsonObject.put("totalSize", beanList.getT2() != null ? beanList.getT2() : 0);
    jsonObject.put("historyDataList", beanList.getT1());
    jsonObject.put("pageIndex", pageIndex);
    jsonObject.put("pageSize", pageSize);

    resp.setData(jsonObject);
    return resp;
  }

  private List<DeviceDataHistoryResp> subList(List<DeviceDataHistoryResp> list, int pageIndex, int pageSize) {
    int totalNum = list.size();

    int fromIndex = (pageIndex - 1) * pageSize;
    int endIndex = pageIndex * pageSize - 1;

    if (totalNum > endIndex) {
      return list.subList(fromIndex, endIndex + 1);
    }
    if (totalNum <= fromIndex) {
      return new ArrayList<DeviceDataHistoryResp>();
    }
    return list.subList(fromIndex, totalNum);

  }


}