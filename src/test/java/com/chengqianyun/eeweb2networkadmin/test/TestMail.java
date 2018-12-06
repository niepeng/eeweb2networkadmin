package com.chengqianyun.eeweb2networkadmin.test;


import com.chengqianyun.eeweb2networkadmin.service.MailService;

/**
 * @author 聂鹏
 * @version 1.0
 * @email lsb@51huadian.cn
 * @date 18/10/31
 */

public class TestMail {

  public static void main(String[] args) {

    MailService.auth = "true";
    MailService.host = "smtp.163.com";
    MailService.protocol = "smtp";
    MailService.port = 465;
    MailService.authName = "cqlweb@163.com";
    MailService.password = "hello1234";
    MailService.charset = "UTF-8";
    MailService.isSSL = true;
    MailService.timeout = "5000";

    boolean isSend = MailService.sendEmail("这是一封普通的邮件1", new String[]{"253041869@qq.com"}, null, "<h3><a href='http://www.baidu.com'>内容随意定义的,其实不需要看,标题就好了.</a></h3>", null);
    System.out.println("发送是否成功:" + isSend);
  }
}