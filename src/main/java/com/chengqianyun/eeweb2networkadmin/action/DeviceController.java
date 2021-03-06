package com.chengqianyun.eeweb2networkadmin.action;

import com.chengqianyun.eeweb2networkadmin.biz.HdConstant;
import com.chengqianyun.eeweb2networkadmin.biz.bean.DeviceFormBean;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.Area;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.Contacts;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceInfo;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.OutCondition;
import com.chengqianyun.eeweb2networkadmin.biz.enums.MenuEnum;
import com.chengqianyun.eeweb2networkadmin.biz.page.PageResult;
import com.chengqianyun.eeweb2networkadmin.biz.page.PaginationQuery;
import com.chengqianyun.eeweb2networkadmin.service.ContactService;
import com.chengqianyun.eeweb2networkadmin.service.DeviceService;
import java.util.List;
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

  @Autowired
  private ContactService contactService;

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
  public String doAreaUpdate(@ModelAttribute Area area, Model model, RedirectAttributes redirectAttributes) {
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



  @RequestMapping(value = "/areaUpdateContacts", method = RequestMethod.GET)
  public String areaUpdateContacts(Long id, Model model, RedirectAttributes redirectAttributes) {
    try {
      addOptMenu(model, MenuEnum.device);
      List<Contacts> list = contactService.getAll();
      Area area = deviceService.getArea(id);
      deviceService.optAreaContacts(area, list);

      model.addAttribute("contactsList", list);
      model.addAttribute("area", area);
      return "/device/areaUpdateContacts";
    } catch (Exception ex) {
      redirectAttributes.addFlashAttribute(SUCCESS, false);
      redirectAttributes.addFlashAttribute(MESSAGE, ex.getMessage());
      log.error(ex);
      return "redirect:/device/areaList";
    }
  }


  @RequestMapping(value = "/doAreaUpdateContacts", method = RequestMethod.POST)
  public String doAreaUpdateContacts(@ModelAttribute Area area, Model model, RedirectAttributes redirectAttributes) {
    try {
      deviceService.updateAreaContacts(area);
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
      redirectAttributes.addFlashAttribute(SUCCESS, false);
      redirectAttributes.addFlashAttribute(MESSAGE, ex.getMessage());
      log.error(ex);
      return "redirect:/device/areaList";
    }
  }


  /**
   * 环境设备管理
   */
  @RequestMapping(value = "/deviceList", method = RequestMethod.GET)
  public String deviceList(
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

      return "/device/deviceList";
    } catch (Exception ex) {
      model.addAttribute(SUCCESS, false);
      model.addAttribute(MESSAGE, ex.getMessage());
      log.error(ex);
      return "/device/areaList";
    }
  }

  /**
   * 设备添加
   */
  @RequestMapping(value = "/deviceAdd", method = RequestMethod.GET)
  public String deviceAdd(
      Model model) {
    try {
      addOptMenu(model, MenuEnum.device);
      List<Area> areaList = deviceService.getAreaAll();
      model.addAttribute("areaList", areaList);

      return "/device/deviceAdd";
    } catch (Exception ex) {
      model.addAttribute(SUCCESS, false);
      model.addAttribute(MESSAGE, ex.getMessage());
      log.error(ex);
      return "/device/deviceAdd";
    }
  }

