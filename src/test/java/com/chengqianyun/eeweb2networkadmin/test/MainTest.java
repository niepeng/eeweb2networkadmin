package com.chengqianyun.eeweb2networkadmin.test;


import com.chengqianyun.eeweb2networkadmin.core.utils.SHAUtil;
import com.chengqianyun.eeweb2networkadmin.core.utils.data.CalcCRC;
import com.chengqianyun.eeweb2networkadmin.core.utils.data.FunctionUnit;
import com.chengqianyun.eeweb2networkadmin.data.InstructionManager;

/**
 * @author 聂鹏
 * @version 1.0
 * @date 18/9/20
 */

public class MainTest {

  public static void main3(String[] args) {
    String s = "3,2，2,";
    System.out.println(s);
    s = s.replaceAll(" ", "").replaceAll("，",",");
    System.out.println(s);
  }

  public static void main(String[] args) {
//    char[] result = genInstructionBySn(new char[]{0x02, 0x18, 0x05, 0x01});
//    System.out.println(FunctionUnit.bytesToHexString(result));

//    String value = SHAUtil.encode("hello@1234");
//    String value = SHAUtil.encode("admin@1234");
    String value = SHAUtil.encode("cql123456");

    // 726d3b955f29d9efdce05c47ace4a05c83ec4a5b063d6c61c73ed78aa07ee438

    System.out.println(value);
  }

  /**
   * 参数:02 18 05 01
   *
   * 返回结果:FB 68 06 02 18 05 01 41 0D  9D 33
   *
   * 具体介绍: FB 68 06 固定
   * 02 18 05 01 序列号
   * 41 0D 序列号校验码
   * 9D 33 是CRC16
   */
  private static char[] genInstructionBySn(char[] sn) {
    char[] result = {0xFB, 0x68, 0x06,
        0x00,0x00,0x00,0x00,  0x00,0x00,
        0x00,0x00
    };

    char[] snNew = {sn[0], sn[1],sn[2], sn[3], 0x00, 0x00};
    char[] snAndCheckCode = InstructionManager.getSnAndCheckCodeBySn(snNew);

    result[3] = snAndCheckCode[0];
    result[4] = snAndCheckCode[1];
    result[5] = snAndCheckCode[2];
    result[6] = snAndCheckCode[3];
    result[7] = snAndCheckCode[4];
    result[8] = snAndCheckCode[5];

    return CalcCRC.getCrc16(result);
  }

}