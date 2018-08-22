package com.chengqianyun.eeweb2networkadmin.test;

import com.chengqianyun.eeweb2networkadmin.core.utils.ShortMessageUnit;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @author 聂鹏
 * @version 1.0
 * @email lsb@51huadian.cn
 * @date 18/8/21
 */

public class SerialTest {

  static int MAX_SEND_LEN = 70;			// 单条短信最长的发送字数(70个)
  static int SMS_SEND_INTERVAL =5*1000;	// 短信发完后等待间隔(5000毫秒)
  static int baudrate = 9600;
  static long waitMillTime = 500;
  static int PACKET_LENGTH = 500;
  static String centerNo = "13010360500";   // 短信中心号码

  public static void main(String[] args) throws Exception {
//    List<String> list = getAllComPorts();
//    System.out.println("list Size= " + list.size());
//    for (String tmp : list) {
//      System.out.println("value===>" + tmp);
//      callPhone(tmp, "15372095699");
//      System.out.println("\n\n");
//    }

    callPhone("COM3", "15372095699");
//    boolean flag = sendMessage("13010360500", "13958190387", "博万通信", null, null);
//    System.out.println("短信发送结果:" + flag);
  }

  public static void callPhone(String commName, String phone) throws Exception {
    CommPortIdentifier commPortIdentifier = CommPortIdentifier.getPortIdentifier(commName);
    if (commPortIdentifier != null) {
      SerialPort serialPort = (SerialPort) commPortIdentifier.open("sms_port", 60);
      serialPort.setSerialPortParams(baudrate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
      serialPort.setDTR(true);
      serialPort.setRTS(true);

      InputStream in = serialPort.getInputStream();
      OutputStream out = serialPort.getOutputStream();

      /**
       * 1:设置不显示回显:ATE0
       * 输入：ATE[<value>]<CR> 参数：<value>―0表示不回显；1表示回显。
       * 响应：<CRLF>OK<CRLF>  //表示成功
       */
      boolean flag = sendCommand(in,out,"ATE0\r");
      if(!flag) {
        System.out.println("第一次发送指令返回失败了");
        return;
      }
      System.out.println("第一次发送指令返回成功了");


      /**
       *  2:设置短信格式:AT+CMGF=0
       *  输入：AT+CMGF=[<mode>]<CR> 参数：<mode>―取0为PDU模式，取1为文本模式，即Text模式。
       *  响应：<CRLF>OK<CRLF>
       */
      flag = sendCommand(in,out,"AT+CMGF=0\r");
      if(!flag) {
        System.out.println("第二次发送指令返回失败了");
        return;
      }
      System.out.println("第二次发送指令返回成功了");


      /**
       * 3.设置接收的短信不保存在sim卡上: AT+CNMI=2,2
       */
      flag = sendCommand(in,out,"AT+CNMI=2,2\r");
      if(!flag) {
        System.out.println("第三次发送指令返回失败了");
        return;
      }
      System.out.println("第三次发送指令返回成功了");

      /**
       * 测试打电话
       */
//      flag = sendCommand(in,out,"ATD"+phone+";\r");
//      System.out.println("打电话结果:" + flag);
//
//      Thread.sleep(30 * 1000);
//      sendCommand(in,out,"ATH\r");

      Thread.sleep(2 * 1000);
      System.out.println("\n 准备发送短信:");
//      flag = sendMessage(URLDecoder.decode("13800571500", "utf-8"), URLDecoder.decode("15372095699", "utf-8"), URLDecoder.decode("我是短信内容,发送试一下", "utf-8"), out, in);
      flag = sendMessage(centerNo, phone, "博万通信", out, in);
      System.out.println("短信发送结果:" + flag);

      Thread.sleep(2 * 1000);
      close(serialPort);
      Thread.sleep(2 * 1000);
    }
  }

  /**
   * 关闭串口，释放资源
   *
   * @param serialPort
   * @throws IOException
   */
  private static void close(SerialPort serialPort) throws IOException {
    OutputStream out = serialPort.getOutputStream();
    InputStream in = serialPort.getInputStream();
    if (out != null) {
      try {
        out.close();
        out = null;
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    if (in != null) {
      try {
        in.close();
        in = null;
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    if (serialPort != null) {
      serialPort.notifyOnDataAvailable(false);
      serialPort.notifyOnBreakInterrupt(false);
      serialPort.removeEventListener();
      serialPort.close();
      serialPort = null;
    }

  }

  private static boolean sendCommand(InputStream in, OutputStream out, String content) throws IOException, InterruptedException {
    write(content, out);
    boolean flag = read(in);
    System.out.println(content + ",currentFlag=" + flag);
    return flag;
  }

  private static boolean read(InputStream in) throws InterruptedException {
    int retry_Time = 3;
    String result = null;
    while (retry_Time > 0) {
      result = readPackData(in);
      System.out.println(retry_Time + ",content=" + result);
      if (result != null && result.length() > 0) {
        if (result.toLowerCase().indexOf("ok") >= 0) {
          return true;
        }
        if (result.toLowerCase().indexOf("error") >= 0) {
          return false;
        }
      }
      Thread.sleep(waitMillTime);
      retry_Time--;
    }

    return false;
  }

  public static String readPackData(InputStream in) {
    StringBuilder result = new StringBuilder();
    byte[] readBuffer = new byte[PACKET_LENGTH];
    int numBytes;
    if (in == null) {
      return null;
    }
    try {
      while (in.available() > 0) {
        numBytes = in.read(readBuffer);
        for (int i = 0; i < numBytes; i++) {
          result.append((char) (readBuffer[i]));
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return result.toString();
  }


  public static void write(String content, OutputStream out) throws IOException, InterruptedException {
    out.write(content.getBytes());
    out.flush();
    Thread.sleep(waitMillTime);
  }

  public static List<String> getAllComPorts() {
    List<String> comList = new ArrayList<String>();
    Enumeration en = CommPortIdentifier.getPortIdentifiers();
    CommPortIdentifier portIdRs;

    while (en.hasMoreElements()) {
      portIdRs = (CommPortIdentifier) en.nextElement();
      if (portIdRs.getPortType() == CommPortIdentifier.PORT_SERIAL) {
        comList.add(portIdRs.getName());
      }
    }
    return comList;
  }

  /**
   * 给手机号码为phoneNo发送msg的短消息. 返回:true:成功  false:失败
   * @param centerNo 短信中心号
   * @param phoneNo 手机号
   * @param message 发送短信的内容
   */
  public static boolean sendMessage(String centerNo, String phoneNo, String message, OutputStream out, InputStream in) {
    boolean rsbool = true;
    String msg ="";					// 经过处理的,要发送的短信内容
    String sendmsg4At = "";			// 经过编码后,要发送的信息
    int msgLen = message.length();	// 短信内容的中长度(太长会被分几次发送)
    int len = 0;					// 单次发送的总长度
    int beginIndex = 0; 			// 截取字符串的的其实位置
    int subLen = 0;					// 临时变量
    int index = 0;
    boolean hasNext = true;
    try {
      subLen = msgLen;
      while (hasNext) {
        // 截取短信内容
        if (subLen > MAX_SEND_LEN){
          subLen = MAX_SEND_LEN;
        }else{
          hasNext = false;
        }
        msg = message.substring(index, index + subLen);
        index = index + subLen;
        subLen = msgLen - index;

        // 发送短信内容
        //centerNo = "+86" + centerNo;
        sendmsg4At = ShortMessageUnit.code4sendMessage(centerNo, phoneNo, msg);// 发送短信AT指令需要的字符串
        len = sendmsg4At.length();
        beginIndex = Integer.parseInt(sendmsg4At.substring(0, 2),16) * 2 + 2;		// 短信中心所占的长度
          String content1 = "AT+CMGS=" + ((len - beginIndex)/2) + "\r";
          String content2 = sendmsg4At + "\r";
          String content3 = (char)26 + "\r";
          write(content1, out);
          write(content2, out);   // ② 发送AT指令(发送短信内容)
          write(content3, out);   // ③ 发送AT指令(发送结束符号""(即(char)26))

          System.out.println("content1==>" + content1);
          System.out.println("content2==>" + content2);
          System.out.println("content3==>" + content3);



//        write(content1, out);
//        write(content2, out);
//        write(content3, out);

        Thread.sleep(SMS_SEND_INTERVAL);											// ④发完短信等待SMS_SEND_INTERVAL毫秒
//         sendATCommand("AT\r");
        //rsbool = receiveATResponse(RESPONSE_OK,RESPONSE_ERROR,1)!=null;				// 读取模块响应结果 -- 可以看到结果 -- 测试用
        rsbool = read(in); // 读取模块响应结果 -- 正式用
      }
    } catch (Exception e) {
      rsbool = false;
    }
    return rsbool;
  }



}