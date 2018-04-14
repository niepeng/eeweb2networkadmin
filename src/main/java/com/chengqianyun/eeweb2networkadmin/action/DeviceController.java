package com.chengqianyun.eeweb2networkadmin.action;

import com.chengqianyun.eeweb2networkadmin.biz.HdConstant;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.Area;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceInfo;
import com.chengqianyun.eeweb2networkadmin.biz.enums.MenuEnum;
import com.chengqianyun.eeweb2networkadmin.biz.page.PageResult;
import com.chengqianyun.eeweb2networkadmin.biz.page.PaginationQuery;
import com.chengqianyun.eeweb2networkadmin.core.utils.StringUtil;
import com.chengqianyun.eeweb2networkadmin.service.DeviceService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 设备管理: 区域, 环境设备, 开关量设备
 *
 * @author 聂鹏
 * @version 1.0
 * @date 18/4/12
 */
@Controller
@RequestMapping("/device")
public class DeviceController extends BaseController {

  @Autowired
  private DeviceService deviceService;

  @RequestMapping(value = "/areaList", method = RequestMethod.GET)
  public String areaList(
      @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex,
      @RequestParam(value = "pageSize", required = false, defaultValue = "15") int pageSize,
      @RequestParam(required = false, defaultValue = "") String name,
      Model model) {
    try {
      addOptMenu(model, MenuEnum.device);
      PaginationQuery query = new PaginationQuery();
      query.setPageIndex(pageIndex);
      query.setRowsPerPage(pageSize);
      query.addQueryData("name", name);
      PageResult<Area> params = deviceService.getAreaList(query);
      model.addAttribute("result", params);
      model.addAttribute("name", name);

      return "/device/areaList";
    } catch (Exception ex) {
      model.addAttribute(SUCCESS, false);
      model.addAttribute(MESSAGE, ex.getMessage());
      log.error(ex);
      return "/device/areaList";
    }
  }


  @RequestMapping(value = "/areaAdd", method = RequestMethod.GET)
  public String areaAdd(Model model, RedirectAttributes redirectAttributes) {
    try {
      addOptMenu(model, MenuEnum.device);
      return "/device/areaAdd";
    } catch (Exception ex) {
      redirectAttributes.addFlashAttribute(SUCCESS, false);
      redirectAttributes.addFlashAttribute(MESSAGE, ex.getMessage());
      log.error(ex);
      return "redirect:/device/areaList";
    }
  }


  @RequestMapping(value = "/doAreaAdd", method = RequestMethod.POST)
  public String doAreaAdd(Area area, Model model, RedirectAttributes redirectAttributes) {
    try {
      deviceService.addArea(area);
      redirectAttributes.addFlashAttribute(SUCCESS, true);
      redirectAttributes.addFlashAttribute(MESSAGE, HdConstant.MESSAGE_RECORD_SAVE_SUCESS);
      return "redirect:/device/areaList";
    } catch (Exception ex) {
      redirectAttributes.addFlashAttribute("area", area);
      redirectAttributes.addFlashAttribute(SUCCESS, false);
      redirectAttributes.addFlashAttribute(MESSAGE, ex.getMessage());
      log.error(ex);
      return "redirect:/device/areaAdd";
    }
  }


  @RequestMapping(value = "/areaUpdate", method = RequestMethod.GET)
  public String areaUpdate(Long id, Model model, RedirectAttributes redirectAttributes) {
    try {
      addOptMenu(model, MenuEnum.device);
      Area area = deviceService.getArea(id);
      model.addAttribute("area", area);
      return "/device/areaUpdate";
    } catch (Exception ex) {
      redirectAttributes.addFlashAttribute(SUCCESS, false);
      redirectAttributes.addFlashAttribute(MESSAGE, ex.getMessage());
      log.error(ex);
      return "redirect:/device/areaList";
    }
  }

  @RequestMapping(value = "/doAreaUpdate", method = RequestMethod.POST)
  public String update(@ModelAttribute Area area, Model model, RedirectAttributes redirectAttributes) {
    try {
      deviceService.updateArea(area);
      redirectAttributes.addFlashAttribute(SUCCESS, true);
      redirectAttributes.addFlashAttribute(MESSAGE, HdConstant.MESSAGE_RECORD_OPERATE_SUCESS);
    } catch (Exception ex) {
      redirectAttributes.addFlashAttribute(SUCCESS, false);
      redirectAttributes.addFlashAttribute(MESSAGE, ex.getMessage());
      log.error(ex);
    }
    return "redirect:/device/areaList";
  }


  @RequestMapping(value = "/deleteArea", method = RequestMethod.GET)
  public String deleteArea(Long id, Model model, RedirectAttributes redirectAttributes) {
    try {
      deviceService.deleteArea(id);
      redirectAttributes.addFlashAttribute(SUCCESS, true);
      redirectAttributes.addFlashAttribute(MESSAGE,HdConstant.MESSAGE_RECORD_DELETE_SUCESS);
      return "redirect:/device/areaList";
    } catch (Exception ex) {
      model.addAttribute(SUCCESS, false);
      model.addAttribute(MESSAGE, ex.getMessage());
      log.error(ex);
      return "redirect:/device/areaList";
    }
  }


  /**
   * 环境设备管理
   */
  @RequestMapping(value = "/deviceEnvList", method = RequestMethod.GET)
  public String deviceEnvList(
      @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex,
      @RequestParam(value = "pageSize", required = false, defaultValue = "15") int pageSize,
      @RequestParam(value = "areaId", required = false, defaultValue = "") String areaId,
      @RequestParam(value = "name", required = false, defaultValue = "") String name,
      Model model) {
    try {
      addOptMenu(model, MenuEnum.device);
      PaginationQuery query = new PaginationQuery();
      query.setPageIndex(pageIndex);
      query.setRowsPerPage(pageSize);
      query.addQueryData("areaId", areaId);
      query.addQueryData("name", name);

      PageResult<DeviceInfo> params = deviceService.getDeviceInfoList(query);
      model.addAttribute("result", params);
      model.addAttribute("areaList", deviceService.getAreaAll());
      model.addAttribute("areaId", areaId);
      model.addAttribute("name", name);

      return "/device/deviceEnvList";
    } catch (Exception ex) {
      model.addAttribute(SUCCESS, false);
      model.addAttribute(MESSAGE, ex.getMessage());
      log.error(ex);
      return "/device/areaList";
    }
  }




}