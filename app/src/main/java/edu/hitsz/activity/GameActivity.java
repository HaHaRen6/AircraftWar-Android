package edu.hitsz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import edu.hitsz.game.BaseGame;
import edu.hitsz.game.EasyGame;
import edu.hitsz.game.HardGame;
import edu.hitsz.game.MediumGame;

public class GameActivity extends AppCompatActivity {
    private static final String TAG = "GameActivity";

    private int gameType = 0;
    private boolean musicSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
          Android系统输出信息
         */
        Handler handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Log.d(TAG, "handleMessage");
                if (msg.what == 1) {
                    Toast.makeText(GameActivity.this, "GameOver", Toast.LENGTH_SHORT).show();
                } else if (msg.what == 2) {
                    Toast.makeText(GameActivity.this, "Boss来咯", Toast.LENGTH_SHORT).show();
                }
            }
        };

        /*
          接受intent传递的信息
         */
        if (getIntent() != null) {
            Bundle bundle = getIntent().getExtras();
            gameType = bundle.getInt("gameType", 1);
            musicSwitch = bundle.getBoolean("musicSwitch", false);
        }

        /*
          开始不同难度的游戏
         */
        BaseGame baseGameView;
        if (gameType == 1) {
            baseGameView = new MediumGame(this, handler, musicSwitch);
        } else if (gameType == 3) {
            baseGameView = new HardGame(this, handler, musicSwitch);
        } else {
            baseGameView = new EasyGame(this, handler, musicSwitch);
        }
        setContentView(baseGameView);

        /*
          等待游戏结束
          每隔 1s 检测一次游戏结束flag
         */
        new Thread(() -> {
            try {
                while (!baseGameView.gameOverFlag) {
                    Thread.sleep(1000);
                }

                // 切换至输入姓名界面
                Intent intent = new Intent(GameActivity.this, InputActivity.class);
                startActivity(intent);

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}