package com.chengqianyun.eeweb2networkadmin.core.utils;

/**
 * @ DeEnUnicode2ZhCN.java
 * 作用 : 中文和Unicode互转
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2010-4-28     niepeng       create
 */
public class UnicodeZhCN {

	/**
	 * 中文转unicode
	 * @param source 需要转换的中文
	 * @return 反回unicode编码
	 */
	public static String toUnicode(String source) {
		StringBuffer strBuf = new StringBuffer();
		String str="";
		char[] c = source.toCharArray();   
		for(int   i=0;i<c.length;i++){
			str =Integer.toHexString((int)c[i]);
			if (str.length() ==3){str = "0" + str;}		// 保证每个str都是4位
			if (str.length() ==2){str = "00" + str;}	// 保证每个str都是4位
			if (str.length() ==1){str = "000" + str;}	// 保证每个str都是4位
			//System.out.println(str+":"+c[i]);
			strBuf.append(str);
		}		
		return strBuf.toString();
	}
	
	/**
	 * unicode码转中文
	 * @param unicode unicode码
	 * @return 返回unicode对应的中文
	 */	
	public static String tozhCN(String unicode) {
		StringBuffer gbk = new StringBuffer();
		int data = 0 ;
		int len  = unicode.length();
		int beginIndex = 0;
		while (len > beginIndex) {
			// 将16进制数转换为 10进制的数据。
			data = Integer.parseInt(unicode.substring(beginIndex,beginIndex+4), 16);
			// 强制转换为char类型就是我们的中文字符了。
			if(data!=8232){//行分隔符 (Unicode 8232)
				gbk.append((char) data); 			
			}
			beginIndex+=4;
		}
		return gbk.toString();
	}
	
	/**
	 * PDU 7bit解码
	 */
	public static String decode7Bit(String src) {   
	    String result = null;   
	    int[] b;   
	    String temp = null;   
	    byte srcAscii;   
	    byte left = 0;   
	    if (src != null && src.length() % 2 == 0) {   
	        result = "";   
	        b = new int[src.length() / 2];   
	        temp = src + "0";   
	        for (int i = 0, j = 0, k = 0; i < temp.length() - 2; i += 2, j++) {   
	            b[j] = Integer.parseInt(temp.substring(i, i + 2), 16);   
	  
	            k = j % 7;   
	            srcAscii = (byte) (((b[j] << k) & 0x7F) | left);   
	            result += (char) srcAscii;   
	            left = (byte) (b[j] >>> (7 - k));   
	            if (k == 6) {   
	                result += (char) left;   
	                left = 0;   
	            }
						if (j == src.length() / 2) {
							result += (char) left;
						}
	        }   
	    }   
	    return result;   
	}	

//	public static void main(String args[]) {
//		String source = "中文123..。！@@ O(∩_∩)O哈哈~ 2009-11-19 12:23:56";
//		String str = UnicodeZhCN.toUnicode(source);
//		System.out.println(str);
//		System.out.println(source);
//		System.out.println(UnicodeZhCN.tozhCN(str));
//	}
}