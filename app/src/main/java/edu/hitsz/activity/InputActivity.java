package edu.hitsz.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import edu.hitsz.R;

public class InputActivity extends AppCompatActivity {

    private static final String TAG = "InputActivity";

    public static int screenWidth;
    public static int screenHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_input);
        Button inputName_btn = findViewById(R.id.inputName_btn);
        TextView textView = findViewById(R.id.textView);
        TextInputEditText textInputEditText = findViewById(R.id.textInputEditText);

        Bundle bundle = new Bundle();

//        Intent intent = new Intent(InputActivity.this, GameActivity.class);
        inputName_btn.setOnClickListener(view -> {
            Toast.makeText(InputActivity.this, textInputEditText.getText(), Toast.LENGTH_SHORT).show();
//            bundle.putInt("gameType",gameType);
//            bundle.putBoolean("musicSwitch",musicSwitch());
//            intent.putExtras(bundle);
//            startActivity(intent);
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}