package com.chengqianyun.eeweb2networkadmin.action;


import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceInfo;
import com.chengqianyun.eeweb2networkadmin.biz.enums.AlarmTypeEnum;
import com.chengqianyun.eeweb2networkadmin.biz.enums.MenuEnum;
import com.chengqianyun.eeweb2networkadmin.biz.page.PageResult;
import com.chengqianyun.eeweb2networkadmin.biz.page.PaginationQuery;
import com.chengqianyun.eeweb2networkadmin.service.AlarmService;
import com.chengqianyun.eeweb2networkadmin.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 报警记录
 *
 * @author 聂鹏
 * @version 1.0
 * @date 18/4/12
 */
@Controller
@RequestMapping("/alarm")
public class AlarmController extends BaseController {


  @Autowired
  private DeviceService deviceService;

  @Autowired
  private AlarmService alarmService;

  /**
   * 报警记录
   */
  @RequestMapping(value = "/alarmList", method = RequestMethod.GET)
  public String alarmList(
      @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex,
      @RequestParam(value = "pageSize", required = false, defaultValue = "15") int pageSize,
      @RequestParam(value = "deviceIds", required = false, defaultValue = "") String deviceIds,
      // AlarmTypeEnum
      @RequestParam(value = "alarmTypes", required = false, defaultValue = "1") String alarmTypes,
      @RequestParam(value = "alarmUnits", required = false, defaultValue = "") String alarmUnits,
      Model model) {
    try {
      addOptMenu(model, MenuEnum.alarm);
      PaginationQuery query = new PaginationQuery();
      query.setPageIndex(pageIndex);
      query.setRowsPerPage(pageSize);
//      query.addQueryData("areaId", areaId);
//      query.addQueryData("name", name);

      PageResult<DeviceInfo> params = deviceService.getDeviceInfoList(query);
      model.addAttribute("result", params);
      model.addAttribute("areaList", deviceService.getAreaAll());
//      model.addAttribute("areaId", areaId);
//      model.addAttribute("name", name);

      return "/alarm/alarmList";
    } catch (Exception ex) {
      model.addAttribute(SUCCESS, false);
      model.addAttribute(MESSAGE, ex.getMessage());
      log.error(ex);
      return "/alarm/alarmList";
    }
  }
}