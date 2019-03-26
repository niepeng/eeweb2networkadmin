package com.chengqianyun.eeweb2networkadmin.biz.enums;


import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

/**
 * @author 聂鹏
 * @version 1.0
 * @date 18/4/12
 */
@Getter
public enum MenuEnum {

  intime("icon-tasks","intime", null, 1, "实时管理", RoleEnum.values(), null),
    intime_dataList(null, "intime_dataList", "intime", 2, "实时数据",RoleEnum.values(), "intime/dataList"),
    intime_curveList(null, "intime_curveList", "intime", 2, "实时曲线",RoleEnum.values(), "intime/intimeCurveList"),


  alarm("icon-bell","alarm", null, 1, "报警记录", RoleEnum.values(), null),
    alarm_opt(null,"alarm_opt",null, 0, "报警操作", new RoleEnum[]{RoleEnum.MANAGER, RoleEnum.ADMIN ,RoleEnum.ROOT}, "alarm/delete,alarm/writeNote,alarm/markRead"),
    alarm_list(null,"alarm_list","alarm", 2, "报警数据", RoleEnum.values(), "alarm/alarmList"),


  history("icon-signal","history", null, 1, "历史管理", RoleEnum.values(), null),
    history_list(null,"history_list","history", 2, "历史数据", RoleEnum.values(), "history/historyList"),
    history_curveList(null,"history_curveList","history", 2, "历史曲线", RoleEnum.values(), "history/historyCurveList"),


  device("icon-inbox","device", null, 1, "设备管理", new RoleEnum[]{RoleEnum.MANAGER, RoleEnum.ADMIN ,RoleEnum.ROOT}, null),
    device_arealist(null,"device_arealist","device", 2, "区域管理", new RoleEnum[]{RoleEnum.MANAGER, RoleEnum.ADMIN ,RoleEnum.ROOT}, "device/areaList"),
    device_list(null,"device_list","device", 2, "设备管理", new RoleEnum[]{RoleEnum.MANAGER, RoleEnum.ADMIN ,RoleEnum.ROOT}, "device/deviceList"),


  wirelessSetting("icon-envelope-alt","wirelessSetting", null, 1, "短信设置", new RoleEnum[]{RoleEnum.MANAGER, RoleEnum.ADMIN ,RoleEnum.ROOT}, null),
    wirelessSetting_normal(null,"wirelessSetting_normal","wirelessSetting", 2, "通讯状态", new RoleEnum[]{RoleEnum.MANAGER, RoleEnum.ADMIN ,RoleEnum.ROOT}, "wirelessSetting/normal"),
    wirelessSetting_list(null,"wirelessSetting_list","wirelessSetting", 2, "通讯列表", new RoleEnum[]{RoleEnum.MANAGER, RoleEnum.ADMIN ,RoleEnum.ROOT}, "wirelessSetting/list"),
    wirelessSetting_history(null,"wirelessSetting_history","wirelessSetting", 2, "通讯历史", new RoleEnum[]{RoleEnum.MANAGER, RoleEnum.ADMIN ,RoleEnum.ROOT}, "wirelessSetting/history"),


  setting("icon-cog","setting", null,  1, "系统设置", new RoleEnum[]{RoleEnum.MANAGER, RoleEnum.ADMIN ,RoleEnum.ROOT}, null),
    setting_normal(null,"setting_normal","setting", 2, "基本设置", new RoleEnum[]{RoleEnum.MANAGER, RoleEnum.ADMIN ,RoleEnum.ROOT}, "setting/normal"),
    setting_alarm(null,"setting_alarm","setting", 2, "报警设置", new RoleEnum[]{RoleEnum.MANAGER, RoleEnum.ADMIN ,RoleEnum.ROOT}, "setting/alarm"),
    setting_email(null,"setting_email","setting", 2, "邮件设置", new RoleEnum[]{RoleEnum.MANAGER, RoleEnum.ADMIN ,RoleEnum.ROOT}, "setting/email"),
    setting_inout_config(null,"setting_inout_config","setting", 2, "导入导出配置", new RoleEnum[]{RoleEnum.ADMIN ,RoleEnum.ROOT}, "setting/inoutConfig"),


  account("icon-user-md","account", null,  1, "账户管理", new RoleEnum[]{RoleEnum.ADMIN ,RoleEnum.ROOT}, null),
    account_list(null,"account_list", "account",  2, "账户列表", new RoleEnum[]{RoleEnum.ADMIN ,RoleEnum.ROOT}, "account/accountList"),

  ;

  private final String icon;

  private final String name;

  private final String parentName;

  // 如果为0非菜单, 1一级菜单,  2二级菜单
  private final int level;

  private final String meaning;

  // 拥有该权限的角色列表
  private final RoleEnum[] roles;

  // 访问地址s, 直接在这里做关联,权限通过枚举去判断
  private String visitUrl;

  private MenuEnum(String icon, String name, String parentName, int level, String meaning, RoleEnum[] roles, String visitUrl) {
    this.icon = icon;
    this.name = name;
    this.parentName = parentName;
    this.level = level;
    this.meaning = meaning;
    this.roles = roles;
    this.visitUrl = visitUrl;
  }

  public static MenuEnum findMenu(String name) {
    for(MenuEnum tmp : MenuEnum.values()) {
      if(tmp.getName().equalsIgnoreCase(name)) {
        return tmp;
      }
    }
    return null;
  }

  public boolean isFirstLevel() {
    return this.level == 1;
  }

  public boolean isSecondLevel() {
    return this.level == 2;
  }

  public boolean hasPermission(long roleId) {
    int roleInt = (int) roleId;
    for (RoleEnum r : roles) {
      if (r.getRoleId() == roleInt) {
        return true;
      }
    }
    return false;
  }

  private List<MenuEnum> childrenList;

  public void addChild(MenuEnum menuEnum) {
    if(childrenList == null) {
      childrenList = new ArrayList<MenuEnum>();
    }
    childrenList.add(menuEnum);
  }

  public void clearChildren() {
    childrenList = null;
  }

}