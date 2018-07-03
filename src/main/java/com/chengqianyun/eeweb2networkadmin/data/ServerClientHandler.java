package com.chengqianyun.eeweb2networkadmin.data;

import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceDataIntime;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceInfo;
import com.chengqianyun.eeweb2networkadmin.biz.enums.DataStatusEnum;
import com.chengqianyun.eeweb2networkadmin.biz.enums.DeviceTypeEnum;
import com.chengqianyun.eeweb2networkadmin.biz.enums.StatusEnum;
import com.chengqianyun.eeweb2networkadmin.core.utils.IoUtil;
import com.chengqianyun.eeweb2networkadmin.core.utils.SpringContextHolder;
import com.chengqianyun.eeweb2networkadmin.core.utils.SystemClock;
import com.chengqianyun.eeweb2networkadmin.core.utils.Tuple2;
import com.chengqianyun.eeweb2networkadmin.core.utils.data.CalcCRC;
import com.chengqianyun.eeweb2networkadmin.core.utils.data.Char55util;
import com.chengqianyun.eeweb2networkadmin.core.utils.data.FunctionUnit;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * 客户端线程 用于处理一个客户端的Socket链路
 *
 * @author niepeng
 */
@Slf4j
@Getter
public class ServerClientHandler implements Runnable {

  private Socket socket;

  private static OptDataHelper optDataHelper;

  public ServerClientHandler(Socket socket) {
    this.socket = socket;
  }



  @Override
  public void run() {

    if (optDataHelper == null) {
      optDataHelper = SpringContextHolder.getBean(OptDataHelper.class);
    }

    /**
     * 1.接收到第一次客户端连接上来
     * 2.建立sokcet和设备的关系
     * 3.持续发送和获取数据(一段时间发送和接收),如果连接断了,释放当前链路,重新尝试
     */
    InputStream in = null;
    PrintWriter out = null;
    try {
      in = socket.getInputStream();

      // 1.接收到第一次客户端连接上来
      char[] readData = read(in);
      String tmpData1 = FunctionUnit.bytesToHexString(readData);
      log.info("tmpData1==>(" + tmpData1 + ")");

      // 2.建立sokcet和地址的关系
      log.info("发送获取sn和地址指令：" + FunctionUnit.bytesToHexString(InstructionManager.genGetSnAddress()));

      writePort(InstructionManager.genGetSnAddress(), socket);

      SystemClock.sleep(6000);

      char[] readData2 = read(in);
      String tmpData2 = FunctionUnit.bytesToHexString(readData2);
      readData2 = Char55util.dealwith55NewV2(readData2);
      log.info("tmpData2==>(" + tmpData2 + ")");

      Tuple2<String, Integer> snAddressTuple = InstructionManager.parseGetSnAddress(readData2);
      if(snAddressTuple == null) {
        return;
      }

      String sn = snAddressTuple.getT1();
      int address = snAddressTuple.getT2();
      log.info("sn={},address={}", sn, address);
      DeviceInfo deviceInfo = ServerConnectionManager.addSnConnection(sn, address, this);
      log.info("addSnConnection_result={}", deviceInfo);

      //  3.持续发送和获取数据(一段时间发送和接收),如果连接断了,释放当前链路,重新尝试
      DeviceDataIntime tmpDeviceData = null;
      Tuple2<StatusEnum, Boolean> tuple = null;
      while (true) {
        deviceInfo = ServerConnectionManager.snDeviceSocketMap.get(deviceInfo.getSn());
        if (DeviceTypeEnum.hasEnv(deviceInfo.getType())) {
          char[] data = writeInstruction(address, InstructionManager.genGetEnv(address));
          tmpDeviceData = InstructionManager.parseGetEnv(data, address);
          log.info("接收到环境数据结果解析==>" + tmpDeviceData);
        }

        if (DeviceTypeEnum.hasIn(deviceInfo.getType())) {
          char[] data = writeInstruction(address, InstructionManager.genGetIn(address));
          tmpDeviceData = InstructionManager.parseGetIn(data, address, tmpDeviceData);
          log.info("接收到开关量输入数据结果解析==>" + tmpDeviceData);
        }

        if (DeviceTypeEnum.hasOut(deviceInfo.getType())) {
          char[] data = writeInstruction(address, InstructionManager.genGetOut(address, deviceInfo.getControlWay()));
          tuple = InstructionManager.parseGetOut(data, address);
          log.info("接收到开关量输出数据结果解析==>" + tuple);
        }

        optDataHelper.optData(tmpDeviceData, tuple, deviceInfo);

        tuple = null;
        tmpDeviceData = null;
      }
    } catch (Exception e) {
      log.error("ServerHandler.runError", e);
    } finally {
      IoUtil.close(in);
      in = null;
      IoUtil.close(out);
      out = null;
      IoUtil.close(socket);
      socket = null;
    }
  }

