package Gobang.Entity;

public class Player {
    String username;
    int blackOrWhite;
    int flag;  //0 匹配ing  1 游戏ing 2 游戏over  3 逃跑
    public int tag=-1; //桌号    如果还没匹配成功时候（匹配超时） ，设置为-1；
    public Player(String username){
        this.username=username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getBlackOrWhite() {
        return blackOrWhite;
    }

    public void setBlackOrWhite(int blackOrWhite) {
        this.blackOrWhite = blackOrWhite;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
