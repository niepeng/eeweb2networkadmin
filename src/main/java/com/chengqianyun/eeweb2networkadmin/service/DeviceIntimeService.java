package com.chengqianyun.eeweb2networkadmin.service;

import com.chengqianyun.eeweb2networkadmin.biz.bean.DataIntimeBean;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.Area;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceDataIntime;
import com.chengqianyun.eeweb2networkadmin.biz.enums.DeviceTypeEnum;
import com.chengqianyun.eeweb2networkadmin.biz.enums.StatusEnum;
import com.chengqianyun.eeweb2networkadmin.core.utils.StringUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 聂鹏
 * @version 1.0
 * @date 18/4/17
 */
@Service
@Slf4j
public class DeviceIntimeService extends BaseService {

  @Autowired
  private DeviceService deviceService;


  public void deviceDataIntime(DataIntimeBean dataIntimeBean) {

    assembleDeviceTypes(dataIntimeBean);
    assembleStatus(dataIntimeBean);
    assembleArea(dataIntimeBean);

    List<DeviceDataIntime> dataList = deviceDataIntimeFor2("","","");

    selectConditionData(dataIntimeBean, dataList);

    dataIntimeBean.mergeElementData();
  }

  public List<DeviceDataIntime> deviceDataIntimeFor2(String status, String areaId, String name) {
    List<DeviceDataIntime> dataList = listDataOneRecentlyAll();

    for (DeviceDataIntime data : dataList) {
      data.setDeviceInfo(deviceInfoMapper.selectByPrimaryKey(data.getDeviceId()));
      data.setAreaId(data.getDeviceInfo().getAreaId());
      if (data.getDeviceInfo().getAreaId() > 0) {
        data.getDeviceInfo().setArea(areaMapper.selectByPrimaryKey(data.getAreaId()));
      }
    }

    if (StringUtil.isEmpty(status) && StringUtil.isEmpty(areaId) && StringUtil.isEmpty(name)) {
      return dataList;
    }

    if (dataList != null) {
      int statusInt = StringUtil.str2int(status);
      long areaIdLong = StringUtil.str2long(areaId);

      DeviceDataIntime tmpData;
      for (int i = 0; i < dataList.size(); ) {
        tmpData = dataList.get(i);
        if (statusInt > 0 && tmpData.getStatus() != statusInt) {
          dataList.remove(i);
          continue;
        }

        if (areaIdLong > 0 && tmpData.getAreaId() != areaIdLong) {
          dataList.remove(i);
          continue;
        }

        if (!StringUtil.isEmpty(name) && tmpData.getDeviceInfo().getName().indexOf(name) < 0) {
          dataList.remove(i);
          continue;
        }
        i++;
      }
    }
    return dataList;
  }

  private List<DeviceDataIntime> listDataOneRecentlyAll() {
    List<Long> ids = dataIntimeMapper.listDataOneIds();
    if (ids == null || ids.size() == 0) {
      return new ArrayList<DeviceDataIntime>();
    }
    String result = StringUtil.assembleLong(ids, ",");
    return dataIntimeMapper.listData(result);
  }

  public List<DeviceDataIntime> intimeCurveList(long deviceId) {
    List<DeviceDataIntime> list = dataIntimeMapper.listDataOneDevice(deviceId, 100);
    return list;
  }


  private void selectConditionData(DataIntimeBean dataIntimeBean, List<DeviceDataIntime> dataList) {
    if (dataList == null) {
      return;
    }

    // 区域,状态,传感器 筛选
    DeviceDataIntime tmpData;
    for (int i = 0; i < dataList.size();) {
      tmpData = dataList.get(i);
      if (!dataIntimeBean.hasArea(tmpData.getAreaId())) {
        dataList.remove(i);
        continue;
      }

      if (!dataIntimeBean.hasStatus(tmpData.getStatus())) {
        dataList.remove(i);
        continue;
      }

      if (!dataIntimeBean.hasOneDeviceType(tmpData.getDeviceInfo().getType())) {
        dataList.remove(i);
        continue;
      }
      i++;
    }
    dataIntimeBean.setDataList(dataList);
  }

  /**
   * 组装筛选的值:区域
   */
  private void assembleArea(DataIntimeBean dataIntimeBean) {
    List<Area> allAreaList = deviceService.getAreaAll();

    if (StringUtil.isEmpty(dataIntimeBean.getAreaIds())) {
      dataIntimeBean.setAreaList(allAreaList);
      return;
    }

    String[] ids = dataIntimeBean.getAreaIds().trim().split(",");
    List<Area> list = new ArrayList<Area>(ids.length);
    long tmpId;
    for (Area area : allAreaList) {
      for (String id : ids) {
        tmpId = StringUtil.str2long(id);
        if (area.getId() == tmpId) {
          list.add(area);
          break;
        }
      }
    }
    dataIntimeBean.setAreaList(list);
  }

  /**
   * 组装筛选的值:状态
   */
  private void assembleStatus(DataIntimeBean dataIntimeBean) {
    if (StringUtil.isEmpty(dataIntimeBean.getStatuses())) {
      dataIntimeBean.setStatusList(Arrays.asList(StatusEnum.values()));
      return;
    }
    String[] ids = dataIntimeBean.getStatuses().trim().split(",");
    List<StatusEnum> list = new ArrayList<StatusEnum>(ids.length);
    StatusEnum tmp;
    for (String id : ids) {
      tmp = StatusEnum.find(StringUtil.str2int(id));
      if (tmp != null) {
        list.add(tmp);
      }
    }
    dataIntimeBean.setStatusList(list);
  }

  /**
   * 组装筛选的值:传感器类型
   */
  private void assembleDeviceTypes(DataIntimeBean dataIntimeBean) {
    if (StringUtil.isEmpty(dataIntimeBean.getDeviceTypes())) {
      dataIntimeBean.setDeviceTypeList(Arrays.asList(DeviceTypeEnum.values()));
      return;
    }

    String[] ids = dataIntimeBean.getDeviceTypes().trim().split(",");
    List<DeviceTypeEnum> list = new ArrayList<DeviceTypeEnum>(ids.length);
    DeviceTypeEnum tmp;
    for (String id : ids) {
      tmp = DeviceTypeEnum.getOneById(StringUtil.str2int(id));
      if (tmp != null) {
        list.add(tmp);
      }
    }
    dataIntimeBean.setDeviceTypeList(list);
  }


  

}