package com.chengqianyun.eeweb2networkadmin.biz.bean.export;

import lombok.Data;
import lombok.ToString;

/**
 * @author 聂鹏
 * @version 1.0
 * @date 18/6/10
 */
@Data
@ToString
public class HistoryListBean {

  private String deviceName;

  private String temp;

  private String humi;
}