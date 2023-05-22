package edu.hitsz.DAO;

import java.util.Objects;

/**
 * 【数据访问对象模式】数据对象实体类
 *
 * @author hhr
 */
public class ScoreInfo {
    private int score;
    private String name;
    private String date;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (Objects.equals(name, "")) {
            this.name = "player";
        } else {
            this.name = name;
        }
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
