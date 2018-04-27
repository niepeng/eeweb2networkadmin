package com.chengqianyun.eeweb2networkadmin.data;


import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceDataIntime;
import com.chengqianyun.eeweb2networkadmin.biz.enums.StatusEnum;
import com.chengqianyun.eeweb2networkadmin.core.utils.StringUtil;
import com.chengqianyun.eeweb2networkadmin.core.utils.Tuple2;
import com.chengqianyun.eeweb2networkadmin.core.utils.data.CalcCRC;
import com.chengqianyun.eeweb2networkadmin.core.utils.data.FunctionUnit;

/**
 * 所有生成和解析指令方法结合
 *
 * @author 聂鹏
 * @version 1.0
 * @date 18/2/10
 */

public class InstructionManager {

  // 读取设备的地址
  private static char[] readAddress = {
      0xFB,0x67,0x06,0x00,0x00,0x00,0x00,0x00,0x00,0x5C,0x0A
  };


  // ------------------------------- 获取设备地址和sn号 start ------------------------------------------------
  /**
   * 获取设备地址和sn号指令
   * @return
   */
  public static char[] genGetAddress() {
    return readAddress;
  }

  /**
   * 解析:获取SN号和设备地址
   * @param responses
   * @return
   */
  public static Tuple2<String, Integer> parseGetAddress(char[] responses) {
    // FC 67 09 00 15 00 00 00 00 01 15 08 9C EC
    if (responses == null || responses.length != 14) {
      return null;
    }
    boolean isSuccess = CalcCRC.checkCrc16(responses);
    if (!isSuccess) {
      return null;
    }

    if(!checkSn(responses)) {
      return null;
    }


    int address = (int) responses[9];
    String sn = FunctionUnit.bytesToHexString(responses, 3, 7, "");
    return new Tuple2<String, Integer>(sn, address);
  }

  // ------------------------------------- 获取环境参数的值 -------------------------------------------------------

  /**
   * 获取环境参数的值:
   *服务器下发指令：
   *  01 03 00 00 00 08 44 0C
   */
  public static char[] genGetEnv(int address) {
    char[] rs = {(char) address, (char) 0x03, (char) 0, (char) 0, (char) 0, (char) 0x08, 0, 0};
    rs = CalcCRC.getCrc16(rs);
    return rs;
  }


  /**
   * 解析 获取其他环境参数的值返回：
   * 01 03 16 12 A7 72 3C 00 00 01 69 00 00 05 DC 27 E7 B4 45
   *
   * 设备返回：01 03 16 12 A7 72 3C 00 00 01 69 00 00 05 DC 27 E7 B4 45
   (获取的数据要过CRC16校验)
   数据分析：
   湿度 12 A7 转10进制为4775，实际值为47.75%
   温度 72 3C 转10进制为29244，实际值为(29244-27315）/100 = 19.29℃
   电量 01 69 转10进制为361，实际值为3.61V
   光照 05 DC 转10进制为1500，实际值为1500lx
   压力 27 E7 转10进制为10215，实际值为102.15kpa
   */
  public static DeviceDataIntime parseGetEnv(char[] response, int address) {
    if(response.length != 19 || !CalcCRC.checkCrc16(response) || response[0] != address) {
      return null;
    }

    DeviceDataIntime intime = new DeviceDataIntime();

    int temp, humi, shine, pressure, power;
    humi = (response[3] << 8) + response[4];
    temp = (response[5] << 8) + response[6] - 27315;
    power = (response[9] << 8) + response[10];
    shine = (response[13] << 8) + response[14];
    pressure = (response[15] << 8) + response[16];

    intime.setHumi(humi);
    intime.setTemp(temp);
    intime.setPower(power);
    intime.setShine(shine);
    intime.setPressure(pressure);

    return intime;
  }

  // ------------------------------------- 获取开关量输入状态 -------------------------------------------------------

  /**
   * 开关量输入查询状态指令:
   *
   * 01 02 00 20 00 04 78 03
   * @param address
   * @return
   */
  public static char[] genGetIn(int address) {
    char[] rs = {(char) address, (char) 0x02, (char) 0, (char)20, (char) 0, (char) 0x04, 0, 0};
    rs = CalcCRC.getCrc16(rs);
    return rs;
  }


  /**
   *  解析 获取开关量输入的值状态：
   * 01 02 08 01 01 FF 00 FF 00 FF 00 70 F5
   *
   * @param response
   * @param address
   * @return
   */
  public static DeviceDataIntime parseGetIn(char[] response, int address) {
    if(response.length != 13 || !CalcCRC.checkCrc16(response) || response[0] != address) {
      return null;
    }

    DeviceDataIntime intime = new DeviceDataIntime();

    if(response[3] == 0xFF) {
      intime.setSmokeStatusEnum(StatusEnum.offline);
    } else {
      intime.setSmoke((short)response[4]);
      intime.setSmokeStatusEnum(response[4] == 1 ? StatusEnum.alarm : StatusEnum.normal);
    }

    if(response[5] == 0xFF) {
      intime.setWaterStatusEnum(StatusEnum.offline);
    } else {
      intime.setWater((short)response[6]);
      intime.setWaterStatusEnum(response[6] == 1 ? StatusEnum.alarm : StatusEnum.normal);
    }


    if(response[7] == 0xFF) {
      intime.setElectricStatusEnum(StatusEnum.offline);
    } else {
      intime.setElectric((short)response[8]);
      intime.setElectricStatusEnum(response[8] == 1 ? StatusEnum.alarm : StatusEnum.normal);
    }

    if(response[9] == 0xFF) {
      intime.setBodyStatusEnum(StatusEnum.offline);
    } else {
      intime.setBody((short)response[10]);
      intime.setBodyStatusEnum(response[10] == 1 ? StatusEnum.alarm : StatusEnum.normal);
    }

    return intime;
  }

