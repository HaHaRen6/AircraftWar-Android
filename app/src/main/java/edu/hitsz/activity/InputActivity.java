package edu.hitsz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

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

        Bundle bundle = new Bundle();
        System.out.println(BaseGame.getScore());
        textView_score.setText(String.valueOf(BaseGame.getScore()));

        Intent intent = new Intent(InputActivity.this, RankingActivity.class);
        inputName_btn.setOnClickListener(view -> {

            intent.putExtra("name",textInputEditText.getText());
//            Toast.makeText(InputActivity.this, "后面还没做", Toast.LENGTH_SHORT).show();
//            bundle.putInt("gameType",gameType);
//            bundle.putBoolean("musicSwitch",musicSwitch());
//            intent.putExtras(bundle);
            startActivity(intent);
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}