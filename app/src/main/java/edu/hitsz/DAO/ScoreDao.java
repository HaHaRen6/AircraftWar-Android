package edu.hitsz.DAO;

import android.content.Context;

/**
 * 【数据访问对象模式】DAO接口
 *
 * @author hhr
 */
public interface ScoreDao {
    /**
     * 往txt文件内写入一条记录
     *
     * @param scoreInfo 数据对象
     */
    void addItem(Context context, ScoreInfo scoreInfo, String scoreFile);

    /**
     * 从txt文件获取对象
     */
    void getAllItems(Context context, String scoreFile);

    /**
     * 排序分数
     */
    void sortByScore();

    /**
     * 获取信息
     *
     * @return Items: String[][]
     */
    String[][] outPutItems();

    void deleteByTime(Context context, String[][] str, String scoreFile);

}
