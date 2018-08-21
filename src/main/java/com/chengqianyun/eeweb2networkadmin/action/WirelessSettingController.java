package com.chengqianyun.eeweb2networkadmin.action;

import com.chengqianyun.eeweb2networkadmin.biz.enums.MenuEnum;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 无线设置
 *
 * @author 聂鹏
 * @version 1.0
 * @email lsb@51huadian.cn
 * @date 18/8/21
 */
@Controller
@RequestMapping("/wirelessSetting")
public class WirelessSettingController extends BaseController {


  /**
   * 通讯测试
   */
  @RequestMapping(value = "/normal", method = RequestMethod.GET)
  public String normal( Model model) {
    try {
      addOptMenu(model, MenuEnum.wirelessSetting);
//      model.addAttribute("settingNormalBean", settingService.normal());

      /**
       * GprsTestAction.send 发送短信
       *
       */
      return "/WirelessSetting/normal";
    } catch (Exception ex) {
      model.addAttribute(SUCCESS, false);
      model.addAttribute(MESSAGE, ex.getMessage());
      log.error(ex);
      return "/WirelessSetting/normal";
    }
  }


  /**
   * 通讯列表
   */
  @RequestMapping(value = "/list", method = RequestMethod.GET)
  public String list( Model model) {
    try {
      addOptMenu(model, MenuEnum.wirelessSetting);
      return "/WirelessSetting/list";
    } catch (Exception ex) {
      model.addAttribute(SUCCESS, false);
      model.addAttribute(MESSAGE, ex.getMessage());
      log.error(ex);
      return "/WirelessSetting/list";
    }
  }


  /**
   * 通讯历史
   */
  @RequestMapping(value = "/history", method = RequestMethod.GET)
  public String history( Model model) {
    try {
      addOptMenu(model, MenuEnum.wirelessSetting);
      return "/WirelessSetting/history";
    } catch (Exception ex) {
      model.addAttribute(SUCCESS, false);
      model.addAttribute(MESSAGE, ex.getMessage());
      log.error(ex);
      return "/WirelessSetting/history";
    }
  }


}