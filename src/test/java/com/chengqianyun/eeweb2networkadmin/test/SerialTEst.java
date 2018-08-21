package com.chengqianyun.eeweb2networkadmin.test;

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

  static int baudrate = 9600;
  static long waitMillTime = 500;
  static int PACKET_LENGTH = 500;

  public static void main(String[] args) {
    List<String> list = getAllComPorts();
    System.out.println("list Size= " + list.size());
    for (String tmp : list) {
      System.out.println("value===>" + tmp);
    }
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
      flag = sendCommand(in,out,"ATD"+phone+";\r");
      System.out.println("打电话结果:" + flag);

      Thread.sleep(15 * 1000);
      sendCommand(in,out,"ATH;\r");

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
        in.close();
        out = null;
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
    write("ATE0\r",out);
    boolean flag = read(in);
    System.out.println("firstFlag=" + flag);
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
    int numBytes = 0;
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
    CommPortIdentifier portIdRs = null;

    while (en.hasMoreElements()) {
      portIdRs = (CommPortIdentifier) en.nextElement();
      if (portIdRs.getPortType() == CommPortIdentifier.PORT_SERIAL) {
        comList.add(portIdRs.getName());
      }
    }
    return comList;
  }


}