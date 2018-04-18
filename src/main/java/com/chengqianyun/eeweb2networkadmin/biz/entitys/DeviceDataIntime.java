package com.chengqianyun.eeweb2networkadmin.biz.entitys;

import java.util.Date;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class DeviceDataIntime {
    /**
     * 主键
     */
    private Long id;

    /**
     * 设备id
     */
    private long deviceId;

    /**
     * StatusEnum 状态:1正常,2报警,3离线
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
     * 电量:3.6v,存储360
     */
    private int power;

    /**
     * 光照:20000,存储直接值
     */
    private int shine;

    /**
     * 压力:800.2,存储80020
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

    private long areaId;

    private DeviceInfo deviceInfo;


    // ==============  扩展方法  =================

    public boolean isTempUp() {
        return temp - deviceInfo.getTempDev() > deviceInfo.getTempUp();
    }

    public boolean isTempDown() {
        return temp + deviceInfo.getTempDev() < deviceInfo.getTempDown();
    }


    public boolean isHumiUp() {
        return humi - deviceInfo.getHumiDev() > deviceInfo.getHumiUp();
    }

    public boolean isHumiDown() {
        return humi + deviceInfo.getHumiDev() < deviceInfo.getHumiDown();
    }


    public boolean isShineUp() {
        return shine - deviceInfo.getShineDev() > deviceInfo.getShineUp();
    }

    public boolean isShineDown() {
        return shine + deviceInfo.getShineDev() < deviceInfo.getShineDown();
    }


    public boolean isPressureUp() {
        return pressure - deviceInfo.getPressureDev() > deviceInfo.getPressureUp();
    }

    public boolean isPressureDown() {
        return pressure + deviceInfo.getPressureDev() < deviceInfo.getPressureDown();
    }

}