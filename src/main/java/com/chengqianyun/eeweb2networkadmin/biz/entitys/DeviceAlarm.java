package com.chengqianyun.eeweb2networkadmin.biz.entitys;

import java.util.Date;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class DeviceAlarm {
    /**
     * 主键
     */
    private Long id;

    /**
     * 设备id
     */
    private long deviceId;

    /**
     * 所属区域id
     */
    private long areaId;

    /**
     * 一种传感器类型: DeviceTypeEnum 中的一位
     */
    private int deviceOneType;

    /**
     * 报警类型 AlarmTypeEnum:1 传感器超限, 2烟感,...
     */
    private int alarmType;

    /**
     *  AlarmConfirmEnum :0未确认,1已确认
     */
    private short confirm;

    /**
     * UpDownEnum:0默认,1偏高,2偏低
     */
    private short upDown;

    /**
     * 设备读取值(报警值)
     */
    private int data;

    /**
     * 设备读取值正常范围
     */
    private String dataScope;

    /**
     * 最近一次报警时间
     */
    private Date recentlyAlarmTime;

    /**
     * 报警结束时间:如果报警没有结束,同一种类型报警不更新值,只更新最近一次报警时间
     */
    private Date alarmEndTime;

    /**
     * 人工确认时间
     */
    private Date userConfirmTime;

    /**
     * 备注
     */
    private String note;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 修改时间
     */
    private Date updatedAt;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 修改人
     */
    private String updatedBy;


    // ==============  扩展属性  =================

    private DeviceInfo deviceInfo;


    // ==============  扩展方法  =================





}