package com.chengqianyun.eeweb2networkadmin.job;


import com.chengqianyun.eeweb2networkadmin.biz.SystemConstants.Times;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceDataIntime;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceDataIntimeMapper;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceInfo;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceInfoMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author 聂鹏
 * @version 1.0
 * @date 18/6/6
 */
@Slf4j
@Component
public class AutoJob {

  private final static long CLEAR_TIMES = 1 * Times.hour;

  @Autowired
  private DeviceInfoMapper deviceInfoMapper;

  @Autowired
  private DeviceDataIntimeMapper deviceDataIntimeMapper;

  @Scheduled(fixedDelay = CLEAR_TIMES)
  public void clearData() {
    log.info("clearDataStart............................");

    /**
     * 1.实时数据清理
     * 获取设备列表,获取每个设备数据的最新一条,删除小于该记录id的数据(即:一个设备只需要保存最新的一条)
     */
    Map<String, String> map = new HashMap<String, String>();
    map.put("startRecord", "0");
    map.put("endRecord", "1000");
    List<DeviceInfo> list = deviceInfoMapper.findPage(map);
    optClearIntime(list);

    log.info("clearDataEnd............................");
  }

  private void optClearIntime(List<DeviceInfo> list) {
    if (list == null || list.size() == 0) {
      return;
    }

    List<DeviceDataIntime> tmpList;
    for (DeviceInfo deviceInfo : list) {
      tmpList = deviceDataIntimeMapper.listDataOneDevice(deviceInfo.getId(), 1);
      if (tmpList == null || tmpList.size() == 0) {
        continue;
      }
      deviceDataIntimeMapper.deleteByDeviceId(deviceInfo.getId(), tmpList.get(0).getId());
    }
  }



}