  // ---------------------------------------- 开关量输出 --------------------------------------------------------------

  /**
   * 开关量输出查询状态指令:
   *
   * 01 02 00 50 00 01 B9 DB
   * @param address
   * @Param way 第几路通道
   * @return
   */
  public static char[] genGetOut(int address, int way) {
    char wayChar = 0x0;
    if (way == 1) {
      wayChar = 0x50;
    } else if (way == 2) {
      wayChar = 0x51;
    }
    char[] rs = {(char) address, (char) 0x02, (char) 0, wayChar, (char) 0, (char) 0x01, 0, 0};
    rs = CalcCRC.getCrc16(rs);
    return rs;
  }

  /**
   *  解析 获取开关量输出的值状态：
   * 01 02 02 01 01 79 E8
   *
   * @return StatusEnum离线或在线
   *         Boolean 继电器是否打开: true==打开, false == 关闭
   */
  public static Tuple2<StatusEnum, Boolean> parseGetOut(char[] response, int address) {
    if (response.length != 7 || !CalcCRC.checkCrc16(response) || response[0] != address) {
      return null;
    }

    StatusEnum tmp;
    Boolean isOpen = false;
    if (response[3] == 0xFF) {
      tmp = StatusEnum.offline;
    } else {
      isOpen = response[4] == 1;
      tmp = isOpen ? StatusEnum.alarm : StatusEnum.normal;
    }

    return new Tuple2<StatusEnum, Boolean>(tmp, isOpen);
  }

  /**
   *
   * 获取继电器打开关闭指令:
   * 服务器下发：01 05 00 50 00 01 0C 1B   打开操作
   * 服务器下发：01 05 00 50 00 00 CD DB  关闭操作
   *
   * @return
   */
  public static char[] genOptOut(int address, int way, boolean isOpen) {
    char wayChar = 0x0;
    if (way == 1) {
      wayChar = 0x50;
    } else if (way == 2) {
      wayChar = 0x51;
    }
    char[] rs = {(char) address, (char) 0x05, (char) 0, wayChar, (char) 0, isOpen ? (char) 0x01 : (char)0x00, 0, 0};
    rs = CalcCRC.getCrc16(rs);
    return rs;
  }

  /**
   *  解析 获取继电器打开或关闭是否成功：
   * 01 05 02 55 55 47 A3 (打开成功）
   * 01 05 02 FF FF B9 7C (打开失败)
   *
   * @return 操作是否成功 true:成功, false:失败
   */
  public static boolean optOutResult(char[] response, int address) {
    if (response.length != 7 || !CalcCRC.checkCrc16(response) || response[0] != address) {
      return false;
    }
    return response[3] == 0x55 && response[4] == 0x55;
  }


  // ----------------------------------------------------------------------------------------------------------------


  /**
   * FC 67 09 02 00 00 01 CE 5E 01 17 03 7F F1
   * sn号 = 02000001
   * SN号校验码(AC) = CE5E
   */
  private static boolean checkSn(char[] responses) {
    System.out.println(FunctionUnit.bytesToHexString(responses));
    System.out.println();
    char[] tmp = StringUtil.subCharArray(responses, 3, 9);

//    System.out.println("初始的指令为:");
//    System.out.println(FunctionUnit.bytesToHexString(tmp));
//    System.out.println();
    if (tmp == null) {
      return false;
    }

    char[] crcTmp = CalcCRC.getCrc16(tmp);
//    System.out.println("crc后的的指令为:");
//    System.out.println(FunctionUnit.bytesToHexString(tmp));
//    System.out.println();

//    System.out.println("ac第一位为:" + Integer.toHexString(0xFF & crcTmp[4]));
//    System.out.println("ac第二位为:" + Integer.toHexString(0xFF & crcTmp[5]));
//    System.out.println();

    char acFirstLow;
    char acSecondLow;
    if( (crcTmp[4] % 0x10) < 2 ) {
      acFirstLow = (char) (crcTmp[4] % 0x10 + 0x10 - 2);
    } else {
      acFirstLow = (char) (crcTmp[4] % 0x10 - 2);
    }

    if( (crcTmp[5] % 0x10) < 14 ) {
      acSecondLow = (char)(crcTmp[5] % 0x10 + 2);
    } else {
      acSecondLow = (char)(crcTmp[5] % 0x10 + 2 - 0x10);
    }

//    System.out.println("ac第一位低位为:" + Integer.toHexString(0xFF & acFirstLow));
//    System.out.println("ac第二位低位为:" + Integer.toHexString(0xFF & acSecondLow));
//    System.out.println();


    char resultAcFirst = (char) ((crcTmp[4] / 0x10 << 4)+ (int)acFirstLow);
    char resultAcSecond = (char) ((crcTmp[5] / 0x10 << 4) + (int)acSecondLow);

//    System.out.println("ac第一位计算为:" + Integer.toHexString(0xFF & resultAcFirst));
//    System.out.println("ac第二位计算为:" + Integer.toHexString(0xFF & resultAcSecond));
//    System.out.println();
//
//    System.out.println("原始第一位计算为:" + Integer.toHexString(0xFF & responses[7]));
//    System.out.println("原始第二位计算为:" + Integer.toHexString(0xFF & responses[8]));

    return resultAcFirst == responses[7] && resultAcSecond == responses[8];
  }


}