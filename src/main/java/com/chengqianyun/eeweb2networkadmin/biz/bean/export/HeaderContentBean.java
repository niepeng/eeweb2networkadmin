package com.chengqianyun.eeweb2networkadmin.biz.bean.export;


import com.chengqianyun.eeweb2networkadmin.biz.entitys.Area;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceInfo;
import com.chengqianyun.eeweb2networkadmin.biz.enums.DeviceTypeEnum;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.ToString;

/**
 * 非核心数据部分的头部内容bean
 *
 * @version 1.0
 * @date 18/7/24
 */
@Data
@ToString
public class HeaderContentBean {

  private DeviceInfo deviceInfo;

  private Area area;

  private String tempMin;
  private String tempMax;

  private String humiMin;
  private String humiMax;

  private String shineMin;
  private String shineMax;

  private String pressureMin;
  private String pressureMax;

  private String startTime;
  private String endTime;
  // 时间间隔:单位分钟
  private String distanceTimes;
  // 记录条数
  private int recordNum;

  /**
   * 标题占用列数
   */
  private int titleCol;

  /**
   * 头部列数
   */
  private int headCol = 5;

  /**
   * 除去标题外,头部内容数据
   */
  private List<String[]> headDataList;

  /**
   *
   * 设备信息	  设备名：测试1	                SN：G2100501	                  区域：区域一	        设备类型：温湿度光照压力
   设备最值	    温度最大值：31.1℃	            温度最小值：27.1℃	              湿度最大值：95%	      湿度最小值：63%
              光照最大值：3000lux	          光照最小值：200lux	            压力最大值：120.1kPa	压力最小值：101.1kPa
   时间段	    开始时间：2018-5-8  23:30:00	结束时间：2018-7-15  13:25:00	  时间间隔:60分钟	      记录条数：14条

   */
  public void calcHeadDataList() {
    if (headDataList != null) {
      return;
    }

    String deviceTypeName = "";
    int deviceTypeNum = 0;
    List<String> minMaxValue = new ArrayList<String>();
    if (DeviceTypeEnum.hasType(deviceInfo.getType(), DeviceTypeEnum.temp)) {
      minMaxValue.add("温度最大值:" + tempMax + DeviceTypeEnum.temp.getUnit());
      minMaxValue.add("温度最小值:" + tempMin + DeviceTypeEnum.temp.getUnit());
      deviceTypeName += DeviceTypeEnum.temp.getName();
      deviceTypeNum++;
    }
    if (DeviceTypeEnum.hasType(deviceInfo.getType(), DeviceTypeEnum.humi)) {
      minMaxValue.add("湿度最大值:" + humiMax + DeviceTypeEnum.humi.getUnit());
      minMaxValue.add("湿度最小值:" + humiMin + DeviceTypeEnum.humi.getUnit());
      deviceTypeName += DeviceTypeEnum.humi.getName();
      deviceTypeNum++;
    }
    if (DeviceTypeEnum.hasType(deviceInfo.getType(), DeviceTypeEnum.shine)) {
      minMaxValue.add("光照最大值:" + shineMax + DeviceTypeEnum.shine.getUnit());
      minMaxValue.add("光照最小值:" + shineMin + DeviceTypeEnum.shine.getUnit());
      deviceTypeName += DeviceTypeEnum.shine.getName();
      deviceTypeNum++;
    }
    if (DeviceTypeEnum.hasType(deviceInfo.getType(), DeviceTypeEnum.pressure)) {
      minMaxValue.add("压力最大值:" + pressureMax + DeviceTypeEnum.pressure.getUnit());
      minMaxValue.add("压力最小值:" + pressureMin + DeviceTypeEnum.pressure.getUnit());
      deviceTypeName += DeviceTypeEnum.pressure.getName();
      deviceTypeNum++;
    }

    String[] line1 = {"设备信息", "设备名:" + deviceInfo.getName(), "SN:" + deviceInfo.getSn(), "区域:" + area != null ? area.getName() : "未定义", "设备类型:" + deviceTypeName};
    String[] line2 = new String[5];
    String[] line3 = null;
    String[] line4 = {"时间段", "开始时间:" + startTime, "结束时间:" + endTime, "时间间隔:" + distanceTimes, "记录条数:" + recordNum};
    if (deviceTypeNum > 2) {
      line2[0] = "设备最值";
      line2[1] = minMaxValue.get(0);
      line2[2] = minMaxValue.get(1);
      line2[3] = minMaxValue.get(2);
      line2[4] = minMaxValue.get(3);

      line3 = new String[5];
      line3[0] = "";
      line3[1] = minMaxValue.get(4);
      line3[2] = minMaxValue.get(5);
      if (minMaxValue.size() > 6) {
        line3[3] = minMaxValue.get(6);
        line3[4] = minMaxValue.get(7);
      } else {
        line3[3] = "";
        line3[4] = "";
      }
    } else {
      line2[0] = "设备最值";
      if (deviceTypeNum > 0) {
        line2[1] = minMaxValue.get(0);
        line2[2] = minMaxValue.get(1);
      }
      if (deviceTypeNum > 2) {
        line2[3] = minMaxValue.get(2);
        line2[4] = minMaxValue.get(3);
      }
    }


    titleCol = Math.max(5,deviceTypeNum + 2);

    headDataList = new ArrayList<String[]>();
    headDataList.add(line1);
    headDataList.add(line2);
    if (line3 != null) {
      headDataList.add(line3);
    }
    headDataList.add(line4);
  }
}