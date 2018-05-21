package Gobang.Controllers;

import Gobang.Entity.GobangMap;
import Gobang.Entity.Vex;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import Gobang.Main;
import javafx.stage.Stage;

public class PutEvent implements EventHandler<MouseEvent>{

    Main main;
    double begin_x;
    double begin_y;


    public PutEvent(Main main ,double x,double y){
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

        if(x > 21.5){
            i++;
        }
        if(y > 21.5){
            j++;
        }

        if(main.getCurrentGobangMap().map[i][j].getWhiteOrBlack()==-1)
        {
            if(main.chessColor==1)
            {
                qizi=new ImageView(new Image("images/黑棋.png"));
            }

            else
            {
                qizi=new ImageView(new Image("images/白棋.png"));
            }

            qizi.setLayoutX(i*43+begin_x-17);
            qizi.setLayoutY(j*43+begin_y-17);

            main.getAnchorPane().getChildren().add(qizi);


            GobangMap gobangMap = main.getCurrentGobangMap();
            int count = ++main.count;

            gobangMap.map[i][j].setPut_number(count);
            gobangMap.map[i][j].setI(i);
            gobangMap.map[i][j].setJ(j);
            gobangMap.map[i][j].setWhiteOrBlack(main.chessColor);
            main.chessColor = 1 - main.chessColor;
            boolean res=gobangMap.judge(i,j);
            if(res){
                Alert _alert = new Alert(Alert.AlertType.INFORMATION);
                _alert.setTitle("游戏结束");
                _alert.setHeaderText(null);

                if(main.chessColor==0)
                    _alert.setContentText("黑棋获胜");
                else
                    _alert.setContentText("白棋获胜");
                _alert.initOwner(main.primaryStage);
                _alert.show();

                //刷新棋盘
                try{
                    main.showtestUi();
                    main.setCurrentGobangMap(new GobangMap());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}
