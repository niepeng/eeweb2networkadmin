package com.chengqianyun.eeweb2networkadmin.test;


import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceDataIntime;
import com.chengqianyun.eeweb2networkadmin.core.utils.Tuple2;
import com.chengqianyun.eeweb2networkadmin.data.InstructionManager;

/**
 * @author 聂鹏
 * @version 1.0
 * @date 18/4/25
 */

public class InstractionMain {

  public static void main1(String[] args) {
    char[] result = {0xFC, 0x67, 0x09, 0x02, 0x00, 0x00, 0x01, 0xCE, 0x5E, 0x01, 0x17, 0x03, 0x7F, 0xF1};
    Tuple2<String, Integer> tuple = InstructionManager.parseGetSnAddress(result);
    System.out.println(tuple.getT1());
    System.out.println(tuple.getT2());
  }

  public static void main2(String[] args) {
    char a  = 0xC4;
    System.out.println((a % 0x010));
    System.out.println((a / 0x10));
  }


  public static void main(String[] args) {
    int address = 1;
    char[] result = {0x01, 0x03, 0x16 , 0x12 , 0xA7 , 0x72 , 0x3C , 0x00 , 0x00 , 0x01 , 0x69 , 0x00 , 0x00 , 0x05 , 0xDC , 0x27 , 0xE7 , 0xB4 , 0x45};
    DeviceDataIntime intime = InstructionManager.parseGetEnv(result, address);
    System.out.println(intime);
  }



}