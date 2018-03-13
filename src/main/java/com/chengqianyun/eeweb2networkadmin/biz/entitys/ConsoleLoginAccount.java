package com.chengqianyun.eeweb2networkadmin.biz.entitys;


import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @author 聂鹏
 * @version 1.0
 * @date 18/3/13
 */
@Data
public class ConsoleLoginAccount implements Serializable {

  private Integer id;
  private String vcLoginName;
  private String vcLoginPassword;
  private String vcRealName;
  /**
   * 手机号码
   */
  private String vcPhone;
  private Integer iValid;
  private Date lockTime;
  private Date dtCreate;
  private Date dtModify;
  private Long roleId;

  // 上次登陆时间
  private String lastLoginTime;
  private String lastLoginIp;


  // ============ 扩展属性 =================

  // 角色
//  private AdminRole adminRole;
  // 权限地址集合
  private String authUrls;
  // 权限代码集合
  private String authCodes;
  // 所有权限集合 可优化
  private String allAuthUrls;



  // ============ 扩展方法 =================


}