package com.chengqianyun.eeweb2networkadmin.biz.bean.api;

import lombok.Data;

/**
 * @author 聂鹏
 * @version 1.0
 * @email lsb@51huadian.cn
 * @date 18/12/14
 */
@Data
public class TokenResp {

  private String token;

  private String expiresIn;

}