package edu.hitsz.MultiPlayers;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class ClientThread implements Runnable {
//    cd "/home/haharen/school/OOP/" && javac MyServer.java && java MyServer

    private final String host;
    //    private String HOST = "10.0.2.2";
    private final int port;
    private Socket socket = null;
    private Handler toclientHandler;     // 向UI线程发送消息的Handler对象
    public Handler toserverHandler;  // 接收UI线程消息的Handler对象
    private BufferedReader in = null;
    private PrintWriter out = null;
    private ObjectInputStream objectIn = null;
    private ObjectOutputStream objectOut = null;

    public ClientThread(String host, int port, Handler myhandler) {
        this.toclientHandler = myhandler;
        this.host = host;
        this.port = port;
    }

    public void run() {
        try {
            int timeout = 5000;
            socket = new Socket();  //建立连接到远程服务器的Socket
            socket.connect(new InetSocketAddress(host, port), timeout);

            if (socket.isConnected()) {

                //初始化输入输出流
//                in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
//                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
//                        socket.getOutputStream(), StandardCharsets.UTF_8)), true);
                objectIn = new ObjectInputStream(socket.getInputStream());
                objectOut = new ObjectOutputStream(socket.getOutputStream());

                // 开始游戏
                Message msg = new Message();
                msg.what = 0x111;
                msg.obj = "ready";
                toclientHandler.sendMessage(msg);

                //创建子线程，
                new Thread(() -> {
                    Response response = null;
                    try {
//                        while ((fromserver = in.readLine()) != null) {
                        while ((response = (Response) objectIn.readObject()) != null) {
                            // 将server信息发送到主线程
                            Message servermsg = new Message();
                            servermsg.what = 100;
                            servermsg.obj = response;
                            toclientHandler.sendMessage(servermsg);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }).start();

                Looper.prepare();  //在子线程中初始化一个Looper对象，即为当前线程创建消息队列
                toserverHandler = new Handler(Looper.myLooper()) {  //实例化Handler对象
                    @Override
                    public void handleMessage(Message msg) {
                        if (msg.what == 456) {
                            // 输出到服务器
                            try {
                                out.println(msg.obj);  //将输出流包装为打印流
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else if (msg.what == 1234) {
                            // 从服务器获取信息
                            // TODO 这里干啥

                        }
                    }
                };
                Looper.loop();  //启动Looper，运行刚才初始化的Looper对象，循环取消息队列的消息
            }
            // TODO 没连上咋办
        } catch (SocketTimeoutException el) {
            System.out.println("网络连接超时！");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
