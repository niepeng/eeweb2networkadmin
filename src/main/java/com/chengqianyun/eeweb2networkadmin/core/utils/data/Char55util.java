package com.chengqianyun.eeweb2networkadmin.core.utils.data;

/**
 * @author 聂鹏
 * @version 1.0
 * @date 18/2/27
 */

public class Char55util {


  public static final int INSTRUCT_SIZE = 13;
  public static final int READ_ADDRESS_SIZE = 14;

  public static void main(String[] args) {
    char[] c1 = { 0x1,0x2,0x3, 0x4, 0x5, 0x6, 0x7, 0x8, 0x9, 0x10, 0x11, 0x12, 0x13,
        0x55,0x55,0x00, 0x1,0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8, 0x9, 0x10, 0x11
    };
    System.out.println("c1size=" + c1.length);
    System.out.println(FunctionUnit.bytesToHexString(dealwith55NewV2(c1)));

  }


  public static char[] dealwith55NewV2(char[] source) {
    if(source == null) {
      return null;
    }
    if (source.length == INSTRUCT_SIZE || source.length == READ_ADDRESS_SIZE) {
      return source;
    }

    if (source.length < INSTRUCT_SIZE) {
      return source;
    }

    int indexLast55 = find55(source);
    // 如果没有0x55 0x55 那么直接从最后面开始截取INSTRUCT_SIZE个
    if (indexLast55 < 0) {
      char[] result = new char[INSTRUCT_SIZE];
      for (int i = 0; i < INSTRUCT_SIZE; i++) {
        result[i] = source[source.length - INSTRUCT_SIZE + i];
      }
      return result;
    }

    // 如果有0x55 0x55， 那么要取0x55 0x55 前后分别为INSTRUCT_SIZE长度的串，优先选择后面的

    //  如果 0x55 0x55 后面有INSTRUCT_SIZE位，直接截取返回
    if(indexLast55 + INSTRUCT_SIZE < source.length) {
      char[] result = new char[INSTRUCT_SIZE];
      for (int i = 0; i < INSTRUCT_SIZE; i++) {
        result[i] = source[indexLast55 + 1 + i];
      }
      return result;
    }

    //  如果 0x55 0x55 前面有INSTRUCT_SIZE位，直接截取返回
    if (indexLast55 - 1 - INSTRUCT_SIZE >= 0) {
      char[] result = new char[INSTRUCT_SIZE];
      for (int i = 0; i < INSTRUCT_SIZE; i++) {
        result[i] = source[indexLast55 - 1 - INSTRUCT_SIZE + i];
      }
      return result;
    }

    return null;
  }

  /**
   * 查找最后一组 0x55 0x55
   * @param source
   * @return
   */
  private static int find55(char[] source) {
    for (int index = source.length - 1; index > 0; index--) {
      if (source[index] == 0x55 && source[index - 1] == 0x55) {
        return index;
      }
    }
    return -1;
  }

  public static char[] dealwith55New1(char[] source) {
    if (source.length == INSTRUCT_SIZE) {
      return source;
    }
    if (source.length < INSTRUCT_SIZE) {
      return null;
    }

    int not55lastIndex = findNot55Index(source);
    // 位数不够
    if(not55lastIndex < INSTRUCT_SIZE - 1) {
      return null;
    }

    int startIndex =  not55lastIndex - INSTRUCT_SIZE + 1;
    char[] result = new char[INSTRUCT_SIZE];
    for (int i = 0; i < INSTRUCT_SIZE; i++) {
      result[i] = source[startIndex + i];
    }
    return result;
  }

  private static int findNot55Index(char[] source) {
    for (int i = source.length - 1; i > 0; i--) {
      if((source[i] == 0x55 && source[i-1] == 0x55)) {
        i--;
      } else {
        return i;
      }
    }
    return -1;
  }


}