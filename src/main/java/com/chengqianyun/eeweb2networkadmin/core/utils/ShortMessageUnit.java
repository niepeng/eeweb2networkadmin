package com.chengqianyun.eeweb2networkadmin.core.utils;


/**
 * @author 聂鹏
 * @version 1.0
 * @date 18/8/22
 */

public class ShortMessageUnit {
/****************************************发送短信****************************************************/
  /**
   * 根据 短信中心号,手机号,发送短信的内容,返回需要发送命令的字符串
   * @param centerNo 短信中心号
   * @param phoneNo 手机号
   * @param msg 发送短信的内容
   * @date:2010-4-28
   */
  public static String code4sendMessage(String centerNo, String phoneNo, String msg){
    /**
     * 示例:0891683108501705F011000B813137185940F80008A71E4F60597DFF0C00680065006C006C006F002E003100320033002100210021
     */
    String smsStr = "";

    StringBuffer strBuf = new StringBuffer();
    //1.08：短信中心地址长度加上2除以2(一般是8)
    //strBuf.append("08");(centerNo如:13800571500)
    int centerNolen = (centerNo.length() + (centerNo.length()%2==0?0:1) + 4)/2;// 保证是偶数
    strBuf.append(formatIntToHexStr(centerNolen));//(一般是8)
    //2.91：短信中心号码类型(可以固定不变) + 68：中国地区代码(可以固定不变)
    strBuf.append("91");
    //3.68：中国地区代码(可以固定不变)
    strBuf.append("68");
    //4.3108401505F0：短信服务中心号码
    strBuf.append(changeOddEven4Send(centerNo));
    //5.1100：发送短信的编码方式(可以固定不变)
    strBuf.append("1100");
    //6.0B：目的手机号码长度
    strBuf.append(formatIntToHexStr(phoneNo.length()));
    //7.81：目的地址类型(可以固定不变)
    strBuf.append("81");
    //8.目的手机号码 --即接收方号码
    strBuf.append(changeOddEven4Send(phoneNo));
    //9.0008：发送中文字符方式（可以固定不变）
    strBuf.append("0008");
    //10.A7：（可以固定不变）短消息有效发送时间
    strBuf.append("A7");
    //11.短信内容长度(字节数)
    String str = UnicodeZhCN.toUnicode(msg);
    strBuf.append(formatIntToHexStr(str.length()/2));//返回的unicode码长度除以2
    //12.发送中文字符的UNICODE码（根据发送内容进行变动）
    strBuf.append(str);
    //13.发送结束标志（十六进制为0x1A），表示短信码结束。(一个完整的短信发送命令应该到13,这里不用加)
    return strBuf.toString().toUpperCase();
  }

  /****************************************接受短信****************************************************/
  /**
   * 把接收来的字符串解析成，手机号码，时间，信息内容
   * @param receviceInfo 接收到的字符串
   */
//  public static SmsRecord receiveMessage(String receviceInfo){
//    /**2个示例对比分析:
//     * 08 91 68 3108501705F0 04 0D 91683137185940F8 0008 01407241901323 06 607060706070
//     * 08 91 68 3108501755F1 24 05 A10180F6         0008 01408011609323 5A 省略...
//     * 08(后面短信中心的字节数,一般短信中心就是后08*2个数)
//     * 91：短信中心号码类型(可以固定不变) + 68：中国地区代码(可以固定不变)
//     * 短信中心号码奇偶位交换:3108501755F1
//     * 04或者24 (PDU类型)
//     * 05(发送人号码的的位数)
//     * 0180F6(发送人号码如果有F去掉F)---这个我们需要(这里发件人就是10086,因此发件人应该从0008往上数6(遇到奇数就加1,因为补了一个F))个数字,
//     * 0008(固定)
//     * 01408011609323(短信接收的时间-奇偶位交换)--(10-04-08 11:06:39:32)---这个我们需要
//     * 06(后面内容部分的字节数,一般是后面0x06*2个数)---这个我们需要
//     */
//    // 由于短信接收年份时间显示只有后面两位,比如:2010,只显示10,这里perYear就要赋值为20
//    String perYear = FunctionUnit.getPerYear();
//
//    SmsRecord  bean = new SmsRecord();
//    bean.setSmstype(2);
//    try {
//      SimpleDateFormat dateFormat = new SimpleDateFormat(FunctionUnit.DATA_FORMAT_LONG);
//      String str = "";
//      int tempInt = 0;
//      int index = 0;
//      str = receviceInfo.substring(0,2);
//      tempInt = Integer.parseInt(str, 16);							// 获取短信中心号码所占的长度(按照示例中这时tempInt=8)
//      index = tempInt * 2 + 4;										// (按照示例中这时index=20)
//      str = receviceInfo.substring(index,index+2);					// (按照示例中这时:substring(20,22))
//      tempInt = Integer.parseInt(str, 16);							// 获取发件人号码所占的长度(按照示例中这时tempInt=13)
//      tempInt = tempInt + (tempInt%2==0?0:1);							// 获取的长度如果是奇数就加1(按照示例中这时tempInt=14)
//      index = index + 4;												// (按照示例中这时index=24)
//      // 获取发件人手机号码
//      str = receviceInfo.substring(index,index+tempInt);				// (按照示例中这时:substring(24,24+14))
//      bean.setSmsphone(changeOddEven4Receive(str));					// **业务数据**发件人手机号码:(按照示例中这时手机号码为:8613738195048)
//      // 获取短信发送时间(0008后面14位)
//      index = index + tempInt + 4;									// (按照示例中这时index=24+14+4=42)
//      str = receviceInfo.substring(index,index+14);					// (按照示例中这时:substring(42,42+14))
//      bean.setSmsrectime(dateFormat.parse(perYear+packup2TimeString(changeOddEven4Receive(str))));// **业务数据**发件时间:(按照示例中这时发件时间为:10-04-27 14:09:31)
//      // 获取短信内容
//      index = index + 14;												// (按照示例中这时index=56)
//      str = receviceInfo.substring(index,index+2);					// (按照示例中这时:substring(56,58))
//      tempInt = Integer.parseInt(str, 16);							// 获取短信内容所占的长度(按照示例中这时tempInt=06个字节)
//      index = index + 2;												// (按照示例中这时index=58)
//      str = receviceInfo.substring(index,(index+tempInt*2));			// (按照示例中这时:substring(58,70))
//      bean.setSmscontent(UnicodeZhCN.tozhCN(str));					// **业务数据**短信内容解码:(按照示例中这时短信内容解码为:恰恰恰)
//    } catch (Exception e) {
//      bean = null;
//      e.printStackTrace();
//    }
//    return bean;
//  }

