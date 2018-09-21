package com.chengqianyun.eeweb2networkadmin.service;


import com.chengqianyun.eeweb2networkadmin.biz.enums.SettingEnum;
import com.chengqianyun.eeweb2networkadmin.core.utils.ShortMessageUnit;
import com.chengqianyun.eeweb2networkadmin.core.utils.SystemClock;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 串口封装操作,目前主要是:打电话和发送短信
 *
 * @author 聂鹏
 * @version 1.0
 * @date 18/8/22
 */
@Service
@Slf4j
public class SerialService extends BaseService {

  private static boolean isRunning;

  private SerialPort serialPort;

  private InputStream in;

  private OutputStream out;


  static boolean inited = false;
  static int MAX_SEND_LEN = 70;			// 单条短信最长的发送字数(70个)
  static int SMS_SEND_INTERVAL =5*1000;	// 短信发完后等待间隔(5000毫秒)
  static int baudrate = 9600;         // 波特率
  static long waitMillTime = 500;
  static int PACKET_LENGTH = 500;

  static volatile long closeSerialTime = 0;

  /**
   * 是否是强制执行初始化
   *
   * @param isForce
   */
  public synchronized void init(boolean isForce, boolean checkSmsPhoneOpenClosed) {

    boolean sms = Boolean.valueOf(getData(SettingEnum.alarm_sms));
    boolean phone = Boolean.valueOf(getData(SettingEnum.alarm_phone));
    if(checkSmsPhoneOpenClosed) {
      if(!sms && !phone) {
        return;
      }
    }

    if (!isForce && isRunning) {
      return;
    }

    log.error("init-ReadyClose");
    close();
    log.error("init-Closed");


    try {
      log.error("init-getAllCom");
      SystemClock.sleepRandom(1000 , 2000);
      List<String> list = getAllComPorts();
      if (list == null || list.size() == 0) {
        isRunning = false;
        return;
      }
      log.error("init-getAllCommSize=" + list.size());

      for (String comm : list) {
        log.error("init-try:" + comm);
        if (initExecute(comm)) {
          log.error("currentMatchSmsComm->" + comm);
          inited = true;
          isRunning = true;
          return;
        }
        SystemClock.sleep(200);
      }

    } catch (Exception e) {
      log.error("SerialService.init.Exception", e);
    }catch (Error e) {
      log.error("SerialService.init.Error", e);
    }
    isRunning = false;
  }

  public boolean testSerial() {
    if (isRunning) {
      return true;
    }

    init(true, false);
    boolean flag = isRunning;
    close();
    return flag;
  }

  public synchronized boolean callPhoneOrSms(String phone, String smsContent, boolean isSms) {
    if (!isRunning) {
      init(true, false);
      delayCloseSerial();
    }

    if (isSms) {
      return sendSms(phone, smsContent);
    }
    try {
      return callPhone(phone);
    } catch (IOException e) {
    } catch (InterruptedException e) {
    }
    return false;
  }

  private void delayCloseSerial() {
    if (closeSerialTime == 0) {
      closeSerialTime = System.currentTimeMillis() + 10 * 60 * 1000;
      new Thread(new Runnable() {
        @Override
        public void run() {
          while (System.currentTimeMillis() < closeSerialTime) {
            SystemClock.sleep(25 * 1000);
          }
          closeSerialTime = 0;
          close();
        }
      }).start();
      return;
    }
    closeSerialTime = 10 * 60 * 1000 + System.currentTimeMillis();
  }


  /**
   * 拨打电话
   *
   * @param phone
   * @return
   */
  private  boolean callPhone(String phone) throws IOException, InterruptedException {
    boolean phoneFlag = Boolean.valueOf(getData(SettingEnum.alarm_phone));
    if(!phoneFlag) {
      return false;
    }

    boolean flag1 = sendCommand("ATD" + phone + ";\r");
    SystemClock.sleep(30 * 1000);
    boolean flag2 = sendCommand("ATH\r");
    return flag1 && flag2;
  }

  /**
   * 发送短信
   *
   * @param phone
   * @param content
   * @return
   */
  private  boolean sendSms(String phone, String content) {
    boolean smsFlag = Boolean.valueOf(getData(SettingEnum.alarm_sms));
    if(!smsFlag) {
      return false;
    }
    String centerNo = getData(SettingEnum.sms_center);
    return sendMessage(centerNo, phone, content);
  }

  public synchronized void close() {

    log.error("executeClosedStart");

    if (out != null) {
      try {
        out.close();		in.close();
        out = null;			in = null;
      } catch (IOException e) {
        log.info("关闭串口时出错:" + e.getMessage() + e.toString());
      }
    }

    if(serialPort != null) {
      serialPort.notifyOnDataAvailable(false);
      serialPort.notifyOnBreakInterrupt(false);
      serialPort.removeEventListener();
      serialPort.close();
      serialPort = null;
    }


//    try {
//      if (serialPort != null) {
//        serialPort.notifyOnDataAvailable(false);
//        serialPort.notifyOnBreakInterrupt(false);
//        serialPort.removeEventListener();
//        serialPort.close();
//        serialPort = null;
//      }
//    } catch (Error e) {
//      log.error("serialCloseError", e);
//    }
//
//    if (out != null) {
//      try {
//        out.close();
//        out = null;
//      } catch (Exception e) {
//        log.error("closedOutException", e);
//      } catch (Error e) {
//        log.error("closedOutError", e);
//      }
//    }
//    log.error("executeClosedOutEnd");
//
//    if (in != null) {
//      try {
//        in.close();
//        in = null;
//      } catch (Exception e) {
//        log.error("closedInException", e);
//      } catch (Error e) {
//        log.error("closedOutError", e);
//      }
//    }
    log.error("executeClosedEnd");
    isRunning = false;
  }


