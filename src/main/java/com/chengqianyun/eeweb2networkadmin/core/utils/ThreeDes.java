package com.chengqianyun.eeweb2networkadmin.core.utils;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

/**
 * @author 聂鹏
 * @version 1.0
 * @email lisenbiao@51huadian.cn
 * @date 17/7/26
 */

public class ThreeDes {

  //    public static String MD5KEY="XINBANG";
  private static Base64 base64 = new Base64();
  private static byte[] myIV = {50, 51, 52, 53, 54, 55, 56, 57};

  public static String KEY = "cqlwebInfo";


  public static String getKey() {
    return KEY;
  }

  public static String desEncrypt(String input, String strkey) throws Exception {
    strkey = procKey(strkey);
    Base64 base64d = new Base64();
    DESedeKeySpec p8ksp = null;
    p8ksp = new DESedeKeySpec(base64d.decode(strkey));
    Key key = null;
    key = SecretKeyFactory.getInstance("DESede").generateSecret(p8ksp);

    input = padding(input);
    byte[] plainBytes = (byte[]) null;
    Cipher cipher = null;
    byte[] cipherText = (byte[]) null;

    plainBytes = input.getBytes("UTF8");
    cipher = Cipher.getInstance("DESede/CBC/NoPadding");
    SecretKeySpec myKey = new SecretKeySpec(key.getEncoded(), "DESede");
    IvParameterSpec ivspec = new IvParameterSpec(myIV);
    cipher.init(1, myKey, ivspec);
    cipherText = cipher.doFinal(plainBytes);
    String regStr = removeBR(base64.encodeToString(cipherText));
    String rtn = CryptTool.byteArrayToHexString(regStr.getBytes());
    return rtn;
  }

  public static String desDecrypt(String cipherText, String strkey) throws Exception {
    cipherText = new String(CryptTool.hexString2ByteArray(cipherText));
    strkey = procKey(strkey);
    Base64 base64d = new Base64();
    DESedeKeySpec p8ksp = null;
    p8ksp = new DESedeKeySpec(base64d.decode(strkey));
    Key key = null;
    key = SecretKeyFactory.getInstance("DESede").generateSecret(p8ksp);

    Cipher cipher = null;
    byte[] inPut = base64d.decode(cipherText);
    cipher = Cipher.getInstance("DESede/CBC/NoPadding");
    SecretKeySpec myKey = new SecretKeySpec(key.getEncoded(), "DESede");
    IvParameterSpec ivspec = new IvParameterSpec(myIV);
    cipher.init(2, myKey, ivspec);
    byte[] output = removePadding(cipher.doFinal(inPut));

    return new String(output, "UTF8");

  }

  private static String removeBR(String str) {
    StringBuffer sf = new StringBuffer(str);

    for (int i = 0; i < sf.length(); ++i) {
      if (sf.charAt(i) == '\n') {
        sf = sf.deleteCharAt(i);
      }
    }
    for (int i = 0; i < sf.length(); ++i) {
      if (sf.charAt(i) == '\r') {
        sf = sf.deleteCharAt(i);
      }
    }

    return sf.toString();
  }

  public static String padding(String str) {
    byte[] oldByteArray;
    try {
      oldByteArray = str.getBytes("UTF8");
      int numberToPad = 8 - oldByteArray.length % 8;
      byte[] newByteArray = new byte[oldByteArray.length + numberToPad];
      System.arraycopy(oldByteArray, 0, newByteArray, 0, oldByteArray.length);
      for (int i = oldByteArray.length; i < newByteArray.length; ++i) {
        newByteArray[i] = 0;
      }
      return new String(newByteArray, "UTF8");
    } catch (UnsupportedEncodingException e) {
      System.out.println("Crypter.padding UnsupportedEncodingException");
    }
    return null;
  }

  public static byte[] removePadding(byte[] oldByteArray) {
    int numberPaded = 0;
    for (int i = oldByteArray.length; i >= 0; --i) {
      if (oldByteArray[(i - 1)] != 0) {
        numberPaded = oldByteArray.length - i;
        break;
      }
    }

    byte[] newByteArray = new byte[oldByteArray.length - numberPaded];
    System.arraycopy(oldByteArray, 0, newByteArray, 0, newByteArray.length);

    return newByteArray;
  }

  /**
   * 把KEY处理成32位，如果不足，在后面补0，如果超出，截取前32位
   */
  private static String procKey(String key) {
    if (key.length() < 32) {
      while (key.length() < 32) {
        key = key + "0";
      }
      return key;
    } else if (key.length() > 32) {
      return key.substring(0, 32);
    }

    return key;
  }


  /**
   * 加密
   **/
  public static String encrypt(String con) {
    try {
      return desEncrypt(con, getKey());
    } catch (Exception e) {
      return con;
    }
  }

  /**
   * 解密
   **/
  public static String decrypt(String con) {
    try {
      return desDecrypt(con, getKey());
    } catch (Exception e) {
      return con;
    }

  }

  public static void main(String[] args) {
    String source = "hello1234";
    String value = encrypt(source);
    System.out.println(value);
    String dd = decrypt(value);
    System.out.println(dd);
  }

}