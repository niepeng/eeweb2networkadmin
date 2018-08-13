package com.chengqianyun.eeweb2networkadmin.biz.bean;


import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceDataHistory;
import com.chengqianyun.eeweb2networkadmin.core.utils.UnitUtil;
import lombok.Data;
import lombok.ToString;

/**
 * @author 聂鹏
 * @version 1.0
 * @date 18/8/13
 */
@Data
@ToString
public class DeviceDataHistoryBean {


  private String num;

  private String time;

  /**
   * 温度
   */
  private int tempMin;
  private int tempMax;
  private int tempAvg;

  /**
   * 湿度
   */
  private int humiMin;
  private int humiMax;
  private int humiAvg;

  /**
   * 电量
   */
  private int powerMin;
  private int powerMax;
  private int powerAvg;

  /**
   * 光照
   */
  private int shineMin;
  private int shineMax;
  private int shineAvg;

  /**
   * 压力
   */
  private int pressureMin;
  private int pressureMax;
  private int pressureAvg;


  /**
   * 导出的地方使用
   * @return
   */
  public String getTempMinStr() {
    return UnitUtil.changeTemp(tempMin);
  }
  public String getTempMaxStr() {
    return UnitUtil.changeTemp(tempMax);
  }
  public String getTempAvgStr() {
    return UnitUtil.changeTemp(tempAvg);
  }


  public String getHumiMinStr() {
    return UnitUtil.changeHumi(humiMin);
  }
  public String getHumiMaxStr() {
    return UnitUtil.changeHumi(humiMax);
  }
  public String getHumiAvgStr() {
    return UnitUtil.changeHumi(humiAvg);
  }



  public String getPowerMinStr() {
    return UnitUtil.changePower(powerMin);
  }
  public String getPowerMaxStr() {
    return UnitUtil.changePower(powerMax);
  }
  public String getPowerAvgStr() {
    return UnitUtil.changePower(powerAvg);
  }



  public String getPressureMinStr() {
    return UnitUtil.changePressure(pressureMin);
  }
  public String getPressureMaxStr() {
    return UnitUtil.changePressure(pressureMax);
  }
  public String getPressureAvgStr() {
    return UnitUtil.changePressure(pressureAvg);
  }


  public String getShineMinStr() {
    return String.valueOf(shineMin);
  }
  public String getShineMaxStr() {
    return String.valueOf(shineMax);
  }
  public String getShineAvgStr() {
    return String.valueOf(shineAvg);
  }





  public void addValue(DeviceDataHistory bean) {
    if(bean.getTemp() > 0) {
      tempMin = Math.min(tempMin, bean.getTemp());
      tempMax = Math.max(tempMax, bean.getTemp());
      tempAvg += bean.getTemp();
    }

    if(bean.getHumi() > 0) {
      humiMin = Math.min(humiMin, bean.getHumi());
      humiMax = Math.max(humiMax, bean.getHumi());
      humiAvg  += bean.getHumi();
    }

    if(bean.getPower() > 0) {
      powerMin = Math.min(powerMin, bean.getPower());
      powerMax = Math.max(powerMax, bean.getPower());
      powerAvg  += bean.getPower();
    }

    if(bean.getShine() > 0) {
      shineMin = Math.min(shineMin, bean.getShine());
      shineMax = Math.max(shineMax, bean.getShine());
      shineAvg  += bean.getShine();
    }

    if(bean.getPressure() > 0) {
      pressureMin = Math.min(pressureMin, bean.getPressure());
      pressureMax = Math.max(pressureMax, bean.getPressure());
      pressureAvg  += bean.getPressure();
    }
  }

  public void calcAvg(int num) {
    tempAvg = tempAvg / num;
    humiAvg = humiAvg / num;
    powerAvg = powerAvg / num;
    shineAvg = shineAvg / num;
    pressureAvg = pressureAvg / num;
  }

}