package com.chengqianyun.eeweb2networkadmin.data;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class ServerNormal {

  public static int DEFAULT_PORT = 8234;

  /**
   * 连续读取数据失败多少次,认为是当前socket无效了
   */
  public static final int FAIL_TIMES_RETURN = 2;

  /**
   * 获取数据周期:单位秒
   */
//  public static final int GET_DATA_CYCLE = 20;
  public static final int GET_DATA_CYCLE = 10;


  /**
   * 单例的ServerSocket
   */
  private static ServerSocket server;

  /**
   * 根据传入参数设置监听端口，如果没有参数调用以下方法并使用默认值
   */
  public static void start() throws IOException {
        start(DEFAULT_PORT);
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
            log.info("服务器已启动，端口号：" + port);

            /**
             * 通过无线循环监听客户端连接, 如果没有客户端接入，将阻塞在accept操作上。
             */
            while (true) {
              Socket socket = server.accept();
              socket.setSoTimeout(5000);
              String ip = socket.getInetAddress().getHostAddress();
              int connectPort = socket.getPort();
              String ipAndPort = ip + ":" + connectPort;
              log.info("handle info, " + ipAndPort);
              /**
               * 当有新的客户端接入时，会执行下面的代码,然后创建一个新的线程处理这条Socket链路
               */
              serverHandle(socket);
            }
        } finally {
            //一些必要的清理工作  
            if (server != null) {
                System.out.println("服务器已关闭。");
                server.close();
                server = null;
            }
        }
    }

    private static void serverHandle(Socket socket) {
        new Thread(new ServerHandler(socket)).start();
    }
}  