package com.chengqianyun.eeweb2networkadmin.test.crud;


import com.chengqianyun.eeweb2networkadmin.biz.entitys.Setting;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.SettingMapper;
import com.chengqianyun.eeweb2networkadmin.test.BaseTest;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author 聂鹏
 * @version 1.0
 * @date 18/3/13
 */

public class BaseOptTest extends BaseTest {

  @Autowired
  private SettingMapper settingMapper;

  public void test() {
    Setting setting = settingMapper.selectByCode("recommend_subject");
    Assert.assertTrue(setting != null && setting.getId() == 3L);
  }

}