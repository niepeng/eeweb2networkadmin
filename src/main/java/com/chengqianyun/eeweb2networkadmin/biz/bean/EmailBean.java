package com.chengqianyun.eeweb2networkadmin.biz.bean;


import lombok.Data;
import lombok.ToString;

/**
 * @author 聂鹏
 * @version 1.0
 * @date 18/12/6
 */
@Data
@ToString
public class EmailBean {

  private boolean alarm_email;
  private boolean mail_smtp_auth;
  private String mail_host;
  private String mail_transport_protocol;
  private int mail_smtp_port;
  private String mail_auth_name;
  private String mail_auth_password;
  private boolean mail_is_ssl;
  private String mail_send_charset;
  private String mail_smtp_timeout;

  /**
   * 逗号分隔多个:中英文都支持
   */
  private String receiveEmails;
}