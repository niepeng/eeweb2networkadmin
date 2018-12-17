package com.chengqianyun.eeweb2networkadmin.biz.bean;


import lombok.Data;

/**
 * @author 聂鹏
 * @version 1.0
 * @date 18/12/14
 */
@Data
public class ParseToken {

  /**
   * 用户名
   */
  private String userId;

  /**
   * true 过期, false未过期
   */
  private boolean isExpire;
}