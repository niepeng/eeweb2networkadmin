package com.chengqianyun.eeweb2networkadmin.test.crud;


import com.chengqianyun.eeweb2networkadmin.biz.entitys.Area;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.AreaMapper;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceDataHistory;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceDataHistoryMapper;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceDataIntime;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceDataIntimeMapper;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.Setting;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.SettingMapper;
import com.chengqianyun.eeweb2networkadmin.core.utils.StringUtil;
import com.chengqianyun.eeweb2networkadmin.test.BaseTest;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author 聂鹏
 * @version 1.0
 * @date 18/3/13
 */

public class BaseOptTest extends BaseTest {

  @Autowired
  private SettingMapper settingMapper;

  @Autowired
  private AreaMapper areaMapper;

  @Autowired
  private DeviceDataIntimeMapper deviceDataIntimeMapper;

  @Autowired
  private DeviceDataHistoryMapper deviceDataHistoryMapper;

  @Autowired
  private DeviceDataIntimeMapper dataIntimeMapper;

  @Test
  public void test_1() {
    List<Long> ids = dataIntimeMapper.listDataOneIds();
    String result = StringUtil.assembleLong(ids, ",");
    System.out.println("result ===>" + result);

    List<DeviceDataIntime> listData = dataIntimeMapper.listData(result);
    System.out.println("listData ===>" + listData.size());
  }

  @Test
  public void test1() {
    Setting setting = settingMapper.selectByCode("recommend_subject");
    Assert.assertTrue(setting != null && setting.getId() == 3L);
  }

  @Test
  public void test2() {
    Area area = areaMapper.selectByPrimaryKey(1L);
    Assert.assertTrue(area == null);
  }

  @Test
  public void test3() {
    deviceDataHistoryMapper.deleteByTime("2018-04-22");
  }


  @Test
  public void test_deviceDataIntime() {
//    DeviceDataIntime record = new DeviceDataIntime();
//    record.setOut((short)1);
//    deviceDataIntimeMapper.insert(record);

    DeviceDataIntime fromDB = deviceDataIntimeMapper.selectByPrimaryKey(6L);
    fromDB.setOut((short)1);
    deviceDataIntimeMapper.updateByPrimaryKey(fromDB);
  }

  @Test
  public void test_deviceDataHistory() {
//    DeviceDataHistory deviceDataHistory = new DeviceDataHistory();
//    deviceDataHistoryMapper.insert(deviceDataHistory);

    DeviceDataHistory fromDB = deviceDataHistoryMapper.selectByPrimaryKey(4L);
    fromDB.setOut((short)1);

    deviceDataHistoryMapper.updateByPrimaryKey(fromDB);
  }

}