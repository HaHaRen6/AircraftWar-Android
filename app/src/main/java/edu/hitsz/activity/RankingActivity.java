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

import edu.hitsz.DAO.ScoreDao;
import edu.hitsz.DAO.ScoreDaoImpl;
import edu.hitsz.R;
import edu.hitsz.game.BaseGame;

public class RankingActivity extends AppCompatActivity {

    private static final String TAG = "InputActivity";

    public static int screenWidth;
    public static int screenHeight;
    private ListView rankingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ranking);
        Button newGame_btn = findViewById(R.id.newGame_btn);
        rankingList = findViewById(R.id.rankingList);

        flushAdapter();

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
                        ScoreDao scoreDao = new ScoreDaoImpl();
                        scoreDao.getAllItems(RankingActivity.this, BaseGame.scoreFile);
                        scoreDao.sortByScore();
                        String[][] items = scoreDao.outPutItems();
//                        for (String[] item : items) {
//                            for (String element : item) {
//                                System.out.print(element + ' ');
//                            }
//                            System.out.println();
//                        }
                        items[position][3] = "delete";
//                        for (String[] item : items) {
//                            for (String element : item) {
//                                System.out.print(element + ' ');
//                            }
//                            System.out.println();
//                        }
                        scoreDao.deleteByTime(RankingActivity.this, items, BaseGame.scoreFile);
//                        for (String[] item : items) {
//                            for (String element : item) {
//                                System.out.print(element + ' ');
//                            }
//                            System.out.println();
//                        }
                        flushAdapter();
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
        HashMap<String, String> user;
        ScoreDao scoreDao = new ScoreDaoImpl();
        scoreDao.getAllItems(RankingActivity.this, BaseGame.scoreFile);
        scoreDao.sortByScore();
        String[][] items = scoreDao.outPutItems();

        for (int i = 0; i < items.length - 1; i++) {
            String[] item = items[i];
            user = new HashMap<>();
            user.put("rank", item[0]);
            user.put("score", item[1]);
            user.put("name", item[2]);
            user.put("date", item[3]);
            data.add(user);
        }


        return data;
    }

    private void flushAdapter() {

        // 初始化适配器
        SimpleAdapter simpleAdapter = new SimpleAdapter(
                this,
                getData(),
                R.layout.listitem,
                new String[]{"rank", "name", "score", "date"},
                new int[]{R.id.rank, R.id.name, R.id.score, R.id.date}
        );

        // 添加并显示数据
        rankingList.setAdapter(simpleAdapter);
    }
}