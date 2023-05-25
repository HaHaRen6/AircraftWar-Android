package edu.hitsz.MultiPlayers;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class ClientThread implements Runnable {
//    cd "/home/haharen/school/OOP/" && javac MyServer.java && java MyServer

    private static final String HOST = "112.74.39.176";
//    private static final String HOST = "10.0.2.2";
    private static final int PORT = 8899;
    private Socket socket = null;
    private Handler toclientHandler;     // 向UI线程发送消息的Handler对象
    public Handler toserverHandler;  // 接收UI线程消息的Handler对象
    private BufferedReader in = null;
    private PrintWriter out = null;

    public ClientThread(Handler myhandler) {
        this.toclientHandler = myhandler;
    }

    public void run() {
        try {
            socket = new Socket(HOST, PORT);  //建立连接到远程服务器的Socket
            //初始化输入输出流
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                    socket.getOutputStream(), "UTF-8")), true);
            Log.i("wotainanl", "in" + in + "@@" + out);
            //创建子线程，
            new Thread(() -> {
                String fromserver = null;
                try {
                    while ((fromserver = in.readLine()) != null) {
                        Message servermsg = new Message();
                        servermsg.what = 0x123;
                        servermsg.obj = fromserver;
                        toclientHandler.sendMessage(servermsg);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            Looper.prepare();  //在子线程中初始化一个Looper对象，即为当前线程创建消息队列
            toserverHandler = new Handler(Looper.myLooper()) {  //实例化Handler对象
                @Override
                public void handleMessage(Message msg) {
                    if (msg.what == 0x456) {
                       try {
                            out.println(msg.obj);  //将输出流包装为打印流
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            Looper.loop();  //启动Looper，运行刚才初始化的Looper对象，循环取消息队列的消息
        } catch (SocketTimeoutException el) {
            System.out.println("网络连接超时！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
