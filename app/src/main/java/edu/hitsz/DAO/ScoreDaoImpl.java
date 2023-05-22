package edu.hitsz.DAO;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 【数据访问对象模式】DAO实现类
 *
 * @author hhr
 */
public class ScoreDaoImpl implements ScoreDao {
    private List<ScoreInfo> scoreInfos;

    public ScoreDaoImpl() {
        scoreInfos = new ArrayList<>();
    }

    public void addItem(Context context, ScoreInfo scoreInfo, String scoreFile) {
        FileOutputStream fOut = null;
        OutputStreamWriter writer = null;

        try {
            // 构建FileOutputStream对象,文件不存在会自动新建
            fOut = context.openFileOutput(scoreFile, Context.MODE_APPEND);

            // 构建OutputStreamWriter对象,参数可以指定编码,默认为操作系统默认编码
            writer = new OutputStreamWriter(fOut, StandardCharsets.UTF_8);

            // 写入到缓冲区
            writer.append(String.valueOf(scoreInfo.getScore())).append("\t");
            writer.append(scoreInfo.getName()).append("\t");
            writer.append(scoreInfo.getDate()).append("\n");

            // 刷新缓冲区（不知道有没有用）
            writer.flush();
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            try {
                //关闭写入流,同时会把缓冲区内容写入文件
                if (writer != null) {
                    writer.close();
                }

                //关闭输出流,释放系统资源
                if (fOut != null) {
                    fOut.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void getAllItems(Context context, String scoreFile) {
        BufferedReader reader = null;
        FileInputStream fIn = null;
        InputStreamReader isr = null;
        scoreInfos = new ArrayList<>();

        try {
            fIn = context.openFileInput(scoreFile);
            isr = new InputStreamReader(fIn, StandardCharsets.UTF_8);
            reader = new BufferedReader(isr);

            String line = "";
            while ((line = reader.readLine()) != null) {
                // 按 \t 分割，存在parts里
                String[] parts = line.split("\t");

                // 写入数据对象
                ScoreInfo temp = new ScoreInfo();
                temp.setScore(Integer.parseInt(parts[0]));
                temp.setName(parts[1]);
                temp.setDate(parts[2]);
                scoreInfos.add(temp);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (isr != null) {
                    isr.close();
                }
                if (fIn != null) {
                    fIn.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public void sortByScore() {
        scoreInfos.sort((b, a) -> a.getScore() - b.getScore());
    }

    public String[][] outPutItems() {
        int cnt = 0;
        String[][] str = new String[scoreInfos.size() + 1][4];
        for (ScoreInfo s : scoreInfos) {
            str[cnt][0] = String.valueOf(cnt + 1);
            str[cnt][1] = String.valueOf(s.getScore());
            str[cnt][2] = s.getName();
            str[cnt][3] = s.getDate();
            cnt++;
        }
        return str;
    }

    public void deleteByTime(Context context, String[][] str, String scoreFile) {
        FileOutputStream fOut = null;
        OutputStreamWriter writer = null;

        try {
            // 构建FileOutputStream对象,文件不存在会自动新建
            fOut = context.openFileOutput(scoreFile, Context.MODE_PRIVATE);

            // 构建OutputStreamWriter对象,参数可以指定编码,默认为操作系统默认编码
            writer = new OutputStreamWriter(fOut, StandardCharsets.UTF_8);

            for (int i = 0; i < str.length - 1; i++) {
                if (!Objects.equals(str[i][3], "delete")) {
                    for (int j = 1; j < str[i].length; j++) {
                        writer.append(str[i][j]).append("\t");
                    }
                    writer.append("\n");
                }
            }

            // 刷新缓冲区（不知道有没有用）
            writer.flush();
            fOut.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                //关闭写入流,同时会把缓冲区内容写入文件
                assert writer != null;
                writer.close();

                //关闭输出流,释放系统资源
                assert fOut != null;
                fOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
