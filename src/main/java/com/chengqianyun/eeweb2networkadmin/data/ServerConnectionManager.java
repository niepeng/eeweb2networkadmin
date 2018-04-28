package com.chengqianyun.eeweb2networkadmin.data;

import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceInfo;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceInfoMapper;
import com.chengqianyun.eeweb2networkadmin.biz.enums.DeviceConfigEnum;
import com.chengqianyun.eeweb2networkadmin.core.SpringHelper;
import com.chengqianyun.eeweb2networkadmin.core.utils.IoUtil;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public final class ServerConnectionManager {


  @Autowired
  private ThreadPoolTaskExecutor threadPoolTaskExecutor;

  public static int DEFAULT_PORT = 8234;

  /**
   * 连续读取数据失败多少次,认为是当前socket无效了
   */
  public static final int FAIL_TIMES_RETURN = 2;

  /**
   * 获取数据周期:单位秒
   */
  public static final int GET_DATA_CYCLE = 10;

  /**
   * 单例的ServerSocket
   */
  private static ServerSocket server;

  public static Map<String, DeviceInfo> snDeviceSocketMap = new HashMap<String, DeviceInfo>();

  /**
   * 根据传入参数设置监听端口，如果没有参数调用以下方法并使用默认值
   */
  @PostConstruct
  public void start() throws IOException {
    threadPoolTaskExecutor.execute(new Runnable() {
      @Override
      public void run() {
        try {
          start(DEFAULT_PORT);
        } catch (IOException e) {
          log.error("threadPoolTaskExecutor.executeError", e);
        }
      }
    });
  }

  /**
   * 这个方法不会被大量并发访问，不太需要考虑效率，直接进行方法同步就行了
   */
    public synchronized static void start(int port) throws IOException {
        if (server != null) {
            return;
        }
        try {
            /**
             * 通过构造函数创建ServerSocket, 如果端口合法且空闲，服务端就监听成功
             */
            server = new ServerSocket(port);
            log.error("服务器已启动，端口号：" + port);

            /**
             * 通过无线循环监听客户端连接, 如果没有客户端接入，将阻塞在accept操作上。
             */
            while (true) {
              Socket socket = server.accept();
              socket.setSoTimeout(5000);
              String ip = socket.getInetAddress().getHostAddress();
              int connectPort = socket.getPort();
              String ipAndPort = ip + ":" + connectPort;
              log.error("handle info, " + ipAndPort);
              /**
               * 当有新的客户端接入时，会执行下面的代码,然后创建一个新的线程处理这条Socket链路
               */
              serverHandle(socket);
            }
        } finally {
            //一些必要的清理工作  
            if (server != null) {
                log.error("服务器已关闭。");
                server.close();
                server = null;
            }
        }
    }

  public static void stopServer() {
    log.error("准备关闭serverSocket服务...");
    if (server == null) {
      return;
    }
    try {
      server.close();
    } catch (Exception e) {
    }
    server = null;
    log.error("已关闭serverSocket服务...");
  }

  public static boolean restart(int port) {
    stopServer();
    try {
      start(port);
    } catch (IOException e) {
      return false;
    }
    return true;
  }

  public static synchronized DeviceInfo addSnConnection(String sn, int address, ServerClientHandler serverClientHandler) {
    DeviceInfoMapper deviceInfoMapper = SpringHelper.getBean("deviceInfoMapper", DeviceInfoMapper.class);
    if (snDeviceSocketMap.containsKey(sn)) {
      close(snDeviceSocketMap.get(sn));
      snDeviceSocketMap.remove(sn);
    }

    int deviceType = DeviceConfigEnum.getById(sn.substring(0, 2)).getDeviceType();
    DeviceInfo fromDB = deviceInfoMapper.selectBySn(sn);

    if (fromDB == null) {
      fromDB = new DeviceInfo();
      fromDB.setSn(sn);
      fromDB.setAddress(address);
      fromDB.setType(deviceType);
      deviceInfoMapper.insert(fromDB);
      fromDB = deviceInfoMapper.selectBySn(sn);
      fromDB.setServerClientHandler(serverClientHandler);
      snDeviceSocketMap.put(sn, fromDB);
      return fromDB;
    }

    if (fromDB.getType() == deviceType && fromDB.getAddress() == address) {
      fromDB.setServerClientHandler(serverClientHandler);
      snDeviceSocketMap.put(sn, fromDB);
      return fromDB;
    }

    fromDB.setType(deviceType);
    fromDB.setAddress(address);
    deviceInfoMapper.updateByPrimaryKey(fromDB);

    fromDB.setServerClientHandler(serverClientHandler);
    snDeviceSocketMap.put(sn, fromDB);
    return fromDB;
  }

  private static void close(DeviceInfo deviceInfo) {
    if (deviceInfo == null) {
      return;
    }

    if (deviceInfo.getServerClientHandler() == null) {
      return;
    }

    IoUtil.close(deviceInfo.getServerClientHandler().getSocket());
  }

  private static void serverHandle(Socket socket) {
    Thread  t = new Thread(new ServerClientHandler(socket));
    t.start();
  }




















}  