package edu.hitsz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

import edu.hitsz.MultiPlayers.ClientThread;
import edu.hitsz.R;

public class MultiReadyActivity extends AppCompatActivity {

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

//        stringBuilder.append();


        // 显示分数
//        textView_score.setText(String.valueOf(BaseGame.getScore()));

        //用于发送接收到的服务端的消息，显示在界面上
        handler = new Handler(getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                // 处理来自于子线程的消息
                if (msg.what == 0x123456) {
                    // 接收种子
                    seed = (Long) msg.obj;
                    System.out.println("set seed: " + seed);
                }
                else if (msg.what == 0x111) {
                    // 连接成功
                    connectState_textView.setText("连接成功");
                    System.out.println("连接成功");
                }
            }
        };

        connect_btn.setOnClickListener(view -> {
            // 创建连接
            connectState_textView.setText("连接中");
            host = connect_textInput.getText().toString();
            port = Integer.parseInt(connectPort_textInput.getText().toString());
            clientThread = new ClientThread(host, port, handler);
            new Thread(clientThread).start();

            // 发送信息
            Message msg = new Message();
            msg.what = 0x456;
//            msg.obj = new Request(1, seed, 100);
            clientThread.toserverHandler.sendMessage(msg);

            // 开始游戏
            Intent intent = new Intent(MultiReadyActivity.this, GameActivity.class);
            intent.putExtra("seed", seed);
            startActivity(intent);
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}