package com.chengqianyun.eeweb2networkadmin;


import com.chengqianyun.eeweb2networkadmin.core.utils.SHAUtil;

/**
 * @author 聂鹏
 * @version 1.0
 * @date 18/3/13
 */

public class T {

  public static  void  main(String[] args) {
    String psw = "hello1234";
    String value = SHAUtil.encode(psw);
    System.out.println(value);
  }
}