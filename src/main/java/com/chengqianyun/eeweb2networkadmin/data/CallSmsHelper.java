package com.chengqianyun.eeweb2networkadmin.data;

import com.chengqianyun.eeweb2networkadmin.biz.bean.PhoneTask;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.Setting;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.SettingMapper;
import com.chengqianyun.eeweb2networkadmin.biz.enums.SettingEnum;
import com.chengqianyun.eeweb2networkadmin.core.utils.ShortMessageUnit;
import com.chengqianyun.eeweb2networkadmin.core.utils.Tuple2;
import com.chengqianyun.eeweb2networkadmin.service.PhoneSmsService;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author 聂鹏
 * @version 1.0
 * @email lsb@51huadian.cn
 * @date 19/3/18
 */
@Component
@Slf4j
public class CallSmsHelper {

  @Autowired
  protected SettingMapper settingMapper;

  static int MAX_SEND_LEN = 70;			// 单条短信最长的发送字数(70个)
  static int SMS_SEND_INTERVAL =5*1000;	// 短信发完后等待间隔(5000毫秒)
  static int baudrate = 9600;         // 波特率
  static long waitMillTime = 500;
  static int PACKET_LENGTH = 500;
//  static String centerNo = "13010360500";   // 短信中心号码


  static SerialPort serialPort;


  public void init() {
    List<String> list = getAllComPorts();
    if (list == null || list.size() == 0) {
      return;
    }

    for(String comName : list) {
      if(findComm(comName)) {
        break;
      }
    }

  }

  public synchronized boolean checkSerialAvailable() {
    if (serialPort != null) {
      return true;
    }
    init();
    if (serialPort == null) {
      log.error("init serialPort fail,串口无法控制发送短信和拨打电话");
      return false;
    }
    PhoneSmsService.markRunning();
    return true;
  }

  /**
   * Tuple2<Boolean, Boolean>  第一个返回拨打电话情况成功或失败,第二个返回发送短信成功或失败;
   *
   * @param phoneTask
   * @return
   */
  public Tuple2<Boolean, Boolean> execute(PhoneTask phoneTask) {
    Tuple2<Boolean, Boolean> tuple2 = new Tuple2<Boolean, Boolean>();
    if(serialPort == null) {
      init();
    }

    if(serialPort == null) {
      log.error("init serialPort fail,串口无法控制发送短信和拨打电话");
      return tuple2;
    }

    PhoneSmsService.markRunning();

     if(phoneTask.isCall()) {
       tuple2.setT1(callPhone(phoneTask.getPhone()));
     }

     if(phoneTask.isSms()) {
       try {
         tuple2.setT2(sendMessage(URLDecoder.decode(getData(SettingEnum.sms_center), "utf-8"), URLDecoder.decode(phoneTask.getPhone(), "utf-8"), phoneTask.getSmsContent()));
       } catch (Exception e) {
         log.error("发送短信异常;{}", e);
       }
     }
    return tuple2;
  }

  public void close() {
    if(serialPort != null) {
      try {
        close(serialPort);
      } catch (IOException e) {
        log.error("close短信通道异常,{}", e);
      }
    }
  }

  /**
   * 关闭串口，释放资源
   *
   * @param serialPort
   * @throws IOException
   */
  public void close(SerialPort serialPort) throws IOException {
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


  /**
   * 打电话
   */
  private boolean callPhone(String phone) {
    try {
      boolean flag = sendCommand("ATD" + phone + ";\r");
      System.out.println("打电话结果:" + flag);
      Thread.sleep(30 * 1000);
      sendCommand("ATH\r");
      return true;
    } catch (Exception e) {
      log.error("拨打电话异常:{}", e);
    }
    return false;
  }

  /**
   * 给手机号码为phoneNo发送msg的短消息. 返回:true:成功  false:失败
   *
   * @param centerNo 短信中心号
   * @param phoneNo 手机号
   * @param message 发送短信的内容
   */
  private boolean sendMessage(String centerNo, String phoneNo, String message) {
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
      InputStream in = serialPort.getInputStream();
      OutputStream out = serialPort.getOutputStream();
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

        log.info("content1==>" + content1);
        log.info("content2==>" + content2);
        log.info("content3==>" + content3);

        Thread.sleep(SMS_SEND_INTERVAL);											// ④发完短信等待SMS_SEND_INTERVAL毫秒
        rsbool = read(in); // 读取模块响应结果 -- 正式用
      }
    } catch (Exception e) {
      log.error("执行发送短信出错,{}", e);
      rsbool = false;
    }
    return rsbool;
  }


  private boolean findComm(String comName) {
    try {
      CommPortIdentifier commPortIdentifier = CommPortIdentifier.getPortIdentifier(comName);
      if (commPortIdentifier == null) {
        return false;
      }
      serialPort = (SerialPort) commPortIdentifier.open("sms_port", 60);
      serialPort.setSerialPortParams(baudrate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
      serialPort.setDTR(true);
      serialPort.setRTS(true);

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

    }catch (UnsupportedCommOperationException e) {
      e.printStackTrace();
    } catch (NoSuchPortException e) {
      e.printStackTrace();
    } catch (PortInUseException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return false;
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


  private boolean sendCommand(String content) throws IOException, InterruptedException {
    write(content, serialPort.getOutputStream());
    boolean flag = read(serialPort.getInputStream());
    return flag;
  }

  private boolean read(InputStream in) throws InterruptedException {
    int retry_Time = 3;
    String result = null;
    while (retry_Time > 0) {
      result = readPackData(in);
      log.info(retry_Time + ",content=" + result);
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

  public String getData(SettingEnum settingEnum) {
    Setting setting = settingMapper.selectByCode(settingEnum.getCode());
    if(setting == null) {
      return  settingEnum.getDefaultValue();
    }
    return setting.getParamValue();
  }


}