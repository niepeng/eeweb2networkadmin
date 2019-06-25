package com.chengqianyun.eeweb2networkadmin.biz.entitys;

import com.chengqianyun.eeweb2networkadmin.biz.enums.DeviceTypeEnum;
import com.chengqianyun.eeweb2networkadmin.core.utils.UnitUtil;
import com.chengqianyun.eeweb2networkadmin.data.ServerClientHandler;
import java.util.Date;
import java.util.List;
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
     * 设备类型: DeviceTypeEnum 按位存储 的 相加
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
     * 光照上限:原值
     */
    private int shineUp;

    /**
     * 光照下限:原值
     */
    private int shineDown;

    /**
     * 光照校正值:原值
     */
    private int shineDev;

    /**
     * 压力上限:800.1,存储80010
     */
    private int pressureUp;

    /**
     * 压力下限:800.1,存储80010
     */
    private int pressureDown;

    /**
     * 压力校正值:800.1,存储80010
     */
    private int pressureDev;

    /**
     * 只有开关量输出才有值:控制通道
     */
    private short controlWay;

    /**
     * 废弃:只有开关量输入才有值: 联动地址
     */
    private long relationOutId;

    /**
     * 废弃:只有开关量输入才有值:联动开关通道
     */
    private short opencloseWay;

    /**
     * 废弃:只有开关量输入才有值:输入通道
     */
    private short inWay;

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

    private Area area;

    /**
     * 开关量输出:打开关闭条件列表
     */
    private List<OutCondition> outConditionList;


    private ServerClientHandler serverClientHandler;


    // ==============  扩展方法  =================

    public String showName() {
        if(area != null) {
            return area.getName() + "-" + name;
        }
        return name;
    }

    public void optArea(List<Area> list) {
        if (list == null) {
            return;
        }

        for (Area tmp : list) {
            if (areaId == tmp.getId()) {
                this.area = tmp;
                return;
            }
        }
    }

    public String getTempScope() {
        return UnitUtil.showValueAndUnit(tempDown - tempDev, DeviceTypeEnum.temp) + " ~ " + UnitUtil.showValueAndUnit(tempUp + tempDev, DeviceTypeEnum.temp);
    }

    public String getHumiScope() {
        return UnitUtil.showValueAndUnit(humiDown - humiDev, DeviceTypeEnum.humi) + " ~ " + UnitUtil.showValueAndUnit(humiUp + humiDev, DeviceTypeEnum.humi);
    }

    public String getShineScope() {
        return UnitUtil.showValueAndUnit(shineDown - shineDev, DeviceTypeEnum.shine) + " ~ " + UnitUtil.showValueAndUnit(shineUp + shineDev, DeviceTypeEnum.shine);
    }

    public String getPowerScope() {
        //TODO. 电压范围..
        return UnitUtil.showValueAndUnit(150, DeviceTypeEnum.power) + " ~ " + UnitUtil.showValueAndUnit(360, DeviceTypeEnum.power);
    }

    public String getPressureScope() {
        return UnitUtil.showValueAndUnit(pressureDown - pressureDev, DeviceTypeEnum.pressure) + " ~ " + UnitUtil.showValueAndUnit(pressureUp + pressureDev, DeviceTypeEnum.pressure);
    }


    public boolean hasTemp() {
        return DeviceTypeEnum.hasType(type, DeviceTypeEnum.temp);
    }

    public boolean hasHumi() {
        return DeviceTypeEnum.hasType(type, DeviceTypeEnum.humi);
    }

    public boolean hasPower() {
        return DeviceTypeEnum.hasType(type, DeviceTypeEnum.power);
    }

    public boolean hasShine() {
        return DeviceTypeEnum.hasType(type, DeviceTypeEnum.shine);
    }

    public boolean hasPressure() {
        return DeviceTypeEnum.hasType(type, DeviceTypeEnum.pressure);
    }

    public boolean hasSmoke() {
        return DeviceTypeEnum.hasType(type, DeviceTypeEnum.smoke);
    }

    public boolean hasWater() {
        return DeviceTypeEnum.hasType(type, DeviceTypeEnum.water);
    }

    public boolean hasElectric() {
        return DeviceTypeEnum.hasType(type, DeviceTypeEnum.electric);
    }

    public boolean hasBody() {
        return DeviceTypeEnum.hasType(type, DeviceTypeEnum.body);
    }

    public boolean hasOut() {
        return DeviceTypeEnum.hasType(type, DeviceTypeEnum.out);
    }

}