package com.chengqianyun.eeweb2networkadmin.action;


import com.chengqianyun.eeweb2networkadmin.biz.HdConstant;
import com.chengqianyun.eeweb2networkadmin.biz.bean.AlarmNoteBean;
import com.chengqianyun.eeweb2networkadmin.biz.bean.DeviceFormBean;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceAlarm;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceInfo;
import com.chengqianyun.eeweb2networkadmin.biz.enums.AlarmTypeEnum;
import com.chengqianyun.eeweb2networkadmin.biz.enums.MenuEnum;
import com.chengqianyun.eeweb2networkadmin.biz.page.PageResult;
import com.chengqianyun.eeweb2networkadmin.biz.page.PaginationQuery;
import com.chengqianyun.eeweb2networkadmin.core.utils.DateUtil;
import com.chengqianyun.eeweb2networkadmin.core.utils.StringUtil;
import com.chengqianyun.eeweb2networkadmin.service.AlarmService;
import com.chengqianyun.eeweb2networkadmin.service.DeviceService;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
      @RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize,
      @RequestParam(value = "deviceName", required = false, defaultValue = "") String deviceName,
      // AlarmTypeEnum
      @RequestParam(value = "alarmType", required = false, defaultValue = "") String alarmType,
      // AlarmConfirmEnum
      @RequestParam(value = "alarmConfirm", required = false, defaultValue = "") String alarmConfirm,
      @RequestParam(value = "startTime", required = false) String startTime,
      @RequestParam(value = "endTime", required = false) String endTime,
      Model model) {
    try {
      startTime = StringUtil.isEmpty(startTime)? (String)model.asMap().get("startTime"): startTime;
      endTime = StringUtil.isEmpty(endTime)? (String)model.asMap().get("endTime"): endTime;
      if(StringUtil.isEmpty(startTime) && StringUtil.isEmpty(endTime)) {
        Date now = new Date();
        startTime = DateUtil.getDate(DateUtil.addDate(now,-7),DateUtil.dateFullPattern);
        endTime = DateUtil.getDate(now, DateUtil.dateFullPattern);
      }

      addOptMenu(model, MenuEnum.alarm);
      PaginationQuery query = new PaginationQuery();
      query.setPageIndex(pageIndex);
      query.setRowsPerPage(pageSize);
      query.addQueryData("deviceName", deviceName);
      query.addQueryData("alarmType", alarmType);
      query.addQueryData("alarmConfirm", alarmConfirm);
      query.addQueryData("startTime", startTime);
      query.addQueryData("endTime", endTime);

      model.addAttribute("areaList", deviceService.getAreaAll());
      model.addAttribute("deviceName", deviceName);
      model.addAttribute("alarmType", alarmType);
      model.addAttribute("alarmConfirm", alarmConfirm);
      model.addAttribute("startTime", startTime);
      model.addAttribute("endTime", endTime);

      Date startTimeDate = DateUtil.getDate(startTime, DateUtil.dateFullPattern);
      Date endTimeDate = DateUtil.getDate(endTime, DateUtil.dateFullPattern);
      if(startTimeDate.getTime() > endTimeDate.getTime()) {
        throw new Exception("开始时间不能大于结束时间");
      }

      PageResult<DeviceAlarm> params = alarmService.getAlarmList(query);
      model.addAttribute("result", params);

      return "/alarm/alarmList";
    } catch (Exception ex) {
      model.addAttribute(SUCCESS, false);
      model.addAttribute(MESSAGE, ex.getMessage());
      log.error(ex);
      return "/alarm/alarmList";
    }
  }

  /**
   * 标记已读
   */
  @RequestMapping(value = "/markRead", method = RequestMethod.GET)
  public String markRead(
      @RequestParam(value = "id", required = false, defaultValue = "0") long alarmId,
      @RequestParam(value = "startTime", required = false, defaultValue = "") String startTime,
      @RequestParam(value = "endTime", required = false, defaultValue = "") String endTime,
      Model model, RedirectAttributes redirectAttributes) {

    try {
      alarmService.markRead(alarmId, null);

      redirectAttributes.addFlashAttribute("startTime", startTime);
      redirectAttributes.addFlashAttribute("endTime", endTime);
      redirectAttributes.addFlashAttribute(SUCCESS, true);
      redirectAttributes.addFlashAttribute(MESSAGE, HdConstant.MESSAGE_RECORD_OPERATE_SUCESS);

      return "redirect:/alarm/alarmList";
    } catch (Exception ex) {
      redirectAttributes.addFlashAttribute(SUCCESS, false);
      redirectAttributes.addFlashAttribute(MESSAGE, ex.getMessage());
      log.error(ex);
      return "redirect:/alarm/alarmList";
    }
  }


  /**
   * 并记录备注
   */
  @RequestMapping(value = "/writeNote", method = RequestMethod.POST)
  public String doDeviceAdd(AlarmNoteBean alarmNoteBean, Model model, RedirectAttributes redirectAttributes) {
    try {
      alarmService.markRead(alarmNoteBean.getAlarmId(), alarmNoteBean.getUserNote());
      redirectAttributes.addFlashAttribute(SUCCESS, true);
      redirectAttributes.addFlashAttribute(MESSAGE, HdConstant.MESSAGE_RECORD_OPERATE_SUCESS);
      return "redirect:/alarm/alarmList";
    } catch (Exception ex) {
      redirectAttributes.addFlashAttribute(SUCCESS, false);
      redirectAttributes.addFlashAttribute(MESSAGE, ex.getMessage());
      log.error(ex);
      return "redirect:/alarm/alarmList";
    }
  }


  /**
   * 报警管理:删除
   */
  @RequestMapping(value = "/deleteAlarm", method = RequestMethod.GET)
  public String deleteAlarm(Long id, Model model, RedirectAttributes redirectAttributes) {
    try {
      alarmService.deleteAlarm(id);
//      accountService.deleteAccount(id);
      redirectAttributes.addFlashAttribute(SUCCESS, true);
      redirectAttributes.addFlashAttribute(MESSAGE, HdConstant.MESSAGE_RECORD_DELETE_SUCESS);
      return "redirect:/alarm/alarmList";
    } catch (Exception ex) {
      redirectAttributes.addFlashAttribute(SUCCESS, false);
      redirectAttributes.addFlashAttribute(MESSAGE, ex.getMessage());
      log.error(ex);
      return "redirect:/alarm/alarmList";
    }
  }


}