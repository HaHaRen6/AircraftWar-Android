package edu.hitsz.MultiPlayers;

import java.io.Serializable;

public class Request implements Serializable {

    /*
        0 请求对战
        1 传递分数
        2 一边结束
     */
    private int type = -1;
    private int score = -1;

    public Request(int type, int score) {
        this.type = type;
        this.score = score;
    }
}