  /**
   * 给手机号码为phoneNo发送msg的短消息. 返回:true:成功  false:失败
   * @param centerNo 短信中心号
   * @param phoneNo 手机号
   * @param message 发送短信的内容
   */
  private boolean sendMessage(String centerNo, String phoneNo, String message) {
    boolean rsbool = true;
    String msg = "";          // 经过处理的,要发送的短信内容
    String sendmsg4At = "";      // 经过编码后,要发送的信息
    int msgLen = message.length();  // 短信内容的中长度(太长会被分几次发送)
    int len = 0;          // 单次发送的总长度
    int beginIndex = 0;      // 截取字符串的的其实位置
    int subLen = 0;          // 临时变量
    int index = 0;
    boolean hasNext = true;
    try {
      subLen = msgLen;
      while (hasNext) {
        // 截取短信内容
        if (subLen > MAX_SEND_LEN) {
          subLen = MAX_SEND_LEN;
        } else {
          hasNext = false;
        }
        msg = message.substring(index, index + subLen);
        index = index + subLen;
        subLen = msgLen - index;

        // 发送短信内容
        //centerNo = "+86" + centerNo;
        sendmsg4At = ShortMessageUnit.code4sendMessage(centerNo, phoneNo, msg);// 发送短信AT指令需要的字符串
        len = sendmsg4At.length();
        beginIndex = Integer.parseInt(sendmsg4At.substring(0, 2), 16) * 2 + 2;    // 短信中心所占的长度
        String content1 = "AT+CMGS=" + ((len - beginIndex) / 2) + "\r";
        String content2 = sendmsg4At + "\r";
        String content3 = (char) 26 + "\r";
        write(content1, out);
        write(content2, out);   // ② 发送AT指令(发送短信内容)
        write(content3, out);   // ③ 发送AT指令(发送结束符号""(即(char)26))

        Thread.sleep(SMS_SEND_INTERVAL);                      // ④发完短信等待SMS_SEND_INTERVAL毫秒
//         sendATCommand("AT\r");
        //rsbool = receiveATResponse(RESPONSE_OK,RESPONSE_ERROR,1)!=null;				// 读取模块响应结果 -- 可以看到结果 -- 测试用
        rsbool = read(in); // 读取模块响应结果 -- 正式用
      }
    } catch (Exception e) {
      rsbool = false;
    }
    return rsbool;
  }





  private List<String> getAllComPorts() {
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
   * 根据串口的comm名称,建立comm端口连接
   *
   * @param commName
   * @return
   */
  private boolean initExecute(String commName) throws PortInUseException, IOException, NoSuchPortException, UnsupportedCommOperationException, InterruptedException {
    CommPortIdentifier commPortIdentifier = CommPortIdentifier.getPortIdentifier(commName);
    if (commPortIdentifier != null) {
      serialPort = (SerialPort) commPortIdentifier.open("sms_port", 60);
      serialPort.setSerialPortParams(baudrate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
      serialPort.setDTR(true);
      serialPort.setRTS(true);

      in = serialPort.getInputStream();
      out = serialPort.getOutputStream();

      if (initSmsInfo()) {
        return true;
      }
    }

    return false;
  }

  private boolean initSmsInfo() throws IOException, InterruptedException {
    if (inited) {
      return true;
    }

    /**
     * 1:设置不显示回显:ATE0
     * 输入：ATE[<value>]<CR> 参数：<value>―0表示不回显；1表示回显。
     * 响应：<CRLF>OK<CRLF>  //表示成功
     */
    boolean flag = sendCommand("ATE0\r");
    if (!flag) {
      return false;
    }

    /**
     *  2:设置短信格式:AT+CMGF=0
     *  输入：AT+CMGF=[<mode>]<CR> 参数：<mode>―取0为PDU模式，取1为文本模式，即Text模式。
     *  响应：<CRLF>OK<CRLF>
     */
    flag = sendCommand("AT+CMGF=0\r");
    if (!flag) {
      return false;
    }

    /**
     * 3.设置接收的短信不保存在sim卡上: AT+CNMI=2,2
     */
    flag = sendCommand("AT+CNMI=2,2\r");

    return flag;
  }


  private boolean sendCommand(String content) throws IOException, InterruptedException {
    write(content, out);
    boolean flag = read(in);
    return flag;
  }

  private boolean read(InputStream in) throws InterruptedException {
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

  public String readPackData(InputStream in) {
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


  public void write(String content, OutputStream out) throws IOException, InterruptedException {
    out.write(content.getBytes());
    out.flush();
    Thread.sleep(waitMillTime);
  }

}