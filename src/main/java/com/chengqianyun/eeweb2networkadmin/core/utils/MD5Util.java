package com.chengqianyun.eeweb2networkadmin.core.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 类MD5Util.java的实现描述：MD5加密工具类
 * 
 * @author wxg
 * 
 */
public class MD5Util {

	/**
	 * 进行MD5加密 参数是明文返回是密文, if data is blank return null
	 * 
	 * @param data
	 * @return
	 */
	public static String md5Hex(String data) {
		if (StringUtils.isBlank(data)) {
			return null;
		}
		return DigestUtils.md5Hex(data);
	}
}
