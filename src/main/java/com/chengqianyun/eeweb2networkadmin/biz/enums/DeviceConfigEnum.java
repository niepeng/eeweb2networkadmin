package com.chengqianyun.eeweb2networkadmin.biz.enums;

import lombok.Getter;

/**
 * 硬件设备中的类型信息
 *
 * @author 聂鹏
 * @version 1.0
 * @date 18/4/26
 */
@Getter
public enum DeviceConfigEnum {

  // 环境设备传感器
  env01("01", "温度 + 电量", DeviceTypeEnum.temp.getId() + DeviceTypeEnum.power.getId()),
  env02("02", "温湿度+电量", DeviceTypeEnum.temp.getId() +  DeviceTypeEnum.humi.getId() + DeviceTypeEnum.power.getId()),
  env03("03", "光照+电量", DeviceTypeEnum.shine.getId() + DeviceTypeEnum.power.getId()),
  env04("04", "压力+电量", DeviceTypeEnum.pressure.getId() + DeviceTypeEnum.power.getId()),
//  env05("05", "CO2+电量", DeviceTypeEnum..getId() + DeviceTypeEnum.power.getId()),
  env06("06", "温湿度 + 光照 + 电量", DeviceTypeEnum.temp.getId() + DeviceTypeEnum.humi.getId() + DeviceTypeEnum.shine.getId() + DeviceTypeEnum.power.getId()),
  env07("07", "温湿度 + 压力 + 电量", DeviceTypeEnum.temp.getId() + DeviceTypeEnum.humi.getId() + DeviceTypeEnum.pressure.getId() + DeviceTypeEnum.power.getId()),
  env08("08", "温湿度 + 光照 + 压力 + 电量", DeviceTypeEnum.temp.getId() + DeviceTypeEnum.humi.getId() + DeviceTypeEnum.shine.getId() + DeviceTypeEnum.pressure.getId() + DeviceTypeEnum.power.getId()),
//  env09("09", "温湿度 + 光照 + CO2 + 电量", DeviceTypeEnum.temp.getId() + DeviceTypeEnum.humi.getId() + DeviceTypeEnum..getId() + DeviceTypeEnum.power.getId()),


//  开关量输入设备
  in21("21", "烟感 + 电量", DeviceTypeEnum.smoke.getId() + DeviceTypeEnum.power.getId()),
  in22("22", "浸水 + 电量", DeviceTypeEnum.water.getId() + DeviceTypeEnum.power.getId()),
  in23("23", "停电来电 + 电量", DeviceTypeEnum.electric.getId() + DeviceTypeEnum.power.getId()),
  in24("24", "人体感应 + 电量", DeviceTypeEnum.body.getId() + DeviceTypeEnum.power.getId()),

  in31("31", "温湿度 + 烟感 + 电量", DeviceTypeEnum.temp.getId() +  DeviceTypeEnum.humi.getId() + DeviceTypeEnum.smoke.getId() + DeviceTypeEnum.power.getId()),
  in32("32", "温湿度 + 浸水 + 电量", DeviceTypeEnum.temp.getId() +  DeviceTypeEnum.humi.getId() + DeviceTypeEnum.water.getId() + DeviceTypeEnum.power.getId()),
  in33("33", "温湿度  + 停电来电 + 电量", DeviceTypeEnum.temp.getId() +  DeviceTypeEnum.humi.getId() + DeviceTypeEnum.electric.getId() + DeviceTypeEnum.power.getId()),
  in34("34", "温湿度  + 人体感应 + 电量", DeviceTypeEnum.temp.getId() +  DeviceTypeEnum.humi.getId() + DeviceTypeEnum.body.getId() + DeviceTypeEnum.power.getId()),


//  开关量输出设备
//  51   开关量输出 第一通道
//  52   开关量输出 第二通道
  out51("51", "开关量输出 第一通道", DeviceTypeEnum.out.getId()),
  out52("52", "开关量输出 第二通道", DeviceTypeEnum.out.getId()),


//  //全部传感器
  F1("F1", "温湿度 + 光照 + 压力 + 电量", DeviceTypeEnum.temp.getId() + DeviceTypeEnum.humi.getId() + DeviceTypeEnum.shine.getId() + DeviceTypeEnum.pressure.getId() + DeviceTypeEnum.power.getId()),
  F2("F2", "烟感 + 浸水 + 停电来电 + 人体感应", DeviceTypeEnum.smoke.getId() + DeviceTypeEnum.water.getId() + DeviceTypeEnum.electric.getId() + DeviceTypeEnum.body.getId()),
  F3("F3", "温湿度 + 光照 + 压力 + 电量  + 烟感 + 浸水 + 停电来电 + 人体感应", DeviceTypeEnum.temp.getId() + DeviceTypeEnum.humi.getId() + DeviceTypeEnum.shine.getId() + DeviceTypeEnum.pressure.getId() + DeviceTypeEnum.power.getId() + DeviceTypeEnum.smoke.getId() + DeviceTypeEnum.water.getId() + DeviceTypeEnum.electric.getId() + DeviceTypeEnum.body.getId()),
  F4("F4", "温湿度 + 光照 + 压力 + 电量  + 烟感 + 浸水 + 停电来电 + 人体感应 + 开关量输出", DeviceTypeEnum.temp.getId() + DeviceTypeEnum.humi.getId() + DeviceTypeEnum.shine.getId() + DeviceTypeEnum.pressure.getId() + DeviceTypeEnum.power.getId() + DeviceTypeEnum.smoke.getId() + DeviceTypeEnum.water.getId() + DeviceTypeEnum.electric.getId() + DeviceTypeEnum.body.getId() + DeviceTypeEnum.out
  .getId())
  ;

  private final String id;

  private final String name;

  private final int deviceType;

  private DeviceConfigEnum(String id, String name, int deviceType) {
    this.id = id;
    this.name = name;
    this.deviceType = deviceType;
  }




}