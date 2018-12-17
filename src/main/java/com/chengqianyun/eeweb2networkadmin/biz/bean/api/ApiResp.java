package com.chengqianyun.eeweb2networkadmin.biz.bean.api;


import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 聂鹏
 * @version 1.0
 * @date 18/12/14
 */
@Data
@NoArgsConstructor
public class ApiResp {

  /**
   * 0 表示成功, 其他
   */
  private int code;

  private Object data;

  private String msg;

  public ApiResp(int code) {
    this.code = code;
  }

}