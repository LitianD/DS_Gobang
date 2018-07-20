package com.bjtu.gobang.gobang.Enities;

import org.springframework.stereotype.Component;

@Component
public class GobangMap {

    int currentColor;
    public Vex[][] map=new Vex[15][15];
    int overFlag;//0是匹配，1是游戏，2是结束
    int tag;
    int lastPlayer=0;

    public int getLastPlayer() {
        return lastPlayer;
    }

    public void setLastPlayer(int lastPlayer) {
        this.lastPlayer = lastPlayer;
    }

    public Player getP1() {
        return p1;
    }

    public int getOverFlag() {
        return overFlag;
    }

    public void setOverFlag(int overFlag) {
        this.overFlag = overFlag;
    }

    public void setP1(Player p1) {
        this.p1 = p1;
    }

    public Player getP2() {
        return p2;
    }

    public void setP2(Player p2) {
        this.p2 = p2;
    }

    Player p1;
    Player p2;

    public GobangMap(){
        for(int i=0;i<15;i++)
        {
            for(int j=0;j<15;j++)
            {
                map[i][j]=new Vex(-1, -1, -1, -1);
            }
        }
        overFlag=0;
    }


    public int getCurrentColor() {
        return currentColor;
    }

    public void setCurrentColor(int currentColor) {
        this.currentColor = currentColor;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    // 根据数组，判断棋形
    public boolean judge(int x1,int y1) {
        boolean res=false;
        // 判断x方向上是否连成五子
        if (true) {
            int X1 = x1;
            int Y1 = y1;

            while (X1 >= 0 && X1 <= 15 && Y1 >= 0 && Y1 <= 15 && map[X1][Y1].whiteOrBlack == map[x1][y1].whiteOrBlack) {
                Y1--;
            }

            int i = 0;
            Y1++;

            while (X1 >= 0 && X1 < 15 && Y1 >= 0 && Y1 < 15 && map[X1][Y1].whiteOrBlack == map[x1][y1].whiteOrBlack) {
                i++;
                if (i == 5) {
                    return true;
                }
                Y1++;
            }
        }

        // 判断Y方向上是否连成五子
        if (true) {
            int X1 = x1;
            int Y1 = y1;
            while (X1 >= 0 && X1 < 15 && Y1 >= 0 && Y1 < 15 && map[X1][Y1].whiteOrBlack == map[x1][y1].whiteOrBlack) {
                X1--;
            }
            X1++;
            System.out.println(X1);
            System.out.println(Y1);
            int i = 0;
            while (X1 >= 0 && X1 < 15 && Y1 >= 0 && Y1 < 15 && map[X1][Y1].whiteOrBlack == map[x1][y1].whiteOrBlack) {
                i++;
                if (i == 5) {
                    return true;
                }

                X1++;
            }

        }

        // 判断右斜方向上是否连成五子
        if (true) {
            int X1 = x1;
            int Y1 = y1;
            while (X1 >= 0 && X1 < 15 && Y1 >= 0 && Y1 < 15 && map[X1][Y1].whiteOrBlack == map[x1][y1].whiteOrBlack) {
                X1--;
                Y1--;
            }
            X1++;
            Y1++;
            int i = 0;
            while (X1 >= 0 && X1 < 15 && Y1 >= 0 && Y1 < 15 && map[X1][Y1].whiteOrBlack == map[x1][y1].whiteOrBlack) {
                i++;
                if (i == 5) {
                    return true;
                }
                X1++;
                Y1++;
            }

        }

        // 判断左斜方向上是否连成五子
        if (true) {
            int X1 = x1;
            int Y1 = y1;
            while (X1 >= 0 && X1 < 15 && Y1 >= 0 && Y1 < 15 && map[X1][Y1].whiteOrBlack == map[x1][y1].whiteOrBlack) {
                X1++;
                Y1--;
            }

            X1--;
            Y1++;

            System.out.println(X1);
            System.out.println(Y1);
            int i = 0;
            while (X1 >= 0 && X1 < 15 && Y1 >= 0 && Y1 < 15 && map[X1][Y1].whiteOrBlack == map[x1][y1].whiteOrBlack) {
                i++;
                if (i == 5) {
                    return true;
                }
                X1--;
                Y1++;
            }

        }

        return false;
    }

}