//

  /**
   * 设备管理:执行添加
   */
  @RequestMapping(value = "/doDeviceAdd", method = RequestMethod.POST)
  public String doDeviceAdd(DeviceFormBean deviceFormBean, Model model, RedirectAttributes redirectAttributes) {
    try {
      addOptMenu(model, MenuEnum.device);
      deviceService.addDeviceInfo(deviceFormBean);

      redirectAttributes.addFlashAttribute(SUCCESS, true);
      redirectAttributes.addFlashAttribute(MESSAGE, HdConstant.MESSAGE_RECORD_SAVE_SUCESS);
      return "redirect:/device/deviceList";
    } catch (Exception ex) {
      redirectAttributes.addFlashAttribute(SUCCESS, false);
      redirectAttributes.addFlashAttribute(MESSAGE, ex.getMessage());
      log.error(ex);
      return "redirect:/device/deviceAdd";
    }
  }

  /**
   * 设备管理:进入编辑页
   */
  @RequestMapping(value = "/deviceUpdate", method = RequestMethod.GET)
  public String deviceUpdate(Long id, Model model, RedirectAttributes redirectAttributes) {
    try {
      addOptMenu(model, MenuEnum.device);
      DeviceInfo deviceInfo = deviceService.getDeviceInfo(id);
      List<Area> areaList = deviceService.getAreaAll();
      model.addAttribute("deviceInfo", deviceInfo);
      model.addAttribute("areaList", areaList);

      return "/device/deviceUpdate";
    } catch (Exception ex) {
      redirectAttributes.addFlashAttribute(SUCCESS, false);
      redirectAttributes.addFlashAttribute(MESSAGE, ex.getMessage());
      log.error(ex);
      return "redirect:/device/deviceList";
    }
  }

  /**
   * 设备管理:执行修改
   */
  @RequestMapping(value = "/doDeviceUpdate", method = RequestMethod.POST)
  public String doDeviceUpdate(@ModelAttribute DeviceFormBean bean, Model model, RedirectAttributes redirectAttributes) {
    try {
      deviceService.updateDevice(bean);
      redirectAttributes.addFlashAttribute(SUCCESS, true);
      redirectAttributes.addFlashAttribute(MESSAGE, HdConstant.MESSAGE_RECORD_OPERATE_SUCESS);
    } catch (Exception ex) {
      redirectAttributes.addFlashAttribute(SUCCESS, false);
      redirectAttributes.addFlashAttribute(MESSAGE, ex.getMessage());
      log.error(ex);
    }
    return "redirect:/device/deviceList";
  }


  /**
   * 设备管理:删除
   */
  @RequestMapping(value = "/deleteDevice", method = RequestMethod.GET)
  public String deleteDevice(Long id, Model model, RedirectAttributes redirectAttributes) {
    try {
      deviceService.deleteDevice(id);
      redirectAttributes.addFlashAttribute(SUCCESS, true);
      redirectAttributes.addFlashAttribute(MESSAGE,HdConstant.MESSAGE_RECORD_DELETE_SUCESS);
      return "redirect:/device/deviceList";
    } catch (Exception ex) {
      redirectAttributes.addFlashAttribute(SUCCESS, false);
      redirectAttributes.addFlashAttribute(MESSAGE, ex.getMessage());
      log.error(ex);
      return "redirect:/device/deviceList";
    }
  }





  /**
   * 开关量输出:配置开关条件
   */
  @RequestMapping(value = "/outConditionList", method = RequestMethod.GET)
  public String outConditionList(
      @RequestParam(value = "id", required = false, defaultValue = "0") int id,
      Model model,RedirectAttributes redirectAttributes) {
    try {
      addOptMenu(model, MenuEnum.device);

      DeviceInfo deviceInfo = deviceService.getDeviceInfo(id);
      if (deviceInfo == null || !deviceInfo.hasOut()) {
        throw new RuntimeException("当前类型的设备不需要配置开关条件");
      }
      deviceInfo.setOutConditionList(deviceService.getOutConditionList(id));
      model.addAttribute("deviceInfo", deviceInfo);
      return "/device/outConditionList";
    } catch (Exception ex) {
      redirectAttributes.addFlashAttribute(SUCCESS, false);
      redirectAttributes.addFlashAttribute(MESSAGE, ex.getMessage());
      log.error(ex);
      return "redirect:/device/deviceList";
    }
  }


  /**
   * 开关量输出:配置开关条件:添加
   */
  @RequestMapping(value = "/outConditionAdd", method = RequestMethod.GET)
  public String outConditionAdd(
      @RequestParam(value = "id", required = false, defaultValue = "0") int id,
      Model model,RedirectAttributes redirectAttributes) {
    try {
      addOptMenu(model, MenuEnum.device);


      DeviceInfo deviceInfo = deviceService.getDeviceInfo(id);
      if (deviceInfo == null || !deviceInfo.hasOut()) {
        throw new RuntimeException("当前类型的设备不需要配置开关条件");
      }
      deviceInfo.setOutConditionList(deviceService.getOutConditionList(id));
      model.addAttribute("deviceInfo", deviceInfo);

      return "/device/outConditionAdd";
    } catch (Exception ex) {
      redirectAttributes.addFlashAttribute(SUCCESS, false);
      redirectAttributes.addFlashAttribute(MESSAGE, ex.getMessage());
      log.error(ex);
      return "redirect:/device/deviceList";
    }
  }

  /**
   * 开关量输出:配置开关条件:执行添加
   */
  @RequestMapping(value = "/doOutConditionAdd", method = RequestMethod.POST)
  public String doOutConditionAdd(OutCondition outCondition, Model model, RedirectAttributes redirectAttributes) {
    long deviceId = 0;
    try {
      addOptMenu(model, MenuEnum.device);
      deviceId = outCondition.getDeviceInfoId();

      deviceService.outConditionAdd(outCondition);

      redirectAttributes.addFlashAttribute(SUCCESS, true);
      redirectAttributes.addFlashAttribute(MESSAGE, HdConstant.MESSAGE_RECORD_SAVE_SUCESS);
      return "redirect:/device/outConditionList?id=" + deviceId;

    } catch (Exception ex) {
      redirectAttributes.addFlashAttribute(SUCCESS, false);
      redirectAttributes.addFlashAttribute(MESSAGE, ex.getMessage());
      log.error(ex);
      return "redirect:/device/outConditionList?id=" + deviceId;
    }
  }


  /**
   * 开关量输出:配置开关条件:查看修改
   */
  @RequestMapping(value = "/outConditionUpdate", method = RequestMethod.GET)
  public String outConditionUpdate(
      @RequestParam(value = "id", required = false, defaultValue = "0") int outConditionId,
      @RequestParam(value = "deviceInfoId", required = false, defaultValue = "0") int deviceInfoId,
      Model model, RedirectAttributes redirectAttributes) {
    try {
      addOptMenu(model, MenuEnum.device);
      OutCondition outCondition =  deviceService.getCondition(outConditionId);
      if(outCondition.getDeviceInfoId() != deviceInfoId) {
        throw new RuntimeException("请求参数错误");
      }
      DeviceInfo deviceInfo = deviceService.getDeviceInfo(deviceInfoId);
      if (deviceInfo == null || !deviceInfo.hasOut()) {
        throw new RuntimeException("当前类型的设备不需要配置开关条件");
      }


      model.addAttribute("outCondition", outCondition);
      model.addAttribute("deviceInfo", deviceInfo);

      return "/device/outConditionUpdate";
    } catch (Exception ex) {
      redirectAttributes.addFlashAttribute(SUCCESS, false);
      redirectAttributes.addFlashAttribute(MESSAGE, ex.getMessage());
      log.error(ex);
      return "redirect:/device/outConditionList?id=" + deviceInfoId;
    }
  }

  /**
   * 开关量输出:配置开关条件:执行修改
   */
  @RequestMapping(value = "/doOutConditionUpdate", method = RequestMethod.POST)
  public String doOutConditionUpdate(@ModelAttribute OutCondition condition, Model model, RedirectAttributes redirectAttributes) {
    try {
      deviceService.updateOutCondition(condition);
      redirectAttributes.addFlashAttribute(SUCCESS, true);
      redirectAttributes.addFlashAttribute(MESSAGE, HdConstant.MESSAGE_RECORD_OPERATE_SUCESS);
    } catch (Exception ex) {
      redirectAttributes.addFlashAttribute(SUCCESS, false);
      redirectAttributes.addFlashAttribute(MESSAGE, ex.getMessage());
      log.error(ex);
    }
    return "redirect:/device/outConditionList?id=" + condition.getDeviceInfoId();
  }


  /**
   * 开关量输出:配置开关条件:执行删除
   */
  @RequestMapping(value = "/outConditionDelete", method = RequestMethod.GET)
  public String outConditionDelete(
      @RequestParam(value = "id", required = false, defaultValue = "0") int outConditionId,
      @RequestParam(value = "deviceInfoId", required = false, defaultValue = "0") int deviceInfoId,
      Model model, RedirectAttributes redirectAttributes) {
    try {
      deviceService.outConditionDelete(outConditionId);
      redirectAttributes.addFlashAttribute(SUCCESS, true);
      redirectAttributes.addFlashAttribute(MESSAGE, HdConstant.MESSAGE_RECORD_DELETE_SUCESS);

      return "redirect:/device/outConditionList?id=" + deviceInfoId;

    } catch (Exception ex) {
      redirectAttributes.addFlashAttribute(SUCCESS, false);
      redirectAttributes.addFlashAttribute(MESSAGE, ex.getMessage());
      log.error(ex);
      return "redirect:/device/outConditionList?id=" + deviceInfoId;
    }
  }



}