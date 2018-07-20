package Gobang.Controllers;

import Gobang.Database.Mysql;
import Gobang.Util.Alert_DIY;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import Gobang.Entity.GobangMap;
import Gobang.Main;
import Gobang.AI.AI;

import java.awt.*;

public class PutEventAI implements EventHandler<MouseEvent> {

    Main main;
    double begin_x;
    double begin_y;
    boolean res=false;
    int whoWin=-1;   //表示谁赢得了比赛 -1表示没有胜负，0表示白棋赢得了比赛，1表示黑棋赢得了比赛

    public PutEventAI(Main main ,double x,double y){
        this.main=main;
        begin_x=x;
        begin_y=y;
    }

    public void handle(MouseEvent event) {

        ImageView qizi;

        double pos_x =event.getSceneX();
        double pos_y = event.getSceneY();
        int i = (int)((pos_x-begin_x)/43);
        int j = (int)((pos_y-begin_y)/43);
        double x = (pos_x-begin_x)%43;
        double y = (pos_y-begin_y)%43;

        if(x > 21.5)
        {
            i++;
        }
        if(y > 21.5){
            j++;
        }

        if(main.getCurrentGobangMap().map[i][j].getWhiteOrBlack()==-1&&main.chessColor==1) //这个点上没有棋子并且即将要落的子为黑色
        {

            qizi=new ImageView(new Image("images/黑棋.png"));

            qizi.setLayoutX(i*43+begin_x-17);
            qizi.setLayoutY(j*43+begin_y-17);

            main.getAnchorPane().getChildren().add(qizi);
            System.out.println("AnchPane size is "+main.getAnchorPane().getChildren().size());

            GobangMap gobangMap = main.getCurrentGobangMap();

            gobangMap.map[i][j].setPut_number(++main.count);
            gobangMap.map[i][j].setI(i);
            gobangMap.map[i][j].setJ(j);
            gobangMap.map[i][j].setWhiteOrBlack(main.chessColor);  //玩家下子之后马上进行判断胜负

            res=gobangMap.judge(i,j);
            if(res)
            {
                boolean isRewatch=false;
                isRewatch=Alert_DIY.f_alert_confirmDialog("棋局结束","黑棋胜利，要进行回放吗",main.primaryStage);
                whoWin=1;
                Mysql.insert(main.count,"Player");
                System.out.println("插入数据库");
                //如果用户点击确定回看
                if(isRewatch)
                {
                    main. isEnterRewatch=true;
                    main.gobangMap_temp=main.getCurrentGobangMap();
                    main.handleUIChange(2);

                }

            }

            //
            if(whoWin!=1) {
                main.chessColor = 0;       //表示即将要落白子（AI 落子）
                int[] XY = new int[2];

                AI ai = new AI(main.getCurrentGobangMap().getArray(), 2);
                XY = ai.getXY();
                System.out.println("AI" + XY[0] + " " + XY[1]);
                qizi = new ImageView(new Image("images/白棋.png"));
                qizi.setLayoutX(XY[0] * 43 + begin_x - 17);
                qizi.setLayoutY(XY[1] * 43 + begin_y - 17);
                main.getAnchorPane().getChildren().add(qizi);

                System.out.println("AnchPane size is "+main.getAnchorPane().getChildren().size());

                gobangMap.map[XY[0]][XY[1]].setPut_number(++main.count);
                gobangMap.map[XY[0]][XY[1]].setI(XY[0]);
                gobangMap.map[XY[0]][XY[1]].setJ(XY[1]);
                gobangMap.map[XY[0]][XY[1]].setWhiteOrBlack(0);

                main.chessColor = 1;

                res=gobangMap.judge(XY[0], XY[1]);
                if(res)
                {
                    boolean isRewatch=false;
                    isRewatch=Alert_DIY.f_alert_confirmDialog("棋局结束","白棋胜利，要进行回放吗",main.primaryStage);
                    whoWin=0;
                    Mysql.insert(main.count,"Computer");
                    System.out.println("插入数据库");
                    //如果用户点击确定回看
                    if(isRewatch)
                    {
                        main. isEnterRewatch=true;
                        main.gobangMap_temp=main.getCurrentGobangMap();
                        main.handleUIChange(2);

                    }
                }
            }
        }
    }


}

