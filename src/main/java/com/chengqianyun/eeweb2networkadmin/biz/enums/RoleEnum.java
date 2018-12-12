package com.chengqianyun.eeweb2networkadmin.biz.enums;


import lombok.Getter;

/**
 * 角色枚举
 *
 * @version 1.0
 * @date 18/12/10
 */
@Getter
public enum RoleEnum {

  NORMAL(1,"普通用户"),
  ADMIN(4,"管理员"),
  ROOT(8,"厂家");

  private int roleId;

  private String meaning;


  private RoleEnum(int roleId,  String meaning) {
    this.roleId = roleId;
    this.meaning = meaning;
  }

  public static RoleEnum find(int roleId) {
    for (RoleEnum roleEnum : RoleEnum.values()) {
      if (roleEnum.getRoleId() == roleId) {
        return roleEnum;
      }
    }
    return null;
  }

}