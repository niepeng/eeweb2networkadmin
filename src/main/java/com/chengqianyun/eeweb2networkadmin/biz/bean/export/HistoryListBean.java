package com.chengqianyun.eeweb2networkadmin.biz.bean.export;

import lombok.Data;
import lombok.ToString;

/**
 * 核心数据bean
 *
 * @author 聂鹏
 * @version 1.0
 * @date 18/6/10
 */
@Data
@ToString
public class HistoryListBean {

  private String num;

  private String time;

  private String temp;

  private String humi;

  private String power;

  private String shine;

  private String pressure;


}