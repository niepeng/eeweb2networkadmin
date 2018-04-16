package com.chengqianyun.eeweb2networkadmin.service;


import com.chengqianyun.eeweb2networkadmin.biz.bean.DeviceFormBean;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.Area;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceInfo;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.OutCondition;
import com.chengqianyun.eeweb2networkadmin.biz.enums.DeviceTypeEnum;
import com.chengqianyun.eeweb2networkadmin.biz.page.PageResult;
import com.chengqianyun.eeweb2networkadmin.biz.page.PaginationQuery;
import com.chengqianyun.eeweb2networkadmin.core.utils.StringUtil;
import com.chengqianyun.eeweb2networkadmin.core.utils.UnitUtil;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author 聂鹏
 * @version 1.0
 * @date 18/4/12
 */
@Service
@Slf4j
public class DeviceService extends BaseService {


  public PageResult<Area> getAreaList(PaginationQuery query) {
    PageResult<Area> result = null;
    try {

      Integer count = areaMapper.findPageCount(query.getQueryData());

      if (null != count && count.intValue() > 0) {
        int startRecord = (query.getPageIndex() - 1) * query.getRowsPerPage();
        query.addQueryData("startRecord", Integer.toString(startRecord));
        query.addQueryData("endRecord", Integer.toString(query.getRowsPerPage()));
        List<Area> list = areaMapper.findPage(query.getQueryData());
        result = new PageResult<Area>(list, count, query);
      }
    } catch (Exception e) {
      log.error("DeviceService.getAreaList,Error", e);
    }
    return result;
  }

  public void addArea(Area area) {
    if (StringUtil.isEmpty(area.getName())) {
      throw new RuntimeException("区域名称不能为空");
    }
    Area from = areaMapper.selectByName(area.getName());
    if (from != null) {
      throw new RuntimeException("当前区域名称已经存在");
    }
    if (area.getSmsPhones() == null) {
      areaMapper.insert(area);
      return;
    }
    area.optSmsPhones();
    areaMapper.insert(area);
  }

  public Area getArea(long areaId) {
    Area area = areaMapper.selectByPrimaryKey(areaId);
    if(area == null) {
      throw new RuntimeException("当前区域已经不存在了");
    }
    return area;
  }

  public void updateArea(Area area) {
    Area fromDB = areaMapper.selectByPrimaryKey(area.getId());
    if (fromDB == null) {
      throw new RuntimeException("当前区域已经不存在了");
    }

    if (!area.getName().equals(fromDB.getName()) && areaMapper.selectByName(area.getName()) != null) {
      throw new RuntimeException("当前区域名称已经存在,请更换");
    }

    fromDB.setName(area.getName());
    fromDB.setNote(area.getNote());
    area.optSmsPhones();
    fromDB.setSmsPhones(area.getSmsPhones());
    areaMapper.updateByPrimaryKey(fromDB);
  }

  public void deleteArea(long areaId) {
    // TODO .. 是否有设备属于这个区域的,如果有,不能删除

    areaMapper.deleteByPrimaryKey(areaId);
  }

  public List<Area> getAreaAll() {
    return areaMapper.listAll();
  }


  public PageResult<DeviceInfo> getDeviceInfoList(PaginationQuery query) {
    PageResult<DeviceInfo> result = null;
    try {
      Integer count = deviceInfoMapper.findPageCount(query.getQueryData());

      if (null != count && count.intValue() > 0) {
        int startRecord = (query.getPageIndex() - 1) * query.getRowsPerPage();
        query.addQueryData("startRecord", Integer.toString(startRecord));
        query.addQueryData("endRecord", Integer.toString(query.getRowsPerPage()));
        List<DeviceInfo> list = deviceInfoMapper.findPage(query.getQueryData());
        List<Area> areaList = areaMapper.listAll();
        if (list != null) {
          for (DeviceInfo info : list) {
            info.optArea(areaList);
//            if(info.getRelationOutId() > 0) {
//              info.setRelationDeviceInfo(deviceInfoMapper.selectByPrimaryKey(info.getRelationOutId()));
//            }
          }
        }
        result = new PageResult<DeviceInfo>(list, count, query);
      }
    } catch (Exception e) {
      log.error("DeviceService.getDeviceInfoList,Error", e);
    }
    return result;
  }

