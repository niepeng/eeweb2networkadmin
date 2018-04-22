package com.chengqianyun.eeweb2networkadmin.biz.entitys;

import com.chengqianyun.eeweb2networkadmin.core.utils.BizConstant;
import java.util.Date;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class DeviceDataHistory {
    /**
     * 主键
     */
    private Long id;

    /**
     * 设备id
     */
    private long deviceId;

    /**
     * 状态:1正常,2报警,3离线
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
     * 跑冒滴漏:0没有报警,1报警
     */
    private short water;

    /**
     * 断电来电:0来电,1断电(报警)
     */
    private short electric;

    /**
     * 人体感应:0没有报警,1报警
     */
    private short body;

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

    public boolean isPowerUp() {
        return power > BizConstant.power_max;
    }

    public boolean isPowerDown() {
        return power < BizConstant.power_min;
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

    public boolean isSmokeAlarm() {
        return smoke == (short) 1;
    }

    public boolean isWaterAlarm() {
        return water == (short) 1;
    }

    public boolean isElectricAlarm() {
        return electric == (short) 1;
    }

    public boolean isBodyAlarm() {
        return body == (short) 1;
    }

    public boolean isOutAlarm() {
        return out == (short) 1;
    }



}