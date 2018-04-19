package com.chengqianyun.eeweb2networkadmin.action;


import com.chengqianyun.eeweb2networkadmin.biz.bean.DataIntimeBean;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.Area;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceDataIntime;
import com.chengqianyun.eeweb2networkadmin.biz.enums.MenuEnum;
import com.chengqianyun.eeweb2networkadmin.biz.page.PaginationQuery;
import com.chengqianyun.eeweb2networkadmin.service.DeviceIntimeService;
import com.chengqianyun.eeweb2networkadmin.service.DeviceService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 实时管理
 *
 * @author 聂鹏
 * @version 1.0
 * @date 18/4/9
 */
@Controller
@RequestMapping("/intime")
public class IntimeController extends BaseController {

  @Autowired
  private DeviceService deviceService;

  @Autowired
  private DeviceIntimeService deviceIntimeService;

  /**
   * 实时数据展示1:列表,单传感器
   */
  @RequestMapping( value = "/dataList", method = RequestMethod.GET)
  public String dataList(
      // 设备类型
      @RequestParam(value = "deviceTypes", required = false, defaultValue = "") String deviceTypes,
      // 报警状态
      @RequestParam(value = "statuses", required = false, defaultValue = "") String statuses,
      // 设备区域
      @RequestParam(value = "areaIds", required = false, defaultValue = "") String areaIds,
//      // 标签
//      @RequestParam(value = "tags", required = false, defaultValue = "") String tags,
      Model model) {
    try {
      addOptMenu(model, MenuEnum.intime);
      List<Area> areaList = deviceService.getAreaAll();
      DataIntimeBean dataIntimeBean = new DataIntimeBean();
      dataIntimeBean.setDeviceTypes(deviceTypes);
      dataIntimeBean.setStatuses(statuses);
      dataIntimeBean.setAreaIds(areaIds);

      deviceIntimeService.deviceDataIntime(dataIntimeBean);
      model.addAttribute("areaList", areaList);
      model.addAttribute("dataIntimeBean", dataIntimeBean);
      return "/intime/dataList";
    } catch (Exception ex) {
      model.addAttribute(SUCCESS, false);
      model.addAttribute(MESSAGE, ex.getMessage());
      log.error(ex);
      return "/intime/dataList";
    }
  }

  /**
   * 实时数据展示2:总览
   */
  @RequestMapping( value = "/dataList2", method = RequestMethod.GET)
  public String dataList2(
      // 报警状态:StatusEnum
      @RequestParam(value = "status", required = false, defaultValue = "") String status,
      // 设备区域
      @RequestParam(value = "areaId", required = false, defaultValue = "") String areaId,
      // 名称
      @RequestParam(value = "name", required = false, defaultValue = "") String name,
      Model model) {
    try {
      addOptMenu(model, MenuEnum.intime);
      List<Area> areaList = deviceService.getAreaAll();
      List<DeviceDataIntime> dataIntimeList = deviceIntimeService.deviceDataIntimeFor2(status, areaId, name);

      model.addAttribute("dataIntimeList", dataIntimeList);
      model.addAttribute("areaList", areaList);
      model.addAttribute("status", status);
      model.addAttribute("areaId", areaId);
      model.addAttribute("name", name);

      return "/intime/dataList2";
    } catch (Exception ex) {
      model.addAttribute(SUCCESS, false);
      model.addAttribute(MESSAGE, ex.getMessage());
      log.error(ex);
      return "/intime/dataList2";
    }
  }


}