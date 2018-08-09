package com.chengqianyun.eeweb2networkadmin.action;


import com.chengqianyun.eeweb2networkadmin.biz.HdConstant;
import com.chengqianyun.eeweb2networkadmin.biz.bean.SettingAlarmBean;
import com.chengqianyun.eeweb2networkadmin.biz.bean.SettingNormalBean;
import com.chengqianyun.eeweb2networkadmin.biz.enums.MenuEnum;
import com.chengqianyun.eeweb2networkadmin.service.AlarmService;
import com.chengqianyun.eeweb2networkadmin.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 系统设置
 *
 * @author 聂鹏
 * @version 1.0
 * @date 18/4/12
 */
@Controller
@RequestMapping("/setting")
public class SettingController extends BaseController {


  @Autowired
  private SettingService settingService;


  /**
   * 基本设置
   */
  @RequestMapping(value = "/normal", method = RequestMethod.GET)
  public String normal(
      Model model) {

    try {
      addOptMenu(model, MenuEnum.setting);
      model.addAttribute("settingNormalBean", settingService.normal());
      return "/setting/normal";
    } catch (Exception ex) {
      model.addAttribute(SUCCESS, false);
      model.addAttribute(MESSAGE, ex.getMessage());
      log.error(ex);
      return "/setting/normal";
    }
  }

  @RequestMapping(value = "/doNormal", method = RequestMethod.POST)
  public String doNormal(SettingNormalBean settingNormalBean, Model model, RedirectAttributes redirectAttributes) {
    try {
      settingService.save(settingNormalBean);
      redirectAttributes.addFlashAttribute(SUCCESS, true);
      redirectAttributes.addFlashAttribute(MESSAGE, HdConstant.MESSAGE_RECORD_SAVE_SUCESS);
      return "redirect:/setting/normal";
    } catch (Exception ex) {
      redirectAttributes.addFlashAttribute(SUCCESS, false);
      redirectAttributes.addFlashAttribute(MESSAGE, ex.getMessage());
      log.error(ex);
      return "redirect:/setting/normal";
    }
  }



  /**
   * 报警设置
   */
  @RequestMapping(value = "/alarm", method = RequestMethod.GET)
  public String alarm(
      Model model) {

    try {
      addOptMenu(model, MenuEnum.setting);
      model.addAttribute("settingAlarmBean", settingService.alarm());
      return "/setting/alarm";
    } catch (Exception ex) {
      model.addAttribute(SUCCESS, false);
      model.addAttribute(MESSAGE, ex.getMessage());
      log.error(ex);
      return "/setting/alarm";
    }
  }

  @RequestMapping(value = "/doAlarm", method = RequestMethod.POST)
  public String doAlarm(SettingAlarmBean settingAlarmBean, Model model, RedirectAttributes redirectAttributes) {
    try {
      settingService.save(settingAlarmBean);
      redirectAttributes.addFlashAttribute(SUCCESS, true);
      redirectAttributes.addFlashAttribute(MESSAGE, HdConstant.MESSAGE_RECORD_SAVE_SUCESS);
      return "redirect:/setting/alarm";
    } catch (Exception ex) {
      redirectAttributes.addFlashAttribute(SUCCESS, false);
      redirectAttributes.addFlashAttribute(MESSAGE, ex.getMessage());
      log.error(ex);
      return "redirect:/setting/alarm";
    }
  }

}