  /********************************工具方法**************************************/
  /**
   * 发送用:先判断source长度是否是奇数,是的话就在后面加F,然后再把source奇偶位交换,返回交换后在值
   */
  private static String changeOddEven4Send(String source) {
    StringBuffer strBuf = new StringBuffer();
    String code = "";
    source = source + (source.length()%2==0?"":"F");
    int len = source.length();
    int beginIndex = 0;
    while (len > beginIndex) {
      code = source.substring(beginIndex+1, beginIndex+2) +
          source.substring(beginIndex, beginIndex+1);
      strBuf.append(code);
      beginIndex += 2;
    }
    return strBuf.toString();
  }
  /**
   * 接收用:把source奇偶位交换,交换后最后一位如果是F,就去掉这个F
   */
  private static String changeOddEven4Receive(String source) {
    String rsStr = "";
    int len = source.length();
    int beginIndex = 0;
    while (len > beginIndex) {
      rsStr += source.substring(beginIndex+1, beginIndex+2) +
          source.substring(beginIndex, beginIndex+1);
      beginIndex += 2;
    }
    rsStr = rsStr.replace("F", "");
    return rsStr;
  }

  /**
   * 把从短信中获取的时间字符(即source)格式成时间字符<br>
   * 如:source=10042714093132<br>
   * 格式后变成:10-04-27 14:09:31(后面的32忽略)<br>
   * 必须满足条件: source.length()>=12,否则返回null值.
   */
  private static String packup2TimeString(String source) {
    String rsStr = "";
    int len = source.length();
    if (len>=12){
      // 格式:年-月-日 时:分:秒
      rsStr = source.substring(0, 2) + "-" +
          source.substring(2, 4) + "-" +
          source.substring(4, 6) + " " +
          source.substring(6, 8) + ":" +
          source.substring(8, 10) + ":" +
          source.substring(10, 12);
    }else{
      rsStr = null;
    }
    return rsStr;
  }
  /**
   * 把数字转换成16进制格式字符,如果转换后的字符只占一位,再前面补0
   */
  private static String formatIntToHexStr(int source){
    String formatStr = Integer.toHexString(source);
    formatStr = (formatStr.length() == 1?"0":"") + formatStr;
    return formatStr;
  }

  public static void main(String[] args) {
    String result = code4sendMessage("13800571500", "15372095699", "我是短信内容,发送试一下");
    System.out.println(result);
  }

//	public static void main(String args[]) {
//		System.out.println(ShortMessageUnit.code4sendMessage("13800571500","13738195048","今天，你好，hello.123!!!。。。"));
//		String receviceInfo = "0891683108501705F0040D91683137185940F80008015031412241230854C854C800310031";
//		SmsRecord recevice = ShortMessageUnit.receiveMessage(receviceInfo);
//		System.out.println("发件号码:" + recevice.getSmsphone());
//		System.out.println("发件内容:" + recevice.getSmscontent());
//		System.out.println("发件时间:" + recevice.getSmsrectime().toLocaleString());
//	}
}