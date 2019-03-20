package com.chengqianyun.eeweb2networkadmin.biz.bean;

import lombok.Data;

@Data
  public class PhoneTask {
    private boolean call;
    private boolean sms;
    private String phone;
    private String smsContent;
    private String contractName;
  }