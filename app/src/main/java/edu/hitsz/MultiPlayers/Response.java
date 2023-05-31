package edu.hitsz.MultiPlayers;

import java.io.Serializable;

public class Response implements Serializable {

    /*
        3 传递种子
        4 游戏结束
     */
    private int type = -1;
    private Long seed = 1L;

    public Response(int type, Long seed) {
        this.type = type;
        this.seed = seed;
    }
}
