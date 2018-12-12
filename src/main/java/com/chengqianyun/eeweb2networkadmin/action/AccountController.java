package com.chengqianyun.eeweb2networkadmin.action;
/**
 * Created by lsb on 18/12/11.
 */


import com.chengqianyun.eeweb2networkadmin.biz.HdConstant;
import com.chengqianyun.eeweb2networkadmin.biz.bean.DeviceFormBean;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.Area;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.ConsoleLoginAccount;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceAlarm;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceInfo;
import com.chengqianyun.eeweb2networkadmin.biz.enums.MenuEnum;
import com.chengqianyun.eeweb2networkadmin.biz.page.PageResult;
import com.chengqianyun.eeweb2networkadmin.biz.page.PaginationQuery;
import com.chengqianyun.eeweb2networkadmin.core.utils.DateUtil;
import com.chengqianyun.eeweb2networkadmin.core.utils.StringUtil;
import com.chengqianyun.eeweb2networkadmin.service.AccountService;
import java.util.Date;
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
 * @author 聂鹏
 * @version 1.0
 * @date 18/12/11
 */
@Controller
@RequestMapping("/account")
public class AccountController extends BaseController {


  @Autowired
  private AccountService accountService;

  /**
   * 账户列表
   */
  @RequestMapping(value = "/accountList", method = RequestMethod.GET)
  public String accountList(
      @RequestParam(value = "userName", required = false, defaultValue = "") String userName,
      @RequestParam(value = "type", required = false, defaultValue = "") String type,
      Model model) {
    try {
      addOptMenu(model, MenuEnum.account);
      List<ConsoleLoginAccount> list = accountService.getAccountList(userName, type);
      model.addAttribute("userName", userName);
      model.addAttribute("type", type);
      model.addAttribute("list", list);
      return "/account/accountList";
    } catch (Exception ex) {
      model.addAttribute(SUCCESS, false);
      model.addAttribute(MESSAGE, ex.getMessage());
      log.error(ex);
      return "/account/accountList";
    }
  }


  /**
   * 账户添加
   */
  @RequestMapping(value = "/accountAdd", method = RequestMethod.GET)
  public String accountAdd(
      Model model) {
    try {
      addOptMenu(model, MenuEnum.account);
      return "/account/accountAdd";
    } catch (Exception ex) {
      model.addAttribute(SUCCESS, false);
      model.addAttribute(MESSAGE, ex.getMessage());
      log.error(ex);
      return "/account/accountAdd";
    }
  }


  /**
   * 账号管理:执行添加
   */
  @RequestMapping(value = "/doAccountAdd", method = RequestMethod.POST)
  public String doAccountAdd(ConsoleLoginAccount account, Model model, RedirectAttributes redirectAttributes) {
    try {
      addOptMenu(model, MenuEnum.account);
      accountService.addAccount(account);

      redirectAttributes.addFlashAttribute(SUCCESS, true);
      redirectAttributes.addFlashAttribute(MESSAGE, HdConstant.MESSAGE_RECORD_SAVE_SUCESS);
      return "redirect:/account/accountList";
    } catch (Exception ex) {
      redirectAttributes.addFlashAttribute(SUCCESS, false);
      redirectAttributes.addFlashAttribute(MESSAGE, ex.getMessage());
      log.error(ex);
      return "redirect:/account/accountList";
    }
  }

  /**
   * 账户管理:进入编辑页
   */
  @RequestMapping(value = "/accountUpdate", method = RequestMethod.GET)
  public String accountUpdate(Integer id, Model model, RedirectAttributes redirectAttributes) {
    try {
      addOptMenu(model, MenuEnum.account);
      ConsoleLoginAccount account = accountService.getAccount(id);
      model.addAttribute("account", account);

      return "/account/accountUpdate";
    } catch (Exception ex) {
      redirectAttributes.addFlashAttribute(SUCCESS, false);
      redirectAttributes.addFlashAttribute(MESSAGE, ex.getMessage());
      log.error(ex);
      return "redirect:/account/accountList";
    }
  }

  /**
   * 账户管理:执行修改
   */
  @RequestMapping(value = "/doAccountUpdate", method = RequestMethod.POST)
  public String doAccountUpdate(@ModelAttribute ConsoleLoginAccount account, Model model, RedirectAttributes redirectAttributes) {
    try {
      accountService.updateAccount(account);
      redirectAttributes.addFlashAttribute(SUCCESS, true);
      redirectAttributes.addFlashAttribute(MESSAGE, HdConstant.MESSAGE_RECORD_OPERATE_SUCESS);
    } catch (Exception ex) {
      redirectAttributes.addFlashAttribute(SUCCESS, false);
      redirectAttributes.addFlashAttribute(MESSAGE, ex.getMessage());
      log.error(ex);
    }
    return "redirect:/account/accountList";
  }



  /**
   * 账户管理:删除
   */
  @RequestMapping(value = "/initPsw", method = RequestMethod.GET)
  public String initPsw(Integer id, Model model, RedirectAttributes redirectAttributes) {
    try {
      accountService.initPsw(id);
      redirectAttributes.addFlashAttribute(SUCCESS, true);
      redirectAttributes.addFlashAttribute(MESSAGE, HdConstant.MESSAGE_RECORD_OPERATE_SUCESS);
      return "redirect:/account/accountList";
    } catch (Exception ex) {
      redirectAttributes.addFlashAttribute(SUCCESS, false);
      redirectAttributes.addFlashAttribute(MESSAGE, ex.getMessage());
      log.error(ex);
      return "redirect:/account/accountList";
    }
  }

  /**
   * 账户管理:删除
   */
  @RequestMapping(value = "/deleteAccount", method = RequestMethod.GET)
  public String deleteAccount(Integer id, Model model, RedirectAttributes redirectAttributes) {
    try {
      accountService.deleteAccount(id);
      redirectAttributes.addFlashAttribute(SUCCESS, true);
      redirectAttributes.addFlashAttribute(MESSAGE, HdConstant.MESSAGE_RECORD_DELETE_SUCESS);
      return "redirect:/account/accountList";
    } catch (Exception ex) {
      redirectAttributes.addFlashAttribute(SUCCESS, false);
      redirectAttributes.addFlashAttribute(MESSAGE, ex.getMessage());
      log.error(ex);
      return "redirect:/account/accountList";
    }
  }



}