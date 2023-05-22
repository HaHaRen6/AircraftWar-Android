package edu.hitsz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Date;

import edu.hitsz.DAO.ScoreDao;
import edu.hitsz.DAO.ScoreDaoImpl;
import edu.hitsz.DAO.ScoreInfo;
import edu.hitsz.R;
import edu.hitsz.game.BaseGame;

public class InputActivity extends AppCompatActivity {

    private static final String TAG = "InputActivity";

    public static int screenWidth;
    public static int screenHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 布局初始化时加载布局文件
        setContentView(R.layout.activity_input);

        Button inputName_btn = findViewById(R.id.inputName_btn);
        TextView textView_score = findViewById(R.id.textView_score);
        TextInputEditText textInputEditText = findViewById(R.id.textInputEditText);

        // 显示分数
        textView_score.setText(String.valueOf(BaseGame.getScore()));

        // 输入名字按钮监听器
        inputName_btn.setOnClickListener(view -> {

            // 以设定的格式获取当前时间
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd HH:mm:ss");

            ScoreInfo scoreInfo = new ScoreInfo();
            scoreInfo.setScore(BaseGame.getScore());
            scoreInfo.setDate(dateFormat.format(date));
            scoreInfo.setName(String.valueOf(textInputEditText.getText()));

            ScoreDao scoreDao = new ScoreDaoImpl();
            scoreDao.addItem(InputActivity.this, scoreInfo, BaseGame.scoreFile);

            Intent intent = new Intent(InputActivity.this, RankingActivity.class);
            startActivity(intent);
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}