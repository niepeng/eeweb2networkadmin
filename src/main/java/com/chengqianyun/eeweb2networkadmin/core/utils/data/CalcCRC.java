package com.chengqianyun.eeweb2networkadmin.core.utils.data;

public class CalcCRC {

	/**
	 * @describe :crc算法
	 * @param StartAddress : 需要CRC校验的char数组
	 * @param len : 需要校验的长度
	 * @return : 经过CRC校验后的结果(2个字节)
	 * @date:2009-11-4
	 */
	private static char CRC16(char[] StartAddress, int len) {
		char crc = (char) 0xffff;
		char u8tmp;
		
		for (int i = 0; i < len; i++) {
			crc ^= StartAddress[i];

			for (int n = 0; n < 8; n++) {
				u8tmp = (char) (crc & 1);
				crc >>= 1;
				crc &= 0x7fff;
				if (u8tmp == 1) {
					crc ^= 0xa001;
				}
			}//end for-i
			
		}//end for-n
		
		return crc;
	}

	/**
	 * @describe: 发送的数据进行crc校验
	 * @param crcValue : 要crc校验的char数组
	 * @return crc校验后的 char 数组
	 * @date:2009-11-4
	 */
	public static char[] getCrc16(char[] crcValue){
		char[] sendArray = crcValue;
		int len = sendArray.length;
		int hexRs = CRC16(sendArray, len - 2);
		sendArray[len - 2] = (char) (hexRs & 0xFF);    
		sendArray[len - 1] = (char) ((hexRs >> 8) & 0xFF); 
		return sendArray;
	}
	
	/**
	 * @describe: 对接收数据进行crc检查，
	 * @param crcValue 接收数据
	 * @return 正确是true,否则为false
	 * @date:2009-11-4
	 */
	public static boolean checkCrc16(char[] crcValue){
		if (crcValue.length < 2) {
			return false;
		}
		
		int hexRs = CRC16(crcValue,crcValue.length - 2);
		char highBit = (char) (hexRs & 0xFF);  
		char lowBit  = (char) ((hexRs >> 8) & 0xFF);
		boolean boolRs = crcValue[crcValue.length-2] == highBit? true:false;
		boolRs = boolRs || crcValue[crcValue.length-1] == lowBit? true:false;
		
		if (!boolRs){
//			System.out.println("-----------start-----------");
//			//System.out.print("计算得到的CRC:" + Integer.toHexString(hexRs) + "--");
//			System.out.print("转换后得到的CRC:" + Integer.toHexString(highBit) +":"+ Integer.toHexString(lowBit) + "--");
//			System.out.print("目标CRC:" + Integer.toHexString(crcValue[crcValue.length-2]));
//			System.out.print(":" + Integer.toHexString(crcValue[crcValue.length-1]));
//			System.out.println();
		}
		return boolRs;
	}
	
//	public static void main (String agrs[]){
//		//01 03 06 16 59 75 75 71 DE 90 50 
//		char [] StartAddress = new char[]{0x01,0x03,0x06,0x16,0x59,0x75,0x75,0x71,0xDE};
//		System.out.println(TestCom.bytesToHexString(StartAddress));
//		
//		StartAddress = CalcCRC.getCrc16(StartAddress);
//		System.out.println(TestCom.bytesToHexString(StartAddress));
//	}
	
}