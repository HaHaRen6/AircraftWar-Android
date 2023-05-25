package edu.hitsz.MultiPlayers;

public class Request {
    private int type = -1;
    //0代表请求对战，1代表传递分数
    private String name = "";
    private int groupID = -1;
    private int score = -1;

    public Request(int type, String name, int groupID, int score) {
        this.type = type;
        this.name = name;
        this.groupID = groupID;
        this.score = score;
    }
}
