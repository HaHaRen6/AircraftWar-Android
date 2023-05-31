package edu.hitsz.MultiPlayers;

import java.io.Serializable;

public class Request implements Serializable {
//    private static final long serialVersionUID = -3867863323116998246L;

    /**
     * Request类型
     * 1 传递分数
     * 2 一边结束
     */
    private int type = -1;
    private int score = -1;

    public int getType() {
        return type;
    }

    public int getScore() {
        return score;
    }

    public Request(int type, int score) {
        this.type = type;
        this.score = score;
    }
}
