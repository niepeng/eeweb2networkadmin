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
     * 报警类型按照位存储: 温度 + 湿度 + 电量 + 光照 + 压力 +  烟感(开关量输入) + 浸水(开关量输入)  +停电来电(开关量输入) +  开关量输出(报警器):0低,1高或者不区分;
     */
    private int alarmType;

    /**
     * 状态:1系统报警,2系统确认,3人工确认
     */
    private int status;

    /**
     * 温度:23.34,存储2334
     */
    private int temp;

    /**
     * 湿度:45.67,存储4567
     */
    private int humi;

    /**
     * 电量:300,代表3v
     */
    private int power;

    /**
     * 光照:20000,存储直接值
     */
    private int shine;

    /**
     * 压力:10000,存储实际值
     */
    private int pressure;

    /**
     * 烟感:0没有报警,1报警
     */
    private short smoke;

    /**
     * 浸水:0没有报警,1报警
     */
    private short water;

    /**
     * 停电来电:0停电,1来电
     */
    private short electric;

    /**
     * 开关量输出(报警器)0没有输出,1有输出
     */
    private short out;

    /**
     * 最近一次报警时间
     */
    private Date recentlyAlarmTime;

    /**
     * 系统确认时间
     */
    private Date systemConfirmTime;

    /**
     * 人工确认时间
     */
    private Date userConfirmTime;

    /**
     * 温度正常范围
     */
    private String tempNormal;

    /**
     * 温度正常范围
     */
    private String humiNormal;

    /**
     * 电压正常范围
     */
    private String powerNormal;

    /**
     * 光照正常范围
     */
    private String shineNormal;

    /**
     * 压力正常范围
     */
    private String pressureNormal;

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


    // ==============  扩展方法  =================


}