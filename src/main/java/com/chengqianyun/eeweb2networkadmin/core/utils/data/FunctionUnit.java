package com.chengqianyun.eeweb2networkadmin.core.utils.data;

/**
 * @author 聂鹏
 * @version 1.0
 * @date 18/2/27
 */

public class FunctionUnit {

  /**
   * @describe:	把char类型转换成16进制字符串
   * @param bArray  char类型数组
   */
  public static final String bytesToHexString(char[] bArray) {
    if (bArray == null || bArray.length == 0) {
      return "";
    }
    return bytesToHexString(bArray, 0, bArray.length, " ");
  }

  /**
   * @describe:	把char类型转换成16进制字符串
   * @param bArray  char类型数组
   */
  public static final String bytesToHexString(char[] bArray, int startIndex, int endIndex, String split) {
    if(bArray == null || bArray.length == 0) {
      return "";
    }

    if(startIndex < 0 || endIndex <= startIndex || bArray.length < endIndex) {
      return "";
    }

    if(split == null) {
      split = "";
    }


    StringBuffer sb = new StringBuffer(endIndex - startIndex);
    String sTemp;

    for (int i = startIndex; i < endIndex; i++) {
      sTemp = Integer.toHexString(0xFF & bArray[i]);
      if (sTemp.length() < 2) {
        sb.append(0);
      }
      sb.append(sTemp.toUpperCase() + split);
    }
    return sb.toString();
  }


  private static char[] readAddress = {
      0xFB,0x67,0x06,0x00,0x00,0x00,0x00,0x00,0x00,0x5C,0x0A
  };

  public static void main(String[] args) {
    String value = bytesToHexString(readAddress, 0 , readAddress.length, "");
    System.out.println(value);
  }




}