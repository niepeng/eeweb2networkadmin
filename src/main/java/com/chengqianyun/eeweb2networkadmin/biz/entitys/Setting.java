package com.chengqianyun.eeweb2networkadmin.biz.entitys;


import java.util.Date;
import lombok.Data;
import lombok.ToString;

/**
 * @author 聂鹏
 * @version 1.0
 * @date 18/3/13
 */
@Data
@ToString
public class Setting {

  private Long id;

  private String paramCode;

  private String paramValue;

  private Date createdAt;

  private Date updatedAt;

  private String createdBy;

  private String updatedBy;


  // ==============  扩展属性  =================


  // ==============  扩展方法  =================



}