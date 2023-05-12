package edu.hitsz.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.hitsz.R;

public class RankingActivity extends AppCompatActivity {

    private static final String TAG = "InputActivity";

    public static int screenWidth;
    public static int screenHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ranking);
        Button newGame_btn = findViewById(R.id.newGame_btn);
        ListView rankingList = findViewById(R.id.rankingList);

        SimpleAdapter simpleAdapter = new SimpleAdapter(
                this,
                getData(),
                R.layout.listitem,
                new String[]{"rank", "name", "score", "date"},
                new int[]{R.id.rank, R.id.name, R.id.score, R.id.date});

        // 添加并显示数据
        rankingList.setAdapter(simpleAdapter);

        //添加单击监听
        rankingList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
//                if (position == 0) {
//                    return false;
//                }

//                Toast.makeText(RankingActivity.this, position + "  " + id, Toast.LENGTH_SHORT).show();

                AlertDialog.Builder builder = new AlertDialog.Builder(RankingActivity.this);
                builder.setMessage("确认删除？");
                builder.setTitle("提示");

//                Negative在左边？？
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(RankingActivity.this, "删除第" + (position + 1) + "条记录", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.create().show();
                return false;
            }
//            @Override
//            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//                Toast.makeText(RankingActivity.this, arg2 + "  " + arg3, Toast.LENGTH_SHORT).show();
////                Map<String, Object> clkmap = (Map<String, Object>) arg0.getItemAtPosition(arg2);
////                setTitle(clkmap.get("title").toString()+"的网址为："+clkmap.get("info").toString());
//            }


        });

        // 重新游戏
        Intent intent = new Intent(RankingActivity.this, MainActivity.class);
        newGame_btn.setOnClickListener(view -> {
            startActivity(intent);
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private List<HashMap<String, String>> getData() {
        ArrayList<HashMap<String, String>> data = new ArrayList<>();
        HashMap<String, String> user = new HashMap<>();

        // test

        user.put("rank", "1");
        user.put("name", "hhr");
        user.put("score", "2000");
        user.put("date", "05-12 21:53:40");
        data.add(user);

        user = new HashMap<>();
        user.put("rank", "2");
        user.put("name", "hhhhh");
        user.put("score", "500");
        user.put("date", "05-12 21:53:40");
        data.add(user);


        return data;
    }

}