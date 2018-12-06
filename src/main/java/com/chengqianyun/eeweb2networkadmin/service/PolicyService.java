package com.chengqianyun.eeweb2networkadmin.service;


import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceDataIntime;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceInfo;
import com.chengqianyun.eeweb2networkadmin.biz.enums.DeviceTypeEnum;
import com.chengqianyun.eeweb2networkadmin.biz.enums.StatusEnum;
import com.chengqianyun.eeweb2networkadmin.core.utils.DateUtil;
import com.chengqianyun.eeweb2networkadmin.core.utils.SystemClock;
import com.chengqianyun.eeweb2networkadmin.data.OptDataHelper;
import com.chengqianyun.eeweb2networkadmin.data.ServerConnectionManager;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 辅助策略服务
 *
 * @author 聂鹏
 * @version 1.0
 * @date 18/12/6
 */
@Service
@Slf4j
public class PolicyService extends BaseService {

  @Autowired
  private OptDataHelper optDataHelper;


  public void executeOffline() {
    log.info("startPolicyService");
    SystemClock.sleep(5 * 60 * 1000);
    while (true) {
      SystemClock.sleep(ServerConnectionManager.GET_DATA_CYCLE * 1000);
      int second = ServerConnectionManager.GET_DATA_CYCLE * ServerConnectionManager.FAIL_TIMES_RETURN;
      Date now = new Date();
      // 获取设备列表
      List<DeviceInfo> list = deviceInfoMapper.findAll();
      if (list == null || list.size() == 0) {
        continue;
      }

      for (DeviceInfo deviceInfo : list) {
        SystemClock.sleep(10);
        Date afterDate = DateUtil.addSecond(now, -second);
        Long id = dataIntimeMapper.hasRecentlyOne(deviceInfo.getId(), afterDate);
        if (id != null && id > 0) {
          continue;
        }

        DeviceDataIntime intime = new DeviceDataIntime();
        if (DeviceTypeEnum.hasEnv(deviceInfo.getType())) {
          intime.setStatus(StatusEnum.offline.getId());
        }

        if (DeviceTypeEnum.hasIn(deviceInfo.getType())) {
          intime.setInStatus(StatusEnum.offline.getId());
        }

        if (DeviceTypeEnum.hasOut(deviceInfo.getType())) {
          intime.setOutStatus(StatusEnum.offline.getId());
        }
        optDataHelper.optData(intime, null, deviceInfo);
      }
    }
  }


}