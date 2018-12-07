package com.chengqianyun.eeweb2networkadmin.action;

import com.chengqianyun.eeweb2networkadmin.biz.entitys.ConsoleLoginAccount;
import com.chengqianyun.eeweb2networkadmin.biz.enums.MenuEnum;
import com.chengqianyun.eeweb2networkadmin.core.utils.HttpSessionUtil;
import com.chengqianyun.eeweb2networkadmin.core.utils.MD5Util;
import com.chengqianyun.eeweb2networkadmin.core.utils.SHAUtil;
import com.chengqianyun.eeweb2networkadmin.core.utils.StringUtil;
import com.chengqianyun.eeweb2networkadmin.service.AlarmService;
import com.chengqianyun.eeweb2networkadmin.service.ConsoleLoginAccountService;
import com.chengqianyun.eeweb2networkadmin.service.DeviceIntimeService;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.codehaus.plexus.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class IndexController extends BaseController {

//  private static Map<String, HttpSession> sessionMap = new HashMap<String, HttpSession>();

  @Autowired
  private ConsoleLoginAccountService consoleLoginAccountService;

  @Autowired
  private DeviceIntimeService deviceIntimeService;

  @Autowired
  private AlarmService alarmService;

  @RequestMapping(value = "/")
  public String index() {
    return "/index";
  }

  @RequestMapping(value = "/main")
  public String main(Model model) {
    deviceIntimeService.main(model);
    model.addAttribute("alarmSong", alarmService.hasAlarmData());
    return "/main";
  }

  @RequestMapping(value = "/nopermission")
  public String nopermission() {
    return "/nopermission";
  }

  @RequestMapping(value = "/login")
  public String login(String loginname,
      String password,
      String sign,
      HttpServletRequest request, Model model,RedirectAttributes redirectAttributes) {

    log.error(String.format("loginAction: %s,p=%s", loginname, password));

    if ("".equals(loginname) || "".equals(password)) {
      model.addAttribute(MESSAGE, "用户名或密码不能为空");
      return "/index";
    }
    if (StringUtil.sql_Injection(loginname)) {
      model.addAttribute(MESSAGE, "用户名格式错误");
      return "/index";
    }
//    // 验证签名
//    String localSign = MD5Util.md5Hex(new String(loginname+password));
//    if (!localSign.equals(sign)){
//      model.addAttribute(MESSAGE, "请求数据非法:"+sign+"{<br>}"+localSign);
//      return "/index";
//    }
    log.error("loginAction:selectDB");
    ConsoleLoginAccount consoleLoginAccount = consoleLoginAccountService.getLoginAccount(loginname);
    if (consoleLoginAccount == null) {
      log.error("loginAction:selectIsNull");
      model.addAttribute(MESSAGE, "帐号或密码错误");
      return "/index";
    }

//    if (consoleLoginAccount.getiValid() == 2 || (consoleLoginAccount.getLockTime() != null
//        && (DateUtil.diffDateToMintue
//        (DateUtil.getCurrentDate(), consoleLoginAccount.getLockTime())) < 5)) {
//      model.addAttribute(MESSAGE, "帐号已被锁定");
//      return "/index";
//    }


    HttpSessionUtil.removeDynamicValidateCodeSession();
    log.error("loginAction:comparePsw");
    if (!SHAUtil.encode(password).equals(consoleLoginAccount.getVcLoginPassword())) {
//      ConsoleLoginAccount consoleLoginAccountParams = new ConsoleLoginAccount();
//      consoleLoginAccountParams.setVcLoginName(consoleLoginAccount.getVcLoginName());
//      operationLogService.add(convertOperationLog(loginname,request,consoleLoginAccountParams,
//          OperationResultEnum.fail, OperationTypeEnum.login));
//      List<OperationLog> list = operationLogService.getRecentLogFailList(loginname,90);
//      if(list.size()>=3){
//        consoleLoginAccount.setLockTime(DateUtil.getCurrentDate());
//        consoleLoginAccountService.updateLockTime(consoleLoginAccount);
//      }
      model.addAttribute(MESSAGE, "帐号或密码错误");
      return "/index";
    }

    //用户Session
//    if(sessionMap.get(loginname)!=null){
//      try {
//        sessionMap.get(loginname).invalidate();
//      }catch(Exception e){
//        log.info("error when invalidate session,it may be invalidate ",e);
//      }
//    }
//    sessionMap.put(loginname,request.getSession());

    // 获取上次登陆信息
//    OperationLog lastLoginLog = operationLogService.getLastLoginLog(consoleLoginAccount.getVcLoginName(),OperationTypeEnum.login.getCode());
//    if(lastLoginLog != null) {
//      consoleLoginAccount.setLastLoginIp(lastLoginLog.getIp());
//      consoleLoginAccount.setLastLoginTime(DateUtil.getDate(lastLoginLog.getOccurTime(),"yyyy-MM-dd HH:mm:ss"));
//    }
//
//    // 初始化权限
    consoleLoginAccountService.initAuth(consoleLoginAccount);
    HttpSessionUtil.setLoginSession(consoleLoginAccount);

//    LogFactory.logMessage("登陆");
//    ConsoleLoginAccount consoleLoginAccountParams = new ConsoleLoginAccount();
//    consoleLoginAccountParams.setVcLoginName(consoleLoginAccount.getVcLoginName());
//    operationLogService.add(convertOperationLog(loginname,request,consoleLoginAccountParams,
//        OperationResultEnum.success, OperationTypeEnum.login));
//
//    //首次登录修改初始密码
//    Map<String,String> map = new HashMap<String,String>();
//    map.put("name",loginname);
//    map.put("eventType",OperationTypeEnum.login.getCode());
//    map.put("opeResult",OperationResultEnum.success.getCode());
//    int allCount = operationLogService.getAllLognnOkCount(map);
//    if(allCount<=1){
//      redirectAttributes.addFlashAttribute("nav_set", "no_nav");
//      redirectAttributes.addAttribute("id",consoleLoginAccount.getId());
//      return "redirect:/consoleaccounts/changepassword";
//    }

//    return "redirect:/main";
    return "redirect:/intime/dataList";
  }

  @RequestMapping("/logout")
  public String logout(HttpServletRequest request, Model model) {
//    LogFactory.logMessage("退出系统");
//    ConsoleLoginAccount account = HttpSessionUtil.getLoginSession();
    HttpSessionUtil.removeLoginSession();
//    ConsoleLoginAccount consoleLoginAccountParams = new ConsoleLoginAccount();
//    consoleLoginAccountParams.setVcLoginName(account.getVcLoginName());
//    operationLogService.add(convertOperationLog(account.getVcLoginName(),request,consoleLoginAccountParams,
//        OperationResultEnum.success, OperationTypeEnum.logout));
    return "/index";

  }



  /**
   * 跳到更改密码页面
   * @param model
   * @return
   */
  @RequestMapping(value = "/changepassword", method = RequestMethod.GET)
  public String changepassword(Model model) {
//    addOptMenu(model, MenuEnum.);
    ConsoleLoginAccount account = HttpSessionUtil.getLoginSession();
    if(account == null) {
      return "redirect:/index";
    }

//    //首次登录修改初始密码
//    Map<String,String> map = new HashMap<String,String>();
//    map.put("name",account.getVcLoginName());
//    map.put("eventType", OperationTypeEnum.login.getCode());
//    map.put("opeResult", OperationResultEnum.success.getCode());
//    int allCount = operationLogService.getAllLognnOkCount(map);
//    if(allCount<=1){
//
//      model.addAttribute("nav_set", "no_nav");
//    }
    model.addAttribute("account", account);
    return "/changepassword";
  }


  /**
   * 执行更改密码操作
   * @param account
   * @param oldPassword
   * @param newPassword
   * @param confirmNewPassword
   * @param model
   * @return
   */
  @RequestMapping(value = "/updatepassword", method = RequestMethod.POST)
  public String updatepassword(@ModelAttribute ConsoleLoginAccount account,
      String oldPassword, String newPassword, String confirmNewPassword,
      Model model) {
//    addOptMenu(model, MenuEnum.adminRole);
    try {
      if (StringUtils.isBlank(oldPassword)) {
        throw new Exception("当前密码不能为空");
      }
      if (newPassword.equals(oldPassword)) {
        throw new Exception("新老密码相同");
      }
      if (StringUtils.isBlank(newPassword)) {
        throw new Exception("新密码不能为空");
      }
      if (StringUtils.isBlank(confirmNewPassword)) {
        throw new Exception("确认密码不能为空");
      }
      if (!newPassword.equals(confirmNewPassword)) {
        throw new Exception("两次输入密码不一致");
      }
      String pwd = SHAUtil.encode(oldPassword);
      ConsoleLoginAccount loginAccount = HttpSessionUtil.getLoginSession();
      if (!pwd.equals(loginAccount.getVcLoginPassword())) {
        throw new Exception("当前密码错误");
      }
      account.setVcLoginPassword(SHAUtil.encode(newPassword));
      account.setDtModify(new Date());
      consoleLoginAccountService.updatepassword(account);
      loginAccount.setVcLoginPassword(account.getVcLoginPassword());
      HttpSessionUtil.setLoginSession(loginAccount);
      return "/success";
    } catch (Exception e) {
      model.addAttribute("newPassword", newPassword);
      model.addAttribute("confirmNewPassword", confirmNewPassword);
      model.addAttribute("account", account);
      model.addAttribute(SUCCESS, false);
      model.addAttribute(MESSAGE, e.getMessage());
      return "/changepassword";
    }
  }






}
