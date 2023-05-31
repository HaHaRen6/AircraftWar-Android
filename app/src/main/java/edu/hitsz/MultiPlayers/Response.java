package edu.hitsz.MultiPlayers;

import java.io.Serializable;

public class Response implements Serializable {

    /**
     * 3 传递种子
     * 4 传递分数
     * 5 游戏结束
     */
    private int type = -1;
    private int opponentScore = 0;
    private Long seed = 1L;

    public int getType() {
        return type;
    }

    public int getOpponentScore(){
        return opponentScore;
    }

    public Long getSeed() {
        return seed;
    }

    public Response(int type, Long seed) {
        this.type = type;
        this.seed = seed;
    }
}
