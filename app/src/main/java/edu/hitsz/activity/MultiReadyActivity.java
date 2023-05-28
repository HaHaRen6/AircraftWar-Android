package edu.hitsz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import edu.hitsz.R;

public class MultiReadyActivity extends AppCompatActivity {

    private static final String TAG = "MultiReadyActivity";

    public static int screenWidth;
    public static int screenHeight;

    private String host;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 布局初始化时加载布局文件
        setContentView(R.layout.activity_multi_ready);

        Button connect_btn = findViewById(R.id.connect_button);
        TextView connectState_textView = findViewById(R.id.connect_state_textView);
        TextInputEditText connect_textInput = findViewById(R.id.connect_textInput);

        // 显示分数
//        textView_score.setText(String.valueOf(BaseGame.getScore()));

        connect_btn.setOnClickListener(view -> {
            host = String.valueOf(connect_textInput.getText());
            // 重新游戏
            Intent intent = new Intent(MultiReadyActivity.this, GameActivity.class);
            startActivity(intent);
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}