  public DeviceInfo getDeviceInfo(long id) {
    DeviceInfo deviceInfo = deviceInfoMapper.selectByPrimaryKey(id);
    if(deviceInfo != null && deviceInfo.getAreaId() > 0) {
      deviceInfo.setArea(areaMapper.selectByPrimaryKey(deviceInfo.getAreaId()));
    }
    return deviceInfo;
  }

  public void addDeviceInfo(DeviceFormBean deviceFormBean) {
    // 检查sn是否唯一
    DeviceInfo fromDB = deviceInfoMapper.selectBySn(deviceFormBean.getSn());
    if (fromDB != null) {
      throw new RuntimeException("设备sn号已经存在");
    }

    DeviceInfo deviceInfo = new DeviceInfo();
    deviceInfo.setAreaId(deviceFormBean.getAreaId());
    deviceInfo.setSn(deviceFormBean.getSn());
    deviceInfo.setName(deviceFormBean.getName());
    deviceInfo.setTag(deviceFormBean.getTag());
    deviceInfo.setAddress(deviceFormBean.getAddress());
    deviceInfo.setType(deviceFormBean.calcDeviceType());

    if (DeviceTypeEnum.hasType(deviceInfo.getType(), DeviceTypeEnum.temp)) {
      deviceInfo.setTempUp(UnitUtil.changeTemp(deviceFormBean.getTempUpStr()));
      deviceInfo.setTempDown(UnitUtil.changeTemp(deviceFormBean.getTempDownStr()));
      deviceInfo.setTempDev(UnitUtil.changeTemp(deviceFormBean.getTempDevStr()));
    }

    if (DeviceTypeEnum.hasType(deviceInfo.getType(), DeviceTypeEnum.humi)) {
      deviceInfo.setHumiUp(UnitUtil.changeHumi(deviceFormBean.getHumiUpStr()));
      deviceInfo.setHumiDown(UnitUtil.changeHumi(deviceFormBean.getHumiDownStr()));
      deviceInfo.setHumiDev(UnitUtil.changeHumi(deviceFormBean.getHumiDevStr()));
    }

    if (DeviceTypeEnum.hasType(deviceInfo.getType(), DeviceTypeEnum.shine)) {
      deviceInfo.setShineUp(StringUtil.str2int(deviceFormBean.getShineUpStr()));
      deviceInfo.setShineDown(StringUtil.str2int(deviceFormBean.getShineDownStr()));
      deviceInfo.setShineDev(StringUtil.str2int(deviceFormBean.getShineDevStr()));
    }

    if (DeviceTypeEnum.hasType(deviceInfo.getType(), DeviceTypeEnum.pressure)) {
      deviceInfo.setPressureUp(UnitUtil.changePressure(deviceFormBean.getPressureUpStr()));
      deviceInfo.setPressureDown(UnitUtil.changePressure(deviceFormBean.getPressureDownStr()));
      deviceInfo.setPressureDev(UnitUtil.changePressure(deviceFormBean.getPressureDevStr()));
    }

    if (DeviceTypeEnum.hasType(deviceInfo.getType(), DeviceTypeEnum.smoke) ||
        DeviceTypeEnum.hasType(deviceInfo.getType(), DeviceTypeEnum.water) ||
        DeviceTypeEnum.hasType(deviceInfo.getType(), DeviceTypeEnum.electric)) {

      deviceInfo.setRelationOutId(StringUtil.str2int(deviceFormBean.getRelationOutId()));
      deviceInfo.setOpencloseWay((short) StringUtil.str2int(deviceFormBean.getOpencloseWay()));
      deviceInfo.setInWay((short) StringUtil.str2int(deviceFormBean.getInWay()));
    }

    if (DeviceTypeEnum.hasType(deviceInfo.getType(), DeviceTypeEnum.out)) {
      deviceInfo.setControlWay((short) StringUtil.str2int(deviceFormBean.getControlWay()));
    }

    deviceInfoMapper.insert(deviceInfo);
  }

