package edu.hitsz.MultiPlayers;

public class Request {
    private int type = -1;
    // 0代表请求对战，1代表传递分数
    private int score = -1;
    private Long seed;

    public Request(int type, Long seed ,int score) {
        this.type = type;
        this.seed = seed;
        this.score = score;
    }
}
