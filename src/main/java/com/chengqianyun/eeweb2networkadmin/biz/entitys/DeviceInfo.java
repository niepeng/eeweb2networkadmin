package com.chengqianyun.eeweb2networkadmin.biz.entitys;

import java.util.Date;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class DeviceInfo {
    /**
     * 主键
     */
    private Long id;

    /**
     * 所属区域
     */
    private long areaId;

    /**
     * sn号
     */
    private String sn;

    /**
     * 名称
     */
    private String name;

    /**
     * 标签
     */
    private String tag;

    /**
     * 设备地址
     */
    private int address;

    /**
     * 设备类型:按位存储
     */
    private int type;

    /**
     * 温度上限:23.34,存储2334
     */
    private int tempUp;

    /**
     * 温度下限:23.34,存储2334
     */
    private int tempDown;

    /**
     * 温度校正值:23.34,存储2334
     */
    private int tempDev;

    /**
     * 湿度上限:45.67,存储4567
     */
    private int humiUp;

    /**
     * 湿度下限:45.67,存储4567
     */
    private int humiDown;

    /**
     * 湿度校正值:45.67,存储4567
     */
    private int humiDev;

    /**
     * 光照上限
     */
    private int shineUp;

    /**
     * 光照下限
     */
    private int shineDown;

    /**
     * 光照校正值
     */
    private int shineDev;

    /**
     * 压力上限
     */
    private int pressureUp;

    /**
     * 压力下限
     */
    private int pressureDown;

    /**
     * 压力校正值
     */
    private int pressureDev;

    /**
     * 只有开关量输出才有值:控制通道
     */
    private short controlWay;

    /**
     * 只有开关量输入才有值:关联对应的开关量输出的id(本表的id)
     */
    private long relationOutId;

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