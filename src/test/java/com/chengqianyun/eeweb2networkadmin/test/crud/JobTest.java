package com.chengqianyun.eeweb2networkadmin.test.crud;
/**
 * Created by lsb on 18/12/20.
 */


import com.chengqianyun.eeweb2networkadmin.job.AutoJob;
import com.chengqianyun.eeweb2networkadmin.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author 聂鹏
 * @version 1.0
 * @email lsb@51huadian.cn
 * @date 18/12/20
 */

public class JobTest extends BaseTest {

  @Autowired
  private AutoJob autoJob;

  @Test
  public void testRun() {
    autoJob.autoBackupDeviceHistory();
  }
}