package com.chengqianyun.eeweb2networkadmin.biz.bean;


import lombok.Data;

/**
 * @author 聂鹏
 * @version 1.0
 * @date 18/12/14
 */
@Data
public class Token {

  private String token;
  /**
   * 过期时间
   */
  private String expireIn;

}