package com.chengqianyun.eeweb2networkadmin.core.utils;

import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 * @author 聂鹏
 * @version 1.0
 * @date 18/3/13
 */

public class StringUtil extends StringUtils {

  public StringUtil() {
  }

  public static char[] subCharArray(char[] array, int startIndex, int endIndex) {
    if (array == null) {
      return null;
    }

    if (startIndex < 1 || startIndex >= endIndex || array.length < endIndex) {
      return null;
    }
    char[] result = new char[endIndex - startIndex];
    for (int i = startIndex; i < endIndex; i++) {
      result[i - startIndex] = array[i];
    }

    return result;
  }

  public static double str2Double(String str) {
    if (isEmpty(str)) {
      return 0D;
    }
    try {
      return Double.valueOf(str);
    } catch (Exception e) {

    }

    return 0D;
  }

  public static long str2long(String str) {
    if (isEmpty(str)) {
      return 0;
    }
    try {
      return Long.valueOf(str).longValue();
    } catch (Exception e) {

    }
    return 0;
  }

  public static int str2int(String str) {
    if (isEmpty(str)) {
      return 0;
    }
    try {
      return Integer.valueOf(str);
    } catch (Exception e) {

    }
    return 0;
  }

  /**
   * 手机号隐藏 例 ：158*****5566
   *
   * @param phone
   * @return
   */
  public static String hidePhone(String phone) {
    if (phone == null || phone.length() < 3) {
      return "";
    }
    return hideStr(3, 4, 4, phone);
  }

  /**
   * 手机号隐藏 例 ：15***666
   *
   * @param phone
   * @return
   */
  public static String hidePhone2(String phone) {
    if (phone == null || phone.length() < 3) {
      return "";
    }
    return hideStr(2, 3, 3, phone);
  }

  /**
   * 隐藏姓名 例：*三,*德华
   * @param name
   * @return
   */
  public static String hideName(String name){
    if (isBlank(name)) {
      return "";
    }
    StringBuilder sb = new StringBuilder();
    if(name.length()>3){
      sb.append(creatHideChar(2)).append(name.substring(2));//复姓
    }else{
      sb.append(creatHideChar(1)).append(name.substring(1));
    }
    return sb.toString();
  }

  /**
   * 隐藏身份证
   * @param cardCode
   * @return
   */
  public static String hideCardCode(String cardCode){
    if (isBlank(cardCode)) {
      return "";
    }
    return hideStr(6, 4, 8, cardCode);
  }

  /**
   * 隐藏用户名 例：lu***cy,l路***cy
   *
   * @param loginName
   * @return
   */
  public static  String hideLoginName(String loginName) {
    if (isBlank(loginName)) {
      return loginName;
    }
    StringBuilder buf = new StringBuilder();
    int len = loginName.length();
    if (len > 5) {
      //return buf.append(loginName.substring(0, 2)).append(creatHideChar(3)).append(loginName.substring(5)).toString();
      return buf.append(loginName.substring(0, 2)).append(creatHideChar(len - 4)).append(loginName.substring(len - 2)).toString();
    }
    if (len <= 2) {
      return loginName;
    }
    if (len > 2 && len <= 5) {
      return buf.append(loginName.substring(0, 2)).append(creatHideChar(len - 2)).toString();
    }
    return "";
  }

  /**
   * 隐藏邮箱
   * @param email
   * @return
   */
  public static String hideEmail(String email){
    if (isBlank(email)) {
      return "";
    }
    String prefix = email.substring(0, email.indexOf("@"));
    String suffix = email.substring(email.indexOf("@"));
    if(prefix.length()<=3){
      return hideStr(1, 0, prefix.length()-1, prefix)+suffix;
    }
    return hideStr(prefix.length()-3, 0, 3, prefix)+suffix;
  }

  /**
   * 截取银行卡后4位
   * @param bankCard
   * @return
   */
  public static String hideBankCard(String bankCard){
    if (isBlank(bankCard)) {
      return "";
    }
    if(bankCard.length()<=4){
      return bankCard;
    }
    return bankCard.substring(bankCard.length()-4);
  }

  /**
   * 生成*字符窜
   *
   * @param num
   * @return
   */
  public static String creatHideChar(int num) {
    StringBuilder sb = new StringBuilder();
    while (num > 0) {
      sb.append("*");
      num--;
    }
    return sb.toString();
  }

  /**
   * 隐藏字符的显示
   *
   * @param preNum
   *            字符前端要显示的字符数
   * @param sufNum
   *            字符后端要显示的字符数
   * @param str
   * @return
   */
  public static String hideStr(int preNum, int sufNum, int hideNum, String str) {
    if (str == null) {
      return str;
    }
    int len = str.length();
    if (preNum + sufNum > len) {
      return str;
    }
    StringBuilder sb = new StringBuilder();
    if(sufNum > 0){
      sb.append(str.substring(0, preNum)).append(creatHideChar(hideNum)).append(str.substring(len - sufNum));
    }else{
      sb.append(str.substring(0, preNum)).append(creatHideChar(hideNum));
    }
    return sb.toString();
  }

  /**
   * 将byte数组转化为16进制
   *
   * @param buf
   * @return
   */
  public static String parseByte2HexStr(byte[] buf) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < buf.length; i++) {
      String hex = Integer.toHexString(buf[i] & 0xFF);
      if (hex.length() == 1) {
        hex = '0' + hex;
      }
      sb.append(hex);
    }
    return sb.toString();
  }

  public static byte[] parseHexStr2Byte(String hexStr) {
    byte[] result = new byte[hexStr.length() / 2];
    for (int i = 0; i < hexStr.length() / 2; i++) {
      int strIndex = i << 1;
      int high = Integer.parseInt(hexStr.substring(strIndex, strIndex + 1), 16);
      int low = Integer.parseInt(hexStr.substring(strIndex + 1, strIndex + 2), 16);
      result[i] = (byte) ((high << 4) + low);
    }
    return result;
  }

  /**
   * 判断一个字符是Ascill字符还是其它字符（如汉，日，韩文字符）
   *
   * @return boolean, 返回true,Ascill字符
   */
  public static boolean isLetter(char c) {
    int k = 0x80;
    return c / k == 0 ? true : false;
  }

  /**
   * 得到一个字符串的长度,显示的长度,一个汉字或日韩文长度为2,英文字符长度为1
   *
   *            s ,需要得到长度的字符串
   * @return int, 得到的字符串长度
   */
  public static int lengths(String s) {
    if (s == null) {
      return 0;
    }
    char[] c = s.toCharArray();
    int len = 0;
    for (int i = 0; i < c.length; i++) {
      len++;
      if (!isLetter(c[i])) {
        len++;
      }
    }
    return len;
  }

  /**
   * 截取一段字符的长度,不区分中英文,如果数字不正好，则少取一个字符位
   *
   */
  public static String substring(String origin, int len) {
    if (origin == null || origin.equals("") || len < 1) {
      return "";
    }

    byte[] strByte = new byte[len];
    if (len > lengths(origin)) {
      return origin;
    }
    System.arraycopy(origin.getBytes(), 0, strByte, 0, len);
    int count = 0;
    for (int i = 0; i < len; i++) {
      int value = (int) strByte[i];
      if (value < 0) {
        count++;
      }
    }
    if (count % 2 != 0) {
      // len = (len == 1) ? ++len : --len;
      --len;
    }
    return new String(strByte, 0, len);
  }

  /**
   * 把List组合成字符串，使用 splitChar 分割。
   *
   * @param value 需要组合的List
   * @return 组合后的字符串
   */
  public static String assemble(List<String> value, String splitChar) {
    if (value == null || value.size() == 0) {
      return "";
    }
    StringBuilder temp = new StringBuilder();
    for (String s : value) {
      temp.append(s);
      temp.append(splitChar);
    }
    temp.deleteCharAt(temp.length() - 1);
    return temp.toString();
  }

  public static String assemble(String[] array, String splitChar) {
    if (array == null || array.length == 0) {
      return "";
    }
    StringBuilder temp = new StringBuilder();
    for (String s : array) {
      temp.append(s);
      temp.append(splitChar);
    }
    temp.deleteCharAt(temp.length() - 1);
    return temp.toString();
  }


  public static boolean sql_Injection(String str) {
    if(str==null||"".equals(str)){
      return true;
    }
    String inj_str = "' and exec insert select delete update"
        + " count * % chr mid master truncate char declare ; or - + ,";
    String arr[] = inj_str.split(" ");
    for (int i = 0; i < arr.length; i++) {
      if (str.indexOf(arr[i]) != -1) {
        return true;
      }
    }
    return false;
  }
}
