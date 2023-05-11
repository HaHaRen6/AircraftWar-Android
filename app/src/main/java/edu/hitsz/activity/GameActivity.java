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
        if (getIntent() != null) {
            Bundle bundle = getIntent().getExtras();
            gameType = bundle.getInt("gameType", 1);
            musicSwitch = bundle.getBoolean("musicSwitch", false);
        }
        BaseGame baseGameView;
        if (gameType == 1) {
            baseGameView = new MediumGame(this, handler, musicSwitch);
        } else if (gameType == 3) {
            baseGameView = new HardGame(this, handler, musicSwitch);
        } else {
            baseGameView = new EasyGame(this, handler, musicSwitch);
        }
        setContentView(baseGameView);

        Runnable r = () -> {

            try {
                while (!baseGameView.gameOverFlag) {
                    Thread.sleep(1000);
//                    System.out.println("123456");
                }
//                System.out.println("654321");
                Intent intent = new Intent(GameActivity.this, InputActivity.class);
//                System.out.println("1111");
                startActivity(intent);
//                System.out.println("2222");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };
        new Thread(r,"t1").start();

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}