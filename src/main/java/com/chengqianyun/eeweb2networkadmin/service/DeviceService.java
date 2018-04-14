package com.chengqianyun.eeweb2networkadmin.service;


import com.chengqianyun.eeweb2networkadmin.biz.entitys.Area;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceInfo;
import com.chengqianyun.eeweb2networkadmin.biz.page.PageResult;
import com.chengqianyun.eeweb2networkadmin.biz.page.PaginationQuery;
import com.chengqianyun.eeweb2networkadmin.core.utils.StringUtil;
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
            if(info.getRelationOutId() > 0) {
              info.setRelationDeviceInfo(deviceInfoMapper.selectByPrimaryKey(info.getRelationOutId()));
            }
          }
        }
        result = new PageResult<DeviceInfo>(list, count, query);
      }
    } catch (Exception e) {
      log.error("DeviceService.getDeviceInfoList,Error", e);
    }
    return result;
  }


}