package com.chengqianyun.eeweb2networkadmin.action;

import com.chengqianyun.eeweb2networkadmin.biz.entitys.ConsoleLoginAccount;
import com.chengqianyun.eeweb2networkadmin.core.utils.HttpSessionUtil;
import com.chengqianyun.eeweb2networkadmin.core.utils.MD5Util;
import com.chengqianyun.eeweb2networkadmin.core.utils.SHAUtil;
import com.chengqianyun.eeweb2networkadmin.core.utils.StringUtil;
import com.chengqianyun.eeweb2networkadmin.service.ConsoleLoginAccountService;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class IndexController extends BaseController {

//  private static Map<String, HttpSession> sessionMap = new HashMap<String, HttpSession>();

  @Autowired
  private ConsoleLoginAccountService consoleLoginAccountService;

  @RequestMapping(value = "/")
  public String index() {
    return "/index";
  }

  @RequestMapping(value = "/main")
  public String main() {
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

    ConsoleLoginAccount consoleLoginAccount = consoleLoginAccountService.getLoginAccount(loginname);
    if (consoleLoginAccount == null) {
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

    return "redirect:/main";
  }

  @RequestMapping("/logout")
  public String logout(HttpServletRequest request, Model model) {
//    LogFactory.logMessage("退出系统");
    HttpSession session = request.getSession();
    ConsoleLoginAccount account = (ConsoleLoginAccount)session.getAttribute("loginSessionInfo");
//    sessionMap.remove(account.getVcLoginName());
    HttpSessionUtil.removeLoginSession();
//    ConsoleLoginAccount consoleLoginAccountParams = new ConsoleLoginAccount();
//    consoleLoginAccountParams.setVcLoginName(account.getVcLoginName());
//    operationLogService.add(convertOperationLog(account.getVcLoginName(),request,consoleLoginAccountParams,
//        OperationResultEnum.success, OperationTypeEnum.logout));
    return "/index";

  }




}
