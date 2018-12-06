package com.chengqianyun.eeweb2networkadmin.service;


import com.chengqianyun.eeweb2networkadmin.biz.bean.DeviceFormBean;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.Area;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.Contacts;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceInfo;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.OutCondition;
import com.chengqianyun.eeweb2networkadmin.biz.enums.DeviceTypeEnum;
import com.chengqianyun.eeweb2networkadmin.biz.page.PageResult;
import com.chengqianyun.eeweb2networkadmin.biz.page.PaginationQuery;
import com.chengqianyun.eeweb2networkadmin.core.utils.StringUtil;
import com.chengqianyun.eeweb2networkadmin.core.utils.UnitUtil;
import com.chengqianyun.eeweb2networkadmin.data.ServerConnectionManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

  public List<Area> getAreaAndDeviceInfo() {
    Area area = new Area();
    area.setId(0L);
    area.setName("未定义");
    List<Area> areaList = getAreaAll();
    List<DeviceInfo> deviceInfoList = deviceInfoMapper.findAll();
    if (areaList == null || areaList.size() == 0) {
      areaList = new ArrayList<Area>();
      areaList.add(area);
      area.setDeviceInfoList(deviceInfoList);
      return areaList;
    }

    areaList.add(0, area);

    for (Area tmpArea : areaList) {
      for (DeviceInfo deviceInfo : deviceInfoList) {
        if (tmpArea.getId().longValue() == deviceInfo.getAreaId()) {
          tmpArea.addDeviceInfo(deviceInfo);
        }
      }
    }

    return areaList;
  }


  public PageResult<Area> getAreaList(PaginationQuery query) {
    PageResult<Area> result = null;
    try {

      Integer count = areaMapper.findPageCount(query.getQueryData());

      if (null != count && count.intValue() > 0) {
        int startRecord = (query.getPageIndex() - 1) * query.getRowsPerPage();
        query.addQueryData("startRecord", Integer.toString(startRecord));
        query.addQueryData("endRecord", Integer.toString(query.getRowsPerPage()));
        List<Area> list = areaMapper.findPage(query.getQueryData());
        if(list != null) {
          List<Contacts> contactsList = contactsMapper.selectAll();
          for(Area area : list) {
            optAreaContacts(area, contactsList);
          }
        }
        result = new PageResult<Area>(list, count, query);
      }
    } catch (Exception e) {
      log.error("DeviceService.getAreaList,Error", e);
    }
    return result;
  }

  public void optAreaContacts(Area area, List<Contacts> contactsList) {
    if (contactsList == null || contactsList.size() == 0 || area == null || StringUtil.isEmpty(area.getContactsIds())) {
      return;
    }
    List<Contacts> list = new ArrayList<Contacts>();
    String[] ids = area.getContactsIds().split(",");
    for (String idStr : ids) {
      long id = StringUtil.str2long(idStr);
      for (Contacts c : contactsList) {
        if (c.getId().longValue() == id) {
          list.add(c);
          break;
        }
      }
    }
    area.setContactsList(list);
  }


  public void addArea(Area area) {
    if (StringUtil.isEmpty(area.getName())) {
      throw new RuntimeException("区域名称不能为空");
    }
    Area from = areaMapper.selectByName(area.getName());
    if (from != null) {
      throw new RuntimeException("当前区域名称已经存在");
    }
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
    areaMapper.updateByPrimaryKey(fromDB);
  }

  public void updateAreaContacts(Area area) {
    Area fromDB = getArea(area.getId());
    fromDB.setContactsIds(area.getContactsIds());
    areaMapper.updateByPrimaryKey(fromDB);
  }

  public void deleteArea(long areaId) {
    // 是否有设备属于这个区域的,如果有,不能删除
    Map<String, String> map = new HashMap<String, String>();
    map.put("areaId", String.valueOf(areaId));
    Integer num = deviceInfoMapper.findPageCount(map);
    if (num != null && num > 0) {
      throw new RuntimeException("当前区域存在设备,暂无法删除");
    }

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
//    deviceInfo.setAddress(deviceFormBean.getAddress());
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

  public void updateDevice(DeviceFormBean deviceFormBean) {
    DeviceInfo deviceInfo = deviceInfoMapper.selectByPrimaryKey(StringUtil.str2long(deviceFormBean.getId()));
    if (deviceInfo == null) {
      throw new RuntimeException("当前设备不存在,请打开设备列表重新操作");
    }

    deviceInfo.setName(deviceFormBean.getName());
    deviceInfo.setAreaId(deviceFormBean.getAreaId());
    deviceInfo.setTag(deviceFormBean.getTag());
    if(deviceFormBean.getAddress() > 0) {
      deviceInfo.setAddress(deviceFormBean.getAddress());
    }
    deviceInfo.setType(deviceFormBean.calcDeviceType());

    if (DeviceTypeEnum.hasType(deviceInfo.getType(), DeviceTypeEnum.temp)) {
      deviceInfo.setTempUp(UnitUtil.changeTemp(deviceFormBean.getTempUpStr()));
      deviceInfo.setTempDown(UnitUtil.changeTemp(deviceFormBean.getTempDownStr()));
      deviceInfo.setTempDev(UnitUtil.changeTemp(deviceFormBean.getTempDevStr()));
    } else {
      deviceInfo.setTempUp(0);
      deviceInfo.setTempDown(0);
      deviceInfo.setTempDev(0);
    }

    if (DeviceTypeEnum.hasType(deviceInfo.getType(), DeviceTypeEnum.humi)) {
      deviceInfo.setHumiUp(UnitUtil.changeHumi(deviceFormBean.getHumiUpStr()));
      deviceInfo.setHumiDown(UnitUtil.changeHumi(deviceFormBean.getHumiDownStr()));
      deviceInfo.setHumiDev(UnitUtil.changeHumi(deviceFormBean.getHumiDevStr()));
    } else {
      deviceInfo.setHumiUp(0);
      deviceInfo.setHumiDown(0);
      deviceInfo.setHumiDev(0);
    }

    if (DeviceTypeEnum.hasType(deviceInfo.getType(), DeviceTypeEnum.shine)) {
      deviceInfo.setShineUp(StringUtil.str2int(deviceFormBean.getShineUpStr()));
      deviceInfo.setShineDown(StringUtil.str2int(deviceFormBean.getShineDownStr()));
      deviceInfo.setShineDev(StringUtil.str2int(deviceFormBean.getShineDevStr()));
    } else {
      deviceInfo.setShineUp(0);
      deviceInfo.setShineDown(0);
      deviceInfo.setShineDev(0);
    }

    if (DeviceTypeEnum.hasType(deviceInfo.getType(), DeviceTypeEnum.pressure)) {
      deviceInfo.setPressureUp(UnitUtil.changePressure(deviceFormBean.getPressureUpStr()));
      deviceInfo.setPressureDown(UnitUtil.changePressure(deviceFormBean.getPressureDownStr()));
      deviceInfo.setPressureDev(UnitUtil.changePressure(deviceFormBean.getPressureDevStr()));
    } else {
      deviceInfo.setPressureUp(0);
      deviceInfo.setPressureDown(0);
      deviceInfo.setPressureDev(0);
    }

    if (DeviceTypeEnum.hasType(deviceInfo.getType(), DeviceTypeEnum.smoke) ||
        DeviceTypeEnum.hasType(deviceInfo.getType(), DeviceTypeEnum.water) ||
        DeviceTypeEnum.hasType(deviceInfo.getType(), DeviceTypeEnum.electric)) {

      deviceInfo.setRelationOutId(StringUtil.str2int(deviceFormBean.getRelationOutId()));
      deviceInfo.setOpencloseWay((short) StringUtil.str2int(deviceFormBean.getOpencloseWay()));
      deviceInfo.setInWay((short) StringUtil.str2int(deviceFormBean.getInWay()));
    } else {
      deviceInfo.setRelationOutId(0);
      deviceInfo.setOpencloseWay((short) 0);
      deviceInfo.setInWay((short) 0);
    }

    if (DeviceTypeEnum.hasType(deviceInfo.getType(), DeviceTypeEnum.out)) {
      deviceInfo.setControlWay((short) StringUtil.str2int(deviceFormBean.getControlWay()));
    } else {
      deviceInfo.setControlWay((short) 0);
    }

    deviceInfoMapper.updateByPrimaryKeySelective(deviceInfo);
    ServerConnectionManager.updateDeviceInfo(deviceInfo.getSn());
  }

  public void deleteDevice(long id) {
    // 注意:校验在开关量条件中是否存在设备,如果存在不能删除
    DeviceInfo deviceInfo = deviceInfoMapper.selectByPrimaryKey(id);
    if (deviceInfo == null) {
      return;
    }

    List<OutCondition> conditionList = outConditionMapper.selectConditionSn(deviceInfo.getSn());
    if (conditionList != null && conditionList.size() > 0) {
      throw new RuntimeException("该设备存在于开关量条件中,请先删除开关量中的条件");
    }
    deviceInfoMapper.deleteByPrimaryKey(id);
    deviceDataIntimeMapper.deleteAllByDeviceId(id);
    ServerConnectionManager.removeDeviceInfo(deviceInfo.getSn());
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

    int dataValue = UnitUtil.getDataValue(outCondition.getDeviceType(), outCondition.getDataValueStr());
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
    int dataValue = UnitUtil.getDataValue(outCondition.getDeviceType(), outCondition.getDataValueStr());
    fromDB.setDataValue(dataValue);
    outConditionMapper.updateByPrimaryKey(fromDB);
  }

  public void outConditionDelete(long outConditionId) {
    outConditionMapper.deleteByPrimaryKey(outConditionId);
  }

}