package com.chengqianyun.eeweb2networkadmin.action;

import com.chengqianyun.eeweb2networkadmin.biz.HdConstant;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.Contacts;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.SendContacts;
import com.chengqianyun.eeweb2networkadmin.biz.enums.MenuEnum;
import com.chengqianyun.eeweb2networkadmin.biz.page.PageResult;
import com.chengqianyun.eeweb2networkadmin.biz.page.PaginationQuery;
import com.chengqianyun.eeweb2networkadmin.core.utils.DateUtil;
import com.chengqianyun.eeweb2networkadmin.core.utils.StringUtil;
import com.chengqianyun.eeweb2networkadmin.service.ContactService;
import com.chengqianyun.eeweb2networkadmin.service.SerialService;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

  @Autowired
  private ContactService contactService;

  @Autowired
  private SerialService serialService;

  /**
   * 通讯测试
   */
  @RequestMapping(value = "/normal", method = RequestMethod.GET)
  public String normal( Model model) {
    try {
      addOptMenu(model, MenuEnum.wirelessSetting);
      model.addAttribute("running", serialService.testSerial());
      return "/wirelessSetting/normal";
    } catch (Exception ex) {
      model.addAttribute(SUCCESS, false);
      model.addAttribute(MESSAGE, ex.getMessage());
      log.error(ex);
      return "/wirelessSetting/normal";
    }
  }


//  /**
//   * 操作:打开关闭
//   */
//  @RequestMapping(value = "/serialOpt", method = RequestMethod.GET)
//  public String serialOpt( Model model,
//      @RequestParam(value = "newFlag", required = false, defaultValue = "") String newFlag, RedirectAttributes redirectAttributes) {
//    try {
//      if (newFlag.equalsIgnoreCase("true")) {
//        serialService.init(true);
//        if (serialService.isRunning()) {
//          redirectAttributes.addFlashAttribute(SUCCESS, true);
//          redirectAttributes.addFlashAttribute(MESSAGE, HdConstant.MESSAGE_RECORD_OPERATE_SUCESS);
//        }
//        redirectAttributes.addFlashAttribute(SUCCESS, false);
//        redirectAttributes.addFlashAttribute(MESSAGE, "运行失败,请确保com口插入硬件设备");
//        return "redirect:/wirelessSetting/normal";
//      }
//      serialService.close();
//      redirectAttributes.addFlashAttribute(SUCCESS, true);
//      redirectAttributes.addFlashAttribute(MESSAGE, HdConstant.MESSAGE_RECORD_OPERATE_SUCESS);
//      return "redirect:/wirelessSetting/normal";
//    } catch (Error ex) {
//      redirectAttributes.addFlashAttribute(SUCCESS, false);
//      redirectAttributes.addFlashAttribute(MESSAGE, "运行失败,请确保com口插入硬件设备,error");
//      log.error(ex);
//      return "redirect:/wirelessSetting/normal";
//    }
//  }


  /**
   * 通讯列表
   */
  @RequestMapping(value = "/list", method = RequestMethod.GET)
  public String list(
      @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex,
      @RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize,
      @RequestParam(value = "name", required = false, defaultValue = "") String name,
      Model model) {
    try {
      addOptMenu(model, MenuEnum.wirelessSetting);
      PaginationQuery query = new PaginationQuery();
      query.setPageIndex(pageIndex);
      query.setRowsPerPage(pageSize);
      query.addQueryData("name", name);


      PageResult<Contacts> params = contactService.getContactList(query);
      model.addAttribute("result", params);
      model.addAttribute("name", name);


      return "/wirelessSetting/list";
    } catch (Exception ex) {
      model.addAttribute(SUCCESS, false);
      model.addAttribute(MESSAGE, ex.getMessage());
      log.error(ex);
      return "/wirelessSetting/list";
    }
  }


  /**
   * 通讯录添加页面
   * @param model
   * @param redirectAttributes
   * @return
   */
  @RequestMapping(value = "/contactsAdd", method = RequestMethod.GET)
  public String contactsAdd(Model model, RedirectAttributes redirectAttributes) {
    try {
      addOptMenu(model, MenuEnum.wirelessSetting);
      return "/wirelessSetting/contactsAdd";
    } catch (Exception ex) {
      redirectAttributes.addFlashAttribute(SUCCESS, false);
      redirectAttributes.addFlashAttribute(MESSAGE, ex.getMessage());
      log.error(ex);
      return "redirect:/wirelessSetting/list";
    }
  }


  /**
   * 通讯录添加执行
   * @param contacts
   * @param model
   * @param redirectAttributes
   * @return
   */
  @RequestMapping(value = "/doContactsAdd", method = RequestMethod.POST)
  public String doContactsAdd(Contacts contacts, Model model, RedirectAttributes redirectAttributes) {
    try {
      if(StringUtil.isEmpty(contacts.getName())) {
        throw new RuntimeException("姓名不能为空");
      }
      if(StringUtil.isEmpty(contacts.getPhone())) {
        throw new RuntimeException("手机号码不能为空");
      }
      contactService.addContacts(contacts);
      redirectAttributes.addFlashAttribute(SUCCESS, true);
      redirectAttributes.addFlashAttribute(MESSAGE, HdConstant.MESSAGE_RECORD_SAVE_SUCESS);
      return "redirect:/wirelessSetting/list";
    } catch (Exception ex) {
      redirectAttributes.addFlashAttribute("contacts", contacts);
      redirectAttributes.addFlashAttribute(SUCCESS, false);
      redirectAttributes.addFlashAttribute(MESSAGE, ex.getMessage());
      log.error(ex);
      return "redirect:/wirelessSetting/contactsAdd";
    }
  }


  /**
   * 通讯录修改页面
   * @param id
   * @param model
   * @param redirectAttributes
   * @return
   */
  @RequestMapping(value = "/contactsUpdate", method = RequestMethod.GET)
  public String contacts(Long id, Model model, RedirectAttributes redirectAttributes) {
    try {
      addOptMenu(model, MenuEnum.wirelessSetting);
        Contacts contacts = contactService.getContactsById(id);
      model.addAttribute("contacts", contacts);
      return "/wirelessSetting/contactsUpdate";
    } catch (Exception ex) {
      redirectAttributes.addFlashAttribute(SUCCESS, false);
      redirectAttributes.addFlashAttribute(MESSAGE, ex.getMessage());
      log.error(ex);
      return "redirect:/wirelessSetting/list";
    }
  }

  /**
   * 通讯录修改执行
   * @param contacts
   * @param model
   * @param redirectAttributes
   * @return
   */
  @RequestMapping(value = "/doContactsUpdate", method = RequestMethod.POST)
  public String doContactsUpdate(@ModelAttribute Contacts contacts, Model model, RedirectAttributes redirectAttributes) {
    try {
      if(StringUtil.isEmpty(contacts.getName())) {
        throw new RuntimeException("姓名不能为空");
      }
      if(StringUtil.isEmpty(contacts.getPhone())) {
        throw new RuntimeException("手机号码不能为空");
      }
      contactService.updateContacts(contacts);
      redirectAttributes.addFlashAttribute(SUCCESS, true);
      redirectAttributes.addFlashAttribute(MESSAGE, HdConstant.MESSAGE_RECORD_OPERATE_SUCESS);
    } catch (Exception ex) {
      redirectAttributes.addFlashAttribute(SUCCESS, false);
      redirectAttributes.addFlashAttribute(MESSAGE, ex.getMessage());
      log.error(ex);
    }
    return "redirect:/wirelessSetting/list";
  }


  @RequestMapping(value = "/deleteContacts", method = RequestMethod.GET)
  public String deleteContacts(Long id, Model model, RedirectAttributes redirectAttributes) {
    try {
      contactService.deleteContacts(id);
      redirectAttributes.addFlashAttribute(SUCCESS, true);
      redirectAttributes.addFlashAttribute(MESSAGE,HdConstant.MESSAGE_RECORD_DELETE_SUCESS);
      return "redirect:/wirelessSetting/list";
    } catch (Exception ex) {
      redirectAttributes.addFlashAttribute(SUCCESS, false);
      redirectAttributes.addFlashAttribute(MESSAGE, ex.getMessage());
      log.error(ex);
      return "redirect:/wirelessSetting/list";
    }
  }



  /**
   * 通讯历史(发送的短信or拨打的电话列表)
   */
  @RequestMapping(value = "/history", method = RequestMethod.GET)
  public String history(
      @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex,
      @RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize,
      @RequestParam(value = "type", required = false, defaultValue = "") String type,
      @RequestParam(value = "startTime", required = false, defaultValue = "") String startTime,
      @RequestParam(value = "endTime", required = false, defaultValue = "") String endTime,
      Model model) {
    try {
      addOptMenu(model, MenuEnum.wirelessSetting);

      PaginationQuery query = new PaginationQuery();
      query.setPageIndex(pageIndex);
      query.setRowsPerPage(pageSize);
      query.addQueryData("startTime", startTime);
      query.addQueryData("endTime", endTime);
      query.addQueryData("type", type);
      if(StringUtil.isEmpty(startTime) && StringUtil.isEmpty(endTime)) {
        endTime = DateUtil.getDate(new Date(), DateUtil.dateFullPatternNoSecond);
        startTime = DateUtil.getDate(DateUtil.addDate(new Date(), -7), DateUtil.dateFullPatternNoSecond);
      }

      PageResult<SendContacts> params = contactService.getSendContactsList(query);
      model.addAttribute("result", params);
      model.addAttribute("type", type);
      model.addAttribute("startTime", startTime);
      model.addAttribute("endTime", endTime);

      return "/wirelessSetting/history";
    } catch (Exception ex) {
      model.addAttribute(SUCCESS, false);
      model.addAttribute(MESSAGE, ex.getMessage());
      log.error(ex);
      return "/wirelessSetting/history";
    }
  }


}