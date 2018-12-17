package com.chengqianyun.eeweb2networkadmin.core;

import com.alibaba.fastjson.JSONObject;
import com.chengqianyun.eeweb2networkadmin.biz.bean.ParseToken;
import com.chengqianyun.eeweb2networkadmin.biz.bean.Token;
import com.chengqianyun.eeweb2networkadmin.core.utils.DateUtil;
import com.chengqianyun.eeweb2networkadmin.core.utils.ThreeDes;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 聂鹏
 * @version 1.0
 * @email lsb@51huadian.cn
 * @date 18/12/14
 */
@Slf4j
public class TokenHelper {

  private static final String desKey = "cqlnewkey2019";

  public static Token genToken(String loginName, String expireTime) {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("userId", loginName);
    jsonObject.put("expireIn", expireTime);
    String tokenValue = jsonObject.toJSONString();
    Token token = new Token();
    try {
      token.setToken(ThreeDes.desEncrypt(tokenValue, desKey));
    } catch (Exception e) {
      log.error("genTokenError, {}", e);
    }
    token.setExpireIn(expireTime);
    return token;
  }


  public static ParseToken parseToken(String token) {
    ParseToken t = new ParseToken();
    try {
      JSONObject jsonObject = JSONObject.parseObject(ThreeDes.desDecrypt(token, desKey));
      t.setUserId(jsonObject.getString("userId"));
      String now = DateUtil.getDate(new Date(), DateUtil.dateFullPattern);
      t.setExpire(DateUtil.isDateBefore(jsonObject.getString("userId"), now));
    } catch (Exception e) {
      log.error("current parse token error, {}", e);
    }
    return t;
  }


  public static void main(String[] args) {
    Token token = TokenHelper.genToken("niepeng", "2018-12-12 01:23:23");
    log.info("token:{}", token);

    ParseToken parseToken = parseToken(token.getToken());
    log.info("parseToken:{}", parseToken);
  }


}