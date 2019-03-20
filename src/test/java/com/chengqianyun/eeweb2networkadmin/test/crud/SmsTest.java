package com.chengqianyun.eeweb2networkadmin.test.crud;
/**
 * Created by lsb on 19/3/20.
 */


import com.chengqianyun.eeweb2networkadmin.core.utils.DateUtil;
import com.chengqianyun.eeweb2networkadmin.service.PhoneSmsService;
import com.chengqianyun.eeweb2networkadmin.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author 聂鹏
 * @version 1.0
 * @email lsb@51huadian.cn
 * @date 19/3/20
 */

public class SmsTest extends BaseTest {

  @Autowired
  private PhoneSmsService phoneSmsService;

  @Test
  public void test() throws InterruptedException {
    String phone = "15372095699";
    String smsContent = "今天来测试下," + DateUtil.getCurrentTimeStamp();
    phoneSmsService.callPhoneOrSms(phone, smsContent, "聂鹏", true, true);

    Thread.sleep(8 * 60 * 1000);
  }
}