package edu.hitsz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

import edu.hitsz.MultiPlayers.ClientThread;
import edu.hitsz.MultiPlayers.Request;
import edu.hitsz.MultiPlayers.Response;
import edu.hitsz.R;
import edu.hitsz.game.BaseGame;
import edu.hitsz.game.MediumGame;

public class MultiActivity extends AppCompatActivity {

    private static final String TAG = "MultiReadyActivity";

    public static int screenWidth;
    public static int screenHeight;

    private String host;
    private int port;
    private Socket socket = null;
    private BufferedReader in = null;
    private PrintWriter out = null;
    private String content = "";
    private StringBuilder stringBuilder = null;
    private Handler handler;
    private ClientThread clientThread;
    private Long seed;
    private BaseGame baseGameView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 布局初始化时加载布局文件
        setContentView(R.layout.activity_multi_ready);

        stringBuilder = new StringBuilder();

        Button connect_btn = findViewById(R.id.connect_button);
        TextView connectState_textView = findViewById(R.id.connect_state_textView);
        TextInputEditText connect_textInput = findViewById(R.id.connect_textInput);
        TextInputEditText connectPort_textInput = findViewById(R.id.connectPort_textInput);

        //用于发送接收到的服务端的消息，显示在界面上
        handler = new Handler(getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                // 处理来自于子线程的消息
                if (msg.what == 1) {
                    // 游戏结束
                    Toast.makeText(MultiActivity.this, "GameOver", Toast.LENGTH_SHORT).show();
                    // 发送游戏结束消息
                    Message servermsg = new Message();
                    servermsg.what = 1234;
                    servermsg.obj = new Request(2, BaseGame.getScore());
                    clientThread.toserverHandler.sendMessage(servermsg);

                } else if (msg.what == 2) {
                    // 产生Boss
                    Toast.makeText(MultiActivity.this, "Boss来咯", Toast.LENGTH_SHORT).show();

                } else if (msg.what == 100) {
                    // 收到信息表示开始游戏
                    Response response = (Response) msg.obj;
                    if (response.getType() == 3) {
                        // 设置种子并开始游戏
                        if (baseGameView == null) {
                            seed = response.getSeed();

                            connectState_textView.setText("匹配成功");
                            Toast.makeText(MultiActivity.this, "2秒后开始游戏", Toast.LENGTH_SHORT).show();
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }

                            // 开始游戏
                            baseGameView = new MediumGame(MultiActivity.this, handler);
                            baseGameView.setSeed(seed);
                            setContentView(baseGameView);
                        }

                    } else if (response.getType() == 4) {
                        // 获取对方分数
                        baseGameView.setOpponentScore(response.getOpponentScore());

                    } else if (response.getType() == 5) {
                        // 游戏结束
                        Intent intent = new Intent(MultiActivity.this, MultiScoreActivity.class);
                        startActivity(intent);

                    }

                } else if (msg.what == 111) {
                    // 连接成功，等待对手
                    connectState_textView.setText("连接成功，等待对手");
                    System.out.println("连接成功，等待对手");
                }
            }
        };

        connect_btn.setOnClickListener(view -> {
            connectState_textView.setText("连接中");
            host = connect_textInput.getText().toString();
            port = Integer.parseInt(connectPort_textInput.getText().toString());
            // 创建连接
            clientThread = new ClientThread(host, port, handler);
            new Thread(clientThread).start();

            new Thread(() -> {
                try {
                    while (baseGameView == null || !baseGameView.gameOverFlag) {
                        Thread.sleep(1000);
                        Request request = new Request(1,BaseGame.getScore());
                        Message msg = new Message();
                        msg.what = 456;
                        msg.obj = request;
                        clientThread.toserverHandler.sendMessage(msg);
                    }
                    Request request = new Request(2,BaseGame.getScore());
                    Message msg = new Message();
                    msg.what = 456;
                    msg.obj = request;
                    clientThread.toserverHandler.sendMessage(msg);

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}