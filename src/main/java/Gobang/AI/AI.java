package Gobang.AI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.*;
import java.io.*;
import java.util.regex.*;

public class AI {
    private int[][] pieces;
    private int player;//player = 1 表示AI为黑子player = 2表示AI为白子
    List<String> pieceShape1 = new ArrayList();
    List<String> pieceShape2 = new ArrayList();
    int [][]temp = new int [15][15];
    //int X,Y;
    HashMap<String, Integer> pieceValue = new HashMap<String, Integer>() {{
        put("长连", 100000);
        put("活四", 10000);
        put("双冲四", 10000);
        put("冲死活三", 10000);
        put("双活三", 5000);
        put("活三眠三", 1000);
        put("冲四", 500);
        put("活三", 200);
        put("双活二", 100);
        put("眠三", 50);
        put("活二眠二", 10);
        put("活二", 5);
        put("眠二", 3);
        put("死四", -5);
        put("死三", -5);
        put("死二", -5);
    }};

    public AI(int[] piece, int player) {
        pieces = new int[15][15];
        this.player = player;
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                pieces[i][j] = piece[15 * i + j];
            }
        }
        setShapeValue();
    }

    void setShapeValue() {
        //依此为长连 活四 冲四3 活三2 眠三6 活二3 眠二6 死四 死三 死二
        //长连0 活四1 冲死234 活三56 眠三789 10 11 12 活二13 14 15 眠二16 17 18 19 20 21 死四22 死三 23 死二 24
        //共有 10类 25种

        //长连
        pieceShape1.add("11111");
        pieceShape2.add("22222");

        //活四
        pieceShape1.add("011110");
        pieceShape2.add("022220");

        //冲四
        pieceShape1.add("011112");
        pieceShape2.add("022221");
        pieceShape1.add("0101110");
        pieceShape2.add("0202220");
        pieceShape1.add("0110110");
        pieceShape2.add("0220220");

        //活三
        pieceShape1.add("01110");
        pieceShape2.add("02220");
        pieceShape1.add("010110");
        pieceShape2.add("020220");

        //眠三
        pieceShape1.add("001112");
        pieceShape2.add("002221");
        pieceShape1.add("010112");
        pieceShape2.add("020221");
        pieceShape1.add("011012");
        pieceShape2.add("022021");
        pieceShape1.add("10011");
        pieceShape2.add("20022");
        pieceShape1.add("10101");
        pieceShape2.add("20202");
        pieceShape1.add("2011102");
        pieceShape2.add("1022201");

        //活二
        pieceShape1.add("00110");
        pieceShape2.add("00220");
        pieceShape1.add("01010");
        pieceShape2.add("02020");
        pieceShape1.add("010010");
        pieceShape2.add("020020");

        //眠二
        pieceShape1.add("000112");
        pieceShape2.add("000221");
        pieceShape1.add("001012");
        pieceShape2.add("002021");
        pieceShape1.add("010012");
        pieceShape2.add("020021");
        pieceShape1.add("10001");
        pieceShape2.add("20002");
        pieceShape1.add("2010102");
        pieceShape2.add("1020201");
        pieceShape1.add("2011002");
        pieceShape2.add("1022001");

        //死四
        pieceShape1.add("211112");
        pieceShape2.add("122221");

        //死三
        pieceShape1.add("21112");
        pieceShape2.add("12221");

        //死二
        pieceShape1.add("2112");
        pieceShape2.add("1221");
    }

    int getValue(int x, int y, int player) {
        int value = 0;
        String shu, heng, pie, na;
        int x1, y1;//撇的边界
        int x2, y2;//捺的边界
        int[][] new_pieces = new int [15][15] ;
        int[] num_piece = new int[10];
        int[] num_value_piece = new int[25];
        List<String> pieceShape = new ArrayList();

        if (player == 1) {
            pieceShape = pieceShape1;
        } else {
            pieceShape = pieceShape2;
        }

        for (int i = 0; i < 10; i++) {
            num_piece[i] = 0;
        }
        for (int i = 0; i < 15; i++) {
            num_value_piece[i] = 0;
        }

        for(int i = 0 ;i<15;i++)
        {
            for(int j = 0 ;j<15;j++)
            {
                new_pieces[i][j] = pieces[i][j];
            }

        }


        new_pieces[x][y] = player;

        //计算撇捺的边界值
        int x11, y11, x12, y12;
        int x21, y21, x22, y22;
        x11 = 0;
        y11 = x11 + (y - x);
        y12 = 0;
        x12 = y12 - (y - x);

        x21 = 0;
        y21 = -x21 + (x + y);
        y22 = 14;
        x22 = (x + y) - y22;
        if (y11 >= 0 && y11 < 15) {
            y1 = y11;
            x1 = x11;
        } else {
            y1 = y12;
            x1 = x12;
        }
        if (y21 >= 0 && y21 < 15) {
            y2 = y21;
            x2 = x21;
        } else {
            y2 = y22;
            x2 = x22;
        }

        //初始化横撇竖捺的字符串
        heng = new String("");
        for (int i = 0; i < 15; i++) {
            Integer p = new Integer(new_pieces[x][i]);
            heng = heng + p.toString();
        }
        shu = new String("");
        for (int i = 0; i < 15; i++) {
            Integer p = new Integer(new_pieces[i][y]);
            shu = shu + p.toString();
        }
        pie = new String("");
        for (int i = 0; i < 14; i++) {
            int x0, y0;
            x0 = x1 + i;
            y0 = y1 + i;
            if (x0>=0&&x0 <= 14 && y0 >= 0&&y0<=14) {
                Integer p = new Integer(new_pieces[x0][y0]);
                pie = pie + p.toString();
            } else {
                break;
            }
        }
        na = new String("");
        for (int i = 0; i < 14; i++) {
            int x0, y0;
            x0 = x2 + i;
            y0 = y2 - i;
            if (x0>=0&&x0 <= 14 && y0 >= 0&&y0<=14) {
                Integer p = new Integer(new_pieces[x0][y0]);
                na = na + p.toString();
            } else {
                break;
            }
        }
        //利用正则表达式检查横撇竖捺
        for (int i = 0; i < 4; i++) {
            String content = new String();
            String pattren = new String();
            int pos;
            //System.out.println(x+" "+y+ " "+heng+"\n"+pie+"\n"+shu+"\n"+na);
            if (x - x1 >= 0) {
                pos = x - x1;
            } else {
                pos = x1 - x;
            }
            //循环检查横撇竖捺
            switch (i) {
                case 0: {
                    content = heng;
                    pos = x;
                    break;
                }
                case 1: {
                    content = shu;
                    pos = y;
                    break;
                }
                case 2: {
                    content = pie;
                    break;
                }
                case 3: {
                    content = na;
                    break;
                }
            }

            for (int j = 0; j < pieceShape.size(); j++) {
                int start = -1;
                pattren = pieceShape.get(j);
                Pattern p = Pattern.compile(pattren);

                Matcher m = p.matcher(content);

                while (m.find()) {
                    start = m.start();
                }
                //获取下子位置对于横竖撇捺 防止重复赋值
                if (start>0)
                {
                    int end = start + 6;

                    //System.out.println(content+"\n"+"pattren: "+pattren+"start: "+start+" end: "+end+" pos: "+pos);
                    num_value_piece[j]++;
                }
            }
        }
        //0:长连0 1:活四1 2:冲死234 3:活三56 4:眠三789 10 11 12 5:活二13 14 15 6:眠二16 17 18 19 20 21 7:死四22 8:死三 23 9:死二 24
        //长连个数
        num_piece[0] = num_value_piece[0];
        //活四
        num_piece[1] = num_value_piece[1];
        //冲死
        num_piece[2] = num_value_piece[2] + num_value_piece[3] + num_value_piece[4];
        //活三
        num_piece[3] = num_value_piece[5] + num_value_piece[6];
        //眠三
        num_piece[4] = num_value_piece[7] + num_value_piece[8] + num_value_piece[9] + num_value_piece[10] + num_value_piece[11] + num_value_piece[12];
        //活二
        num_piece[5] = num_value_piece[13] + num_value_piece[14] + num_value_piece[15];
        //眠二
        num_piece[6] = num_value_piece[16] + num_value_piece[17] + num_value_piece[18] + num_value_piece[19] + num_value_piece[20] + num_value_piece[21];
        //死四
        num_piece[7] = num_value_piece[22];
        //死三
        num_piece[8] = num_value_piece[23];
        //死二
        num_piece[9] = num_value_piece[24];

        //依此计算长连 活四 双冲四 冲四活三 双活三 活三眠三 冲四 活三 双活二 眠三 活二眠二 活二 眠二 死四 死三 死二
        value = value + num_piece[0] * pieceValue.get("长连") + num_piece[1] * pieceValue.get("活四") + num_piece[2] * pieceValue.get("冲四") + num_piece[3] * pieceValue.get("活三") + num_piece[4] * pieceValue.get("眠三")
                + num_piece[5] * pieceValue.get("活二") + num_piece[6] * pieceValue.get("眠二") + num_piece[7] * pieceValue.get("死四") + num_piece[8] * pieceValue.get("死三") + num_piece[9] * pieceValue.get("死二");
        if (num_piece[2] == 2) {
            value = value + pieceValue.get("双冲四");
        }
        /*
        if (num_piece[2] == 1 && num_piece[3] == 1) {
            value = value + pieceValue.get("冲四活三");
        }
        */
        if (num_piece[3] == 2) {
            value = value + pieceValue.get("双活三");
        }
        if (num_piece[3] == 1 && num_piece[4] == 1) {
            value = value + pieceValue.get("活三眠三");
        }
        if (num_piece[5] == 2) {
            value = value + pieceValue.get("双活二");
        }
        if (num_piece[5] == 1 && num_piece[6] == 1) {
            value = value + pieceValue.get("活二眠二");
        }
        return value;
    }

    public int Alpha_Beta(int depth,int [][] pieces,int alpha,int beta,int x,int y,int player)
    {
        if(depth == 2)
        {
            int other = (player==1)?2:1;
            temp[x][y] = getValue(x,y,player)+getValue(x,y,other);
            return temp[x][y];
        }
        int [][] new_pieces = new int [15][15];
        new_pieces = pieces;
        for(int i = 0;i<14;i++)
        {
            for(int j = 0;j<14;j++)
            {
                if(new_pieces[i][j] != 0)
                    continue;
                new_pieces[i][j] = player;
                player = (player == 1)?2:1;
                int value = -Alpha_Beta(depth+1,new_pieces,-beta,-alpha,i,j,player);
                new_pieces[i][j]=0;
                if(value > alpha)
                {
                    if(value>=beta)
                    {
                        return beta;
                    }
                }

                alpha = value;
            }
        }
        return alpha;
    }

    public int[] getXY()
    {
        int [] XY = new int[2];
        int val=0;
        int AIx=0;
        int AIy=0;
        Alpha_Beta(0,pieces,-1000000,1000000,0,0,player);
        for(int i = 0;i<15;i++)
        {
            for(int j = 0;j<15;j++)
            {
                if(val<temp[i][j]&&pieces[i][j]==0)
                {
                    val = temp[i][j];
                    System.out.println("值："+val);
                    AIx = i;
                    AIy = j;
                }
            }
        }

        XY[0]=AIx;
        XY[1]=AIy;

        return XY;
    }
/*
	public int[] getXY() {
		int x0 = -1, y0 = -1;
		int x1 = -1, y1 = -1;
		int maxValue = 0;
		int maxValue1 = 0;
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 15; j++) {
				if (pieces[i][j] == 0) {
					int value;
					value = getValue(i, j, player);
					if (value > maxValue) {
						x0 = i;
						y0 = j;
						maxValue = value;
					}
					int value2;
					value2 = getValue(i, j, 1);
					if (value2 > maxValue1) {
						x1 = i;
						y1 = j;
						maxValue1 = value2;
					}
				}

			}
		}
		int[] XY = new int[2];
		if (maxValue > maxValue1) {
			XY[0] = x0;
			XY[1] = y0;
		} else {
			XY[0] = x1;
			XY[1] = y1;
		}

		return XY;
	}
*/
}


