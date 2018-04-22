package com.chengqianyun.eeweb2networkadmin.action;


import com.chengqianyun.eeweb2networkadmin.biz.entitys.Area;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceDataHistory;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceDataIntime;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceInfo;
import com.chengqianyun.eeweb2networkadmin.biz.enums.MenuEnum;
import com.chengqianyun.eeweb2networkadmin.biz.page.PageResult;
import com.chengqianyun.eeweb2networkadmin.biz.page.PaginationQuery;
import com.chengqianyun.eeweb2networkadmin.core.utils.BizConstant;
import com.chengqianyun.eeweb2networkadmin.core.utils.DateUtil;
import com.chengqianyun.eeweb2networkadmin.core.utils.StringUtil;
import com.chengqianyun.eeweb2networkadmin.service.DeviceService;
import com.chengqianyun.eeweb2networkadmin.service.HistoryService;
import java.util.Date;
import java.util.List;
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
      model.addAttribute("areaList", areaList);
      model.addAttribute("startTime", startTime);
      model.addAttribute("endTime", endTime);


      // 检查时间段不能超过一天
      Date startTimeDate = DateUtil.getDate(startTime, DateUtil.dateFullPatternNoSecond);
      Date endTimeDate = DateUtil.getDate(endTime, DateUtil.dateFullPatternNoSecond);
      if(startTimeDate.getTime() > endTimeDate.getTime()) {
        throw new Exception("开始时间不能大于结束时间");
      }
      if(startTimeDate.getTime() + BizConstant.Times.day < endTimeDate.getTime()) {
        throw new Exception("开始时间和结束时间的 间隔不能超过1天");
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

      // 检查时间段不能超过一天
      Date startTimeDate = DateUtil.getDate(startTime, DateUtil.dateFullPatternNoSecond);
      Date endTimeDate = DateUtil.getDate(endTime, DateUtil.dateFullPatternNoSecond);
      if(startTimeDate.getTime() > endTimeDate.getTime()) {
        throw new Exception("开始时间不能大于结束时间");
      }
      if (startTimeDate.getTime() + BizConstant.Times.day < endTimeDate.getTime()) {
        throw new Exception("开始时间和结束时间的 间隔不能超过1天");
      }

      List<DeviceDataHistory> dataHistoryList = historyService.historyDataList(deviceInfo.getId(), startTime, endTime);
      model.addAttribute("dataHistoryList", dataHistoryList);

      return "/history/historyCurveList";
    } catch (Exception ex) {
      model.addAttribute(SUCCESS, false);
      model.addAttribute(MESSAGE, ex.getMessage());
      log.error(ex);
      return "/history/historyCurveList";
    }
  }


}