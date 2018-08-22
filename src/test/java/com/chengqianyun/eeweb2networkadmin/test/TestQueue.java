package com.chengqianyun.eeweb2networkadmin.test;


import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceAlarm;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author 聂鹏
 * @version 1.0
 * @email lsb@51huadian.cn
 * @date 18/8/22
 */

public class TestQueue {

  static BlockingQueue<DeviceAlarm> queue = new LinkedBlockingQueue<DeviceAlarm>();

  public static void main(String[] args) throws InterruptedException {
    new Thread(new Runnable() {
      @Override
      public void run() {
        while(true) {
          DeviceAlarm deviceAlarm = null;
          try {
            deviceAlarm = queue.take();
            System.out.println(deviceAlarm.getId());
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      }
    }).start();

    long i = 1;
    DeviceAlarm d1 = new DeviceAlarm();
    d1.setId(i++);
    queue.put(d1);
    System.out.println("add1");
    Thread.sleep(1000);

    System.out.println("add2");
    DeviceAlarm d2 = new DeviceAlarm();
    d2.setId(i++);
    queue.put(d2);

    Thread.sleep(1000000);


  }
}