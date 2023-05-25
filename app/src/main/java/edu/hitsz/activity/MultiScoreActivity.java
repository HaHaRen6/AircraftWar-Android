package edu.hitsz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import edu.hitsz.R;
import edu.hitsz.game.BaseGame;

public class MultiScoreActivity extends AppCompatActivity {

    private static final String TAG = "MultiScoreActivity";

    public static int screenWidth;
    public static int screenHeight;

    private int opponentScore = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 布局初始化时加载布局文件
        setContentView(R.layout.activity_multi_score);

        Button newGame_btn = findViewById(R.id.newGameMulti_btn);
        TextView textView_score = findViewById(R.id.textView_score);
        TextView textView_score2 = findViewById(R.id.textView_score2);
        TextView textView_whoWin = findViewById(R.id.textView_whoWin);


        // 显示分数
        textView_score.setText(String.valueOf(BaseGame.getScore()));
        textView_score2.setText(String.valueOf(opponentScore));
        String whoWin;

        if (BaseGame.getScore() > opponentScore){
            whoWin = "你赢了";
        }
        else if (BaseGame.getScore() < opponentScore){
            whoWin = "你输了";
        }
        else {
            whoWin = "平局";
        }
        textView_whoWin.setText(whoWin);

        newGame_btn.setOnClickListener(view -> {
            // 重新游戏
            Intent intent = new Intent(MultiScoreActivity.this, MainActivity.class);
            startActivity(intent);
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}