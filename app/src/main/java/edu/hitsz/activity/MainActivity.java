package edu.hitsz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;

import edu.hitsz.R;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    public static int screenWidth;
    public static int screenHeight;

    private int gameType=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button medium_btn = findViewById(R.id.medium_btn);
        Button easy_btn = findViewById(R.id.easy_btn);
        Button hard_btn = findViewById(R.id.hard_btn);
        Bundle bundle = new Bundle();

        getScreenHW();

        /*
          不同按钮开始不同难度游戏
         */
        Intent intent = new Intent(MainActivity.this, GameActivity.class);
        // 普通难度
        medium_btn.setOnClickListener(view -> {
            gameType=1;
            bundle.putInt("gameType",gameType);
            bundle.putBoolean("musicSwitch",musicSwitch());
            intent.putExtras(bundle);
            startActivity(intent);
        });
        // 简单难度
        easy_btn.setOnClickListener(view -> {
            gameType =2;
            bundle.putInt("gameType",gameType);
            bundle.putBoolean("musicSwitch",musicSwitch());
            intent.putExtras(bundle);
            startActivity(intent);
        });
        // 困难难度
        hard_btn.setOnClickListener(view -> {
            gameType =3;
            bundle.putInt("gameType",gameType);
            bundle.putBoolean("musicSwitch",musicSwitch());
            intent.putExtras(bundle);
            startActivity(intent);
        });
    }

    public void getScreenHW(){
        //定义DisplayMetrics 对象
        DisplayMetrics dm = new DisplayMetrics();
        //取得窗口属性
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        //窗口的宽度
        screenWidth= dm.widthPixels;
        //窗口高度
        screenHeight = dm.heightPixels;

        Log.i(TAG, "screenWidth : " + screenWidth + " screenHeight : " + screenHeight);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public boolean musicSwitch(){
        CheckBox music_swc = findViewById(R.id.musicSwitch);
        return music_swc.isChecked();
    }
}