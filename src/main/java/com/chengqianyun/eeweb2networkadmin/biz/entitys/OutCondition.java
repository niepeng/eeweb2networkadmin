package com.chengqianyun.eeweb2networkadmin.biz.entitys;

import java.util.Date;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class OutCondition {
    /**
     * 主键
     */
    private Long id;

    /**
     * 关联设备信息表id
     */
    private long deviceInfoId;

    /**
     * 打开关闭条件类型:1打开,2关闭
     */
    private short openClosed;

    /**
     * 条件设备类型:温度,湿度,光照等
     */
    private int deviceType;

    /**
     * 条件设备sn号
     */
    private String deviceSn;

    /**
     * 小于大于:1小于,2大于
     */
    private short minMax;

    /**
     * 数值,参考t_device_info表中的值存储格式
     */
    private int dataValue;

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