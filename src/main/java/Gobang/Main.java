package Gobang;

import Gobang.Controllers.GameModelController;
import Gobang.Controllers.TestController;
import Gobang.Database.Mysql;
import Gobang.Entity.GobangMap;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;



import java.io.IOException;


public class Main extends Application {

    static int time=0;//用来弹窗
    public Stage primaryStage;

    public int isChooseGameModel=0;                  //0表示没有选择模式，1代表人人对战，2代表人机对战  3代表对手提前退出游戏
    AnchorPane anchorPane;                           //游戏开始之后的fxml的AnchPane

    TestController testControl;
    public int getChessColor() {
        return chessColor;
    }

    public void setChessColor(int chessColor) {
        this.chessColor = chessColor;
    }

    GobangMap currentGobangMap=new GobangMap();

    public int [][] lastMap=new int[15][15];
    public int chessColor=1;    //chessColor表示即将要落的棋子 0代表白色，1代表黑色  人机默认自己先落子是黑色

    public  int count=0;   //count 表示当前是自己下的第几步骤

    public Boolean isEnterRewatch=false;  //是否进入回放模式

    public GobangMap gobangMap_temp;  //用于回放棋局暂存
    public int Rewatch_currentNum=0;    //用于暂存回放到哪一步
    public boolean isPlayerQiutSelf=false;  //判断对手是否逃跑

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("BJTU_Gobang");

        for(int i=0;i<15;i++)
            for(int j=0;j<15;j++)
                lastMap[i][j]=-1;

        showChooseGameModel();

    }

    public  int getTime() {
        return time;
    }

    public  void setTime(int time) {
        Main.time = time;
    }

    public void showtestUi() throws Exception {
        try
        {
            if(isChooseGameModel==1||isChooseGameModel==2) {
                Mysql.Connect();
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(Main.class.getResource("/FXML/test.fxml"));
                AnchorPane anchorPane = loader.load();
                this.anchorPane = anchorPane;

                TestController control = loader.getController();

                Scene scene = new Scene(anchorPane);
                primaryStage.setScene(scene);
                primaryStage.setMinWidth(1024.0);
                primaryStage.setMinHeight(768.0);
                primaryStage.setTitle("BJTU Gobang");

                primaryStage.setResizable(false);
                testControl = loader.getController();
                testControl.setMainApplication(this, anchorPane);
                primaryStage.show();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    public void showChooseGameModel() throws Exception {
        try
        {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/FXML/GameModel.fxml"));
            AnchorPane anchorPane = loader.load();
            this.anchorPane=anchorPane;

            GameModelController gameModelController = loader.getController();

            Scene scene = new Scene(anchorPane);
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(1024.0);
            primaryStage.setMinHeight(768.0);
            primaryStage.setTitle("BJTU Gobang");

            primaryStage.setResizable(false);
            gameModelController = loader.getController();
            gameModelController.setMainApplication(this);
            primaryStage.show();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    public void handleUIChange(int gamemodel)      //游戏界面转换控制
    {
        try {
            if (gamemodel == 1 || gamemodel == 2) {
                showtestUi();
            }
            if (gamemodel == 0 ) {

                showChooseGameModel();
            }

        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public GobangMap getCurrentGobangMap()
    {
        return currentGobangMap;
    }

    //刷新棋盘
    public void reflushMapUI(){
        boolean res=false;

        if(time==0&&isChooseGameModel==1) {
            double begin_x = testControl.getBoardImage().getLayoutX() + 27;//第一个棋子的坐标为（195+27，78+27）
            double begin_y = testControl.getBoardImage().getLayoutY() + 27;
            for (int i = 0; i < 15; i++) {
                for (int j = 0; j < 15; j++) {
                    if (currentGobangMap.map[i][j].getWhiteOrBlack() != lastMap[i][j]) {
                        lastMap[i][j] = currentGobangMap.map[i][j].getWhiteOrBlack();
                        ImageView qizi;
                        if (currentGobangMap.map[i][j].getWhiteOrBlack() == 1) {
                            qizi = new javafx.scene.image.ImageView(new Image("images/黑棋.png"));
                        } else {
                            qizi = new javafx.scene.image.ImageView(new Image("images/白棋.png"));
                        }
                        qizi.setLayoutX(i * 43 + begin_x - 17);
                        qizi.setLayoutY(j * 43 + begin_y - 17);
                        anchorPane.getChildren().add(qizi);
                        res = getCurrentGobangMap().judge(i, j);

                        System.out.println("判断结果是： "+res);
                    }
                }
            }
        }
        //用于给败方输出弹框
        if(time==0&&res&&isChooseGameModel==1){
            if(getChessColor()!=getCurrentGobangMap().getLastPlayer()) {
                System.out.println("棋子颜色为" + getChessColor() + "  上一步为" + getCurrentGobangMap().getLastPlayer());

                Alert _alert = new Alert(Alert.AlertType.INFORMATION);
                _alert.setTitle("游戏结束");
                _alert.setHeaderText(null);
                _alert.setContentText("对手获胜");
                System.out.println("判断结果是： "+res);
                _alert.initOwner(this.primaryStage);
                    _alert.show();

            }
            try{
                isChooseGameModel=0;
                showChooseGameModel();
            }catch (Exception e){}
            this.time = 1;
        }

    }

    public void setCurrentGobangMap(GobangMap currentGobangMap)
    {
        this.currentGobangMap = currentGobangMap;
    }

    public AnchorPane getAnchorPane() {
        return anchorPane;
    }

    public static void main(String[] args) {
        launch(args);
    }

    //人人结束刷新
    public void reflushLastMap(){
        for(int i=0;i<15;i++)
            for(int j=0;j<15;j++)
                lastMap[i][j]=-1;
    }

}

