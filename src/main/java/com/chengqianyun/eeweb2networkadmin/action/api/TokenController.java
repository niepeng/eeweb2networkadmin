package com.chengqianyun.eeweb2networkadmin.action.api;

import com.chengqianyun.eeweb2networkadmin.action.BaseController;
import com.chengqianyun.eeweb2networkadmin.biz.bean.Token;
import com.chengqianyun.eeweb2networkadmin.biz.bean.api.ApiResp;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.ConsoleLoginAccount;
import com.chengqianyun.eeweb2networkadmin.core.TokenHelper;
import com.chengqianyun.eeweb2networkadmin.core.utils.BizConstant;
import com.chengqianyun.eeweb2networkadmin.core.utils.BizConstant.ApiCode;
import com.chengqianyun.eeweb2networkadmin.core.utils.DateUtil;
import com.chengqianyun.eeweb2networkadmin.core.utils.SHAUtil;
import com.chengqianyun.eeweb2networkadmin.core.utils.StringUtil;
import com.chengqianyun.eeweb2networkadmin.service.ConsoleLoginAccountService;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author 聂鹏
 * @version 1.0
 * @date 18/12/14
 */
@Controller
@RequestMapping("/api/token")
public class TokenController extends BaseController {

  @Autowired
  private ConsoleLoginAccountService consoleLoginAccountService;

  @ResponseBody
  @RequestMapping(value = "/get", method = RequestMethod.GET)
  public ApiResp getToken(@RequestParam("user") String user, @RequestParam("psw") String psw) {
    ApiResp resp = new ApiResp(0);
    if (StringUtil.isEmpty(user) || StringUtil.isEmpty(psw)) {
      return defaultApiError("用户名密码不能为空", ApiCode.empty);
    }

    ConsoleLoginAccount consoleLoginAccount = consoleLoginAccountService.getLoginAccount(user);
    if (consoleLoginAccount == null) {
      return defaultApiError("用户名或密码错误", ApiCode.check_fail);
    }

    if (!SHAUtil.encode(psw).equals(consoleLoginAccount.getVcLoginPassword())) {
      return defaultApiError("用户名或密码错误", ApiCode.check_fail);
    }

    String expireTime = DateUtil.getDate(DateUtil.addDate(new Date(), BizConstant.token_days), DateUtil.dateFullPattern);
    Token token = TokenHelper.genToken(user, expireTime);
    resp.setData(token);
    return resp;
  }

}