  private char[] writeInstruction(int address, char[] writeData) throws IOException {
    DataStatusEnum tmpDataStatusEnum;
    for (int i = 0; i < ServerConnectionManager.FAIL_TIMES_RETURN; i++) {
      log.info("发送指令:" + FunctionUnit.bytesToHexString(writeData));
      writePort(writeData, socket);
      SystemClock.sleep(ServerConnectionManager.GET_DATA_CYCLE * 1000);
      char[] readData3 = read(socket.getInputStream());
      log.info("接收到数据结果====>" + FunctionUnit.bytesToHexString(readData3));
//      readData3 = Char55util.dealwith55NewV2(readData3);
      tmpDataStatusEnum = checkReturn(readData3, address);
      if (!DataStatusEnum.isSuccess(tmpDataStatusEnum)) {
        i++;
        log.info("接收到数据有问题:" + tmpDataStatusEnum.getMeaning());
        continue;
      }
      return readData3;
    }
    return null;
  }

  public char[] read(InputStream in) throws IOException {
    byte[] readBuffer = new byte[200];
    char[] msgPack = null;
    int numBytes = 0;
    if (in == null) {
      return msgPack;
    }

    while (in.available() > 0) {
      numBytes = in.read(readBuffer);
      msgPack = new char[numBytes];
      for (int i = 0; i < numBytes; i++) {
        msgPack[i] = (char) (readBuffer[i] & 0xFF);
      }// end for
    }// end while
    return msgPack;
  }


  public void writePort(char[] bytes, Socket socket) throws IOException {
    //  根据地址得到对应的socket通道，然后修改in和out
    for (char b : bytes) {
      writePort(b, socket);
    }
  }

  /**
   * @describe: 向串口写数据 char bytes
   * @date:2009-11-5
   */
  public void writePort(char b, Socket socket) throws IOException {
    if (socket == null) {
      return;
    }
    OutputStream out = socket.getOutputStream();
    if (out == null) {
      return;
    }
    out.write(b);
    out.flush();
  }

  /**
   * @param rsByte 待检查的char数组
   * @param address 用来判断地址是否一致. 不需要时,赋值为 -1
   * @describe: 检查读取的值: address:-1(不检查头字节)
   * @return: 返回CRC检查结果. 只有返回 DataStatusEnum.right_frame ,为正常成功的帧,其他都为失败帧
   */
  public DataStatusEnum checkReturn(char[] rsByte, int address) {
    boolean rsOut = true;
    DataStatusEnum result = DataStatusEnum.right_frame;

    //帧判断
    if (rsByte == null) {
      //判断是否丢帧
      result = DataStatusEnum.lose_frame;
    } else {
      if (address != -1) {
        //检测地址头
        rsOut = rsByte[0] == address;
      }
      if (rsOut == false) {
        result = DataStatusEnum.wrong_address;
      } else {
        //对接收的数据进行crc校验，检查是否通讯故障
        rsOut = CalcCRC.checkCrc16(rsByte);
        if (rsOut == false) {
          result = DataStatusEnum.wrong_frame;
        }
      }
    }
    return result;
  }

  public void close() {
    IoUtil.close(socket);
  }

}