package com.chengqianyun.eeweb2networkadmin.action;


import com.chengqianyun.eeweb2networkadmin.biz.HdConstant;
import com.chengqianyun.eeweb2networkadmin.biz.bean.EmailBean;
import com.chengqianyun.eeweb2networkadmin.biz.bean.SettingAlarmBean;
import com.chengqianyun.eeweb2networkadmin.biz.bean.SettingNormalBean;
import com.chengqianyun.eeweb2networkadmin.biz.enums.MenuEnum;
import com.chengqianyun.eeweb2networkadmin.core.utils.DateUtil;
import com.chengqianyun.eeweb2networkadmin.core.utils.data.CalcCRC;
import com.chengqianyun.eeweb2networkadmin.core.utils.data.FunctionUnit;
import com.chengqianyun.eeweb2networkadmin.data.InstructionManager;
import com.chengqianyun.eeweb2networkadmin.service.AlarmService;
import com.chengqianyun.eeweb2networkadmin.service.SettingService;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.util.Date;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
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
      // TODO ..短信开关在页面上默认关闭
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

  /**
   * 邮件设置
   */
  @RequestMapping(value = "/email", method = RequestMethod.GET)
  public String email(Model model) {
    try {
      addOptMenu(model, MenuEnum.setting);
      model.addAttribute("settingEmailBean", settingService.email());
      return "/setting/email";
    } catch (Exception ex) {
      model.addAttribute(SUCCESS, false);
      model.addAttribute(MESSAGE, ex.getMessage());
      log.error(ex);
      return "/setting/email";
    }
  }


  /**
   * 导入导出配置
   */
  @RequestMapping(value = "/inoutConfig", method = RequestMethod.GET)
  public String inoutConfig(Model model) {

    try {
      addOptMenu(model, MenuEnum.setting_inout_config);
      return "/setting/inoutConfig";
    } catch (Exception ex) {
      model.addAttribute(SUCCESS, false);
      model.addAttribute(MESSAGE, ex.getMessage());
      log.error(ex);
      return "/setting/inoutConfig";
    }
  }

//  doInoutConfig
  // configType
//MultipartFile pic

  @RequestMapping(value = "/doInoutConfig", method = RequestMethod.POST)
  public String doInoutConfig(MultipartFile file, String configType, HttpServletResponse response,Model model, RedirectAttributes redirectAttributes) {
    try {

      if("export".equalsIgnoreCase(configType)) {
        String content = settingService.exportConfig();

        String fileName = "export_" + DateUtil.getDate(new Date(), DateUtil.PATTERN_YYYYMMDDANDHHMMSS) + ".txt";
        OutputStream os = response.getOutputStream();
        response.reset();
        response.setHeader("Content-disposition", "attachment; filename=" + new String(fileName.getBytes("GB2312"), "ISO8859-1"));
        response.setContentType("text/plain");
        BufferedOutputStream buff = new BufferedOutputStream(os);
        buff.write(content.getBytes("UTF-8"));
        buff.flush();
        buff.close();
        redirectAttributes.addFlashAttribute(SUCCESS, true);
        redirectAttributes.addFlashAttribute(MESSAGE, HdConstant.MESSAGE_RECORD_OPERATE_SUCESS);
        return "redirect:/setting/inoutConfig";
      }


      if("import".equalsIgnoreCase(configType)) {
        if(file == null || file.getBytes().length == 0 || file.getOriginalFilename().indexOf(".txt") < 0) {
          redirectAttributes.addFlashAttribute(SUCCESS, false);
          redirectAttributes.addFlashAttribute(MESSAGE, "请先选择正确的文件再导入");
          return "redirect:/setting/inoutConfig";
        }
        String fileContent = new String(file.getBytes(), "UTF-8");
        settingService.importConfig(fileContent);
        redirectAttributes.addFlashAttribute(SUCCESS, true);
        redirectAttributes.addFlashAttribute(MESSAGE, "导入成功,请重启服务使得数据生效");
        return "redirect:/setting/inoutConfig";
      }

      redirectAttributes.addFlashAttribute(SUCCESS, true);
      redirectAttributes.addFlashAttribute(MESSAGE, HdConstant.MESSAGE_RECORD_OPERATE_SUCESS);
      return "redirect:/setting/inoutConfig";

    } catch (Exception ex) {
      redirectAttributes.addFlashAttribute(SUCCESS, false);
      redirectAttributes.addFlashAttribute(MESSAGE, ex.getMessage());
      log.error(ex);
      return "redirect:/setting/inoutConfig";
    }
  }



  @RequestMapping(value = "/doEmail", method = RequestMethod.POST)
  public String doEmail(EmailBean emailBean, Model model, RedirectAttributes redirectAttributes) {
    try {
      settingService.save(emailBean);
      redirectAttributes.addFlashAttribute(SUCCESS, true);
      redirectAttributes.addFlashAttribute(MESSAGE, HdConstant.MESSAGE_RECORD_SAVE_SUCESS);
      return "redirect:/setting/email";
    } catch (Exception ex) {
      redirectAttributes.addFlashAttribute(SUCCESS, false);
      redirectAttributes.addFlashAttribute(MESSAGE, ex.getMessage());
      log.error(ex);
      return "redirect:/setting/email";
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

  @RequestMapping(value = "/genInstructionInfo", method = RequestMethod.GET)
  public String genInstructionInfo(
      // 设备区域
      @RequestParam(value = "sn",  defaultValue = "") String sn,
      Model model) {

    try {
      addOptMenu(model, MenuEnum.setting);
      model.addAttribute("sn", sn);
      char[] snChars = changeSnChars(sn);
      if(snChars != null) {
        char[] data = genInstructionBySn(snChars);
        String value = FunctionUnit.bytesToHexString(data);
        model.addAttribute("value", value);
      } else {
        model.addAttribute("value", "请输入正确的sn格式");
      }
      return "/setting/genInstructionInfo";
    } catch (Exception ex) {
      model.addAttribute(SUCCESS, false);
      model.addAttribute(MESSAGE, ex.getMessage());
      log.error(ex);
      return "/setting/genInstructionInfo";
    }
  }

  private char[] changeSnChars(String sn) {
    if(sn == null || sn.trim().length() == 0) {
      return null;
    }

    String[] strs = sn.split(" ");
    if(strs.length != 4) {
      return null;
    }

    char[] r = new char[4];
    for(int i=0;i<r.length;i++) {
      r[i] = (char) Integer.parseInt(strs[i], 16);
    }
    return r;
  }


  private static char[] genInstructionBySn(char[] sn) {
    char[] result = {0xFB, 0x68, 0x06,
        0x00,0x00,0x00,0x00,  0x00,0x00,
        0x00,0x00
    };

    char[] snNew = {sn[0], sn[1],sn[2], sn[3], 0x00, 0x00};
    char[] snAndCheckCode = InstructionManager.getSnAndCheckCodeBySn(snNew);

    result[3] = snAndCheckCode[0];
    result[4] = snAndCheckCode[1];
    result[5] = snAndCheckCode[2];
    result[6] = snAndCheckCode[3];
    result[7] = snAndCheckCode[4];
    result[8] = snAndCheckCode[5];

    return CalcCRC.getCrc16(result);
  }
  

}