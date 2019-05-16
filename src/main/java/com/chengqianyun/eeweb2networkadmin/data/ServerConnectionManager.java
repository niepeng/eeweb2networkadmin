package com.chengqianyun.eeweb2networkadmin.data;

import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceInfo;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceInfoMapper;
import com.chengqianyun.eeweb2networkadmin.biz.enums.DeviceConfigEnum;
import com.chengqianyun.eeweb2networkadmin.core.SpringHelper;
import com.chengqianyun.eeweb2networkadmin.core.utils.IoUtil;
import com.chengqianyun.eeweb2networkadmin.core.utils.SpringContextHolder;
import com.chengqianyun.eeweb2networkadmin.service.PolicyService;
import com.chengqianyun.eeweb2networkadmin.service.SendPhoneService;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
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
   * 连续读取数据失败多少次,认为是当前离线
   */
  public static final int FAIL_TIMES_RETURN = 5;

  /**
   * 获取数据周期:单位秒
   */
  public static int GET_DATA_CYCLE = 10;

  /**
   * socketClient time out
   */
  public static final int SOCKET_TIME_OUT = 20 * 1000;

  /**
   * 单例的ServerSocket
   */
  private static ServerSocket server;

  public static Map<String, DeviceInfo> snDeviceSocketMap = new ConcurrentHashMap<String, DeviceInfo>();

  /**
   * 根据传入参数设置监听端口，如果没有参数调用以下方法并使用默认值
   */
  @PostConstruct
  public void start() throws IOException {
    threadPoolTaskExecutor.execute(new Runnable() {
      @Override
      public void run() {
        try {
          // 启动监听端口，等待连接上来
          start(DEFAULT_PORT);
        } catch (Exception e) {
          log.error("threadPoolTaskExecutor.executeError", e);
        }
      }
    });

    // 启动发送报警信息线程
    new Thread(new Runnable() {
      @Override
      public void run() {
        SpringContextHolder.getBean(SendPhoneService.class).sendAlarm();
      }
    }).start();

    // 启动恢复报警信息线程
    new Thread(new Runnable() {
      @Override
      public void run() {
        SpringContextHolder.getBean(SendPhoneService.class).sendRecoverAlarm();
      }
    }).start();

    // 启动离线处理线程
    new Thread(new Runnable() {
      @Override
      public void run() {
        SpringContextHolder.getBean(PolicyService.class).executeOffline();
      }
    }).start();



//    try {
//      SpringContextHolder.getBean(SerialService.class).init(false, true);
//    } catch (Error e) {
//      log.error("threadPoolTaskExecutor.SerialServiceInitError", e);
//    }

  }

    public static void start(int port) throws IOException {
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
              socket.setSoTimeout(SOCKET_TIME_OUT);
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

  public static DeviceInfo addSnConnection(String sn, int address, ServerClientHandler serverClientHandler) {
//    log.info("addSnConnection start,sn={},address={}", sn, address);
    DeviceInfoMapper deviceInfoMapper = SpringHelper.getBean("deviceInfoMapper", DeviceInfoMapper.class);
    if (snDeviceSocketMap.containsKey(sn)) {
      log.warn("addSnConnection_snDeviceSocketMap already exist");
      close(snDeviceSocketMap.get(sn));
      snDeviceSocketMap.remove(sn);
    }

    int deviceType = DeviceConfigEnum.getById(sn.substring(0, 2)).getDeviceType();
    DeviceInfo fromDB = deviceInfoMapper.selectBySn(sn);
//    log.warn("addSnConnection_fromDB={}", fromDB);

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

  public static void updateDeviceInfo(String sn) {
    DeviceInfoMapper deviceInfoMapper = SpringHelper.getBean("deviceInfoMapper", DeviceInfoMapper.class);
    DeviceInfo deviceInfo = deviceInfoMapper.selectBySn(sn);
    if (deviceInfo != null && snDeviceSocketMap.containsKey(sn)) {
      DeviceInfo oldDeviceInfo = snDeviceSocketMap.get(sn);
      deviceInfo.setServerClientHandler(oldDeviceInfo.getServerClientHandler());
      oldDeviceInfo.setServerClientHandler(null);
      snDeviceSocketMap.put(sn, deviceInfo);
    }
  }

  public static void removeDeviceInfo(String sn) {
    if (!snDeviceSocketMap.containsKey(sn)) {
      return;
    }
    DeviceInfo oldDeviceInfo = snDeviceSocketMap.get(sn);
    if (oldDeviceInfo != null && oldDeviceInfo.getServerClientHandler() != null) {
      oldDeviceInfo.getServerClientHandler().close();
    }
    snDeviceSocketMap.remove(sn);
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


  public static ServerClientHandler getClient(String sn) {
    if(snDeviceSocketMap.containsKey(sn)) {
      return snDeviceSocketMap.get(sn).getServerClientHandler();
    }
    return null;
  }
}