  public List<OutCondition> getOutConditionList(long deviceId) {
    List<OutCondition> list = outConditionMapper.listByDeviceId(deviceId);
    if (list != null) {
      for (OutCondition outCondition : list) {
        outCondition.setDeviceInfo(deviceInfoMapper.selectBySn(outCondition.getDeviceSn()));
        DeviceInfo tmpDeviceInfo = new DeviceInfo();
        tmpDeviceInfo.setType(outCondition.getDeviceType());
        outCondition.setTmpDeviceInfo(tmpDeviceInfo);
      }
    }
    return list;
  }

  public void outConditionAdd(OutCondition outCondition) {
    if (StringUtil.isEmpty(outCondition.getDeviceSn())) {
      throw new RuntimeException("请填写条件设备sn号");
    }

    DeviceInfo deviceInfo = deviceInfoMapper.selectBySn(outCondition.getDeviceSn());
    if (deviceInfo == null) {
      throw new RuntimeException("当前条件设备sn号设备不存在");
    }

    DeviceTypeEnum tmpEnum = DeviceTypeEnum.getOneById(outCondition.getDeviceType());
    if (tmpEnum == null) {
      throw new RuntimeException("请选择条件设备类型");
    }

    if (!DeviceTypeEnum.hasType(deviceInfo.getType(), tmpEnum)) {
      throw new RuntimeException("当前条件设备sn号不是 " + tmpEnum.getName() + "设备");
    }

    int dataValue = getDataValue(outCondition.getDeviceType(), outCondition.getDataValueStr());
    outCondition.setDataValue(dataValue);
    outConditionMapper.insert(outCondition);
  }


  public OutCondition getCondition(long outConditionId) {
    OutCondition fromDB = outConditionMapper.selectByPrimaryKey(outConditionId);

    DeviceInfo tmpDeviceInfo = new DeviceInfo();
    tmpDeviceInfo.setType(fromDB.getDeviceType());
    fromDB.setTmpDeviceInfo(tmpDeviceInfo);
    return fromDB;
  }

  public void updateOutCondition(OutCondition outCondition) {
    OutCondition fromDB = outConditionMapper.selectByPrimaryKey(outCondition.getId());
    if (fromDB == null) {
      throw new RuntimeException("当前条件不存在");
    }

    fromDB.setDeviceSn(outCondition.getDeviceSn());
    fromDB.setOpenClosed(outCondition.getOpenClosed());
    fromDB.setDeviceType(outCondition.getDeviceType());
    fromDB.setMinMax(outCondition.getMinMax());
    int dataValue = getDataValue(outCondition.getDeviceType(), outCondition.getDataValueStr());
    fromDB.setDataValue(dataValue);
    outConditionMapper.updateByPrimaryKey(fromDB);
  }

  public void outConditionDelete(long outConditionId) {
    outConditionMapper.deleteByPrimaryKey(outConditionId);
  }




  private int getDataValue(int deviceType, String dataValueStr) {
    DeviceInfo device = new DeviceInfo();
    device.setType(deviceType);
    if (device.hasTemp()) {
      return UnitUtil.changeTemp(dataValueStr);
    }

    if (device.hasHumi()) {
      return UnitUtil.changeHumi(dataValueStr);
    }
    if (device.hasPressure()) {
      return UnitUtil.changePressure(dataValueStr);
    }

    if (device.hasPower()) {
      return UnitUtil.changePower(dataValueStr);
    }
    return StringUtil.str2int(dataValueStr);
  }


}