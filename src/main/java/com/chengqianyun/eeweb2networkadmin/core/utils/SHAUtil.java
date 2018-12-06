package com.chengqianyun.eeweb2networkadmin.core.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;

public class SHAUtil {
    
	/**
     * sha-256 加密
     * @param text
     * @return
     */
    public static String encode(String text) {
        try {
          MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(text.getBytes("UTF-8"));
            String output = Hex.encodeHexString(hash);
            return output;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static void main(String[] args) {
      String pwd = encode("cql1234");
	  	System.out.println(pwd);
		  System.out.println(pwd.length());
	}

}

