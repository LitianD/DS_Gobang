package Gobang.Controllers;
import Gobang.Constant.Constant;
import Gobang.Database.Mysql;
import Gobang.Entity.GobangMap;
import Gobang.Entity.Player;
import Gobang.Main;

import Gobang.Net.Network;
import Gobang.Thread.MatchThread;
import Gobang.Thread.PlayThread;
import Gobang.Util.Alert_DIY;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import sun.nio.ch.Net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class TestController {

    Main mainApplication;
    AnchorPane anchorPane;
    Network network = Network.getNetwork();
    Constant constant = Constant.getConstant();

    @FXML
    Button OutRewatch;

    @FXML
    Button Rewatch_next;

    @FXML
    Button Rewatch_pre;

    @FXML
    Button cheki;

    @FXML
    Button surrender;

    @FXML
    Button peace;

    @FXML
    Button solo;

    @FXML
    Button renji;

    @FXML
    Button refresh;

    @FXML
    Button returnToChooseModel;

    @FXML
    ImageView boardImage;

    @FXML
    ComboBox<String> playerComboBox;

    @FXML
    Button leaderboard;


    @FXML
    private void initialize() {
        flushUser();
    }

    String myname = null;


    public void setMainApplication(Main mainApplication, AnchorPane anchorPane) {

        this.mainApplication = mainApplication;
        this.anchorPane = anchorPane;

        double begin_x = boardImage.getLayoutX() + 27;//第一个棋子的坐标为（195+27，78+27）
        double begin_y = boardImage.getLayoutY() + 27;

        //默认没有进入回放模式
        this.Rewatch_next.setVisible(false);
        this.Rewatch_pre.setVisible(false);
        this.OutRewatch.setVisible(false);

        //如果进入回放模式
        if(mainApplication.isEnterRewatch)
        {
            this.refresh.setVisible(false);
            this.Rewatch_next.setVisible(true);
            this.Rewatch_pre.setVisible(true);
            this.cheki.setVisible(false);
            this.peace.setVisible(false);
            this.renji.setVisible(false);
            this.playerComboBox.setVisible(false);
            this.solo.setVisible(false);
            this.surrender.setVisible(false);
            this.returnToChooseModel.setVisible(false);
            this.OutRewatch.setVisible(true);
        }

        //人人对战  进入回放模式之后不可以下子
        if (mainApplication.isChooseGameModel == 1&&mainApplication.isEnterRewatch==false)
        {
            this.refresh.setVisible(false);
            this.playerComboBox.setVisible(false);
            this.surrender.setVisible(false);
            this.cheki.setVisible(false);
            this.renji.setVisible(false);
            this.refresh.setVisible(false);
            this.peace.setVisible(false);

            boardImage.addEventHandler(MouseEvent.MOUSE_CLICKED, new PutEvent(mainApplication, begin_x, begin_y));
            anchorPane.addEventHandler(MouseEvent.MOUSE_ENTERED,new ReflushEvent(mainApplication));
        }
        //人机对战  进入回放模式之后不可以下子
        if (mainApplication.isChooseGameModel == 2&&mainApplication.isEnterRewatch==false)
        {

            this.surrender.setVisible(false);
            this.playerComboBox.setVisible(false);
            this.solo.setVisible(false);
            this.renji.setVisible(false);
            this.playerComboBox.setVisible(false);
            this.peace.setVisible(false);

            boardImage.addEventHandler(MouseEvent.MOUSE_CLICKED, new PutEventAI(mainApplication, begin_x, begin_y));
        }
    }

    @FXML
    public void clickSolo() {
        Dialog<Pair<String, String>> dialog = new Dialog<Pair<String, String>>();
        dialog.setTitle("输入用户名");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);


        final TextField name = new TextField();
        name.setPromptText("用户名");

        grid.add(new Label("用户名:"), 0, 0);
        grid.add(name, 1, 0);

        dialog.getDialogPane().setContent(grid);

        dialog.setOnCloseRequest(new EventHandler<DialogEvent>() {
            public void handle(DialogEvent event) {
                Player player = new Player(name.getText());
                player.setFlag(-1);
                player.setBlackOrWhite(-1);

                if (name.getText().equals("")) {
                    Alert _alert = new Alert(Alert.AlertType.INFORMATION);
                    _alert.setTitle("加入游戏");
                    _alert.setHeaderText(null);
                    _alert.setContentText("请输入用户名");
                    _alert.show();
                } else {
                    //设置自己棋子颜色
                    myname = player.getUsername();
                    mainApplication.setTime(0);
                    final GobangMap gobangMap = network.joinSolo(player);//得到匹配ing时候的gobangMap
                    System.out.println("桌号为："+gobangMap.getTag()+"游戏状态为:"+gobangMap.getOverFlag());
                    if (gobangMap.getP1().getUsername().equals(myname)) {
                        mainApplication.setChessColor(gobangMap.getP1().getBlackOrWhite());
                    } else {
                        mainApplication.setChessColor(gobangMap.getP2().getBlackOrWhite());
                    }

                    solo.setVisible(false);

                    System.out.println("当前游戏状态为："+gobangMap.getOverFlag());


                    //如果游戏没有结束
                    if (gobangMap.getOverFlag() != 2) {
                        //如果游戏可以进行匹配
                        if (gobangMap.getOverFlag() == 0) {
                            mainApplication.setChessColor(1);
                            MatchThread matchThread = new MatchThread(gobangMap.getTag());
                            matchThread.setMap(mainApplication);
                            int conTime=0;
                            while (conTime<=25&&mainApplication.getCurrentGobangMap().getOverFlag() == 0) {
                                try {
                                    Thread thread1 = new Thread(matchThread);
                                    thread1.start();
                                    thread1.join();
                                    conTime++;
                                    System.out.println("第"+conTime+"次匹配");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            if (mainApplication.getCurrentGobangMap().getOverFlag() == 0) {
                                Alert _alert = new Alert(Alert.AlertType.INFORMATION);
                                _alert.setContentText("连接超时");
                                _alert.show();
                                _alert.setOnCloseRequest(new EventHandler<DialogEvent>() {
                                    public void handle(DialogEvent event) {
                                        Network.getNetwork().quit(-1,0);
                                        try{
                                            mainApplication.showtestUi();
                                            mainApplication.setCurrentGobangMap(new GobangMap());
                                        }catch (Exception e){}
                                    }
                                });
                            }
                        }
                        //如果在游戏中
                        else if (gobangMap.getOverFlag() == 1) {
                            System.out.println("游戏进行中");
                            mainApplication.setChessColor(0);
                            new Thread(new Runnable() {
                                public void run() {
                                    try {
                                        while (mainApplication.getCurrentGobangMap().getLastPlayer() == mainApplication.getChessColor()&&mainApplication.isPlayerQiutSelf==false) {
                                            Gson gson = new Gson();
                                            String data = Network.getNetwork().getStrFromUri(Network.base_URI + "player/" + gobangMap.getTag());
                                            JsonParser parser = new JsonParser();
                                            JsonObject temp = parser.parse(data).getAsJsonObject();
                                            GobangMap gobangMap_ = gson.fromJson(temp, GobangMap.class);
                                            mainApplication.setCurrentGobangMap(gobangMap_);
                                            if(gobangMap_.getOverFlag()==3)
                                            {
                                                mainApplication.isPlayerQiutSelf=true;
                                                mainApplication.isChooseGameModel=3;
                                              //  Thread.interrupted();
                                           //     anchorPane.addEventHandler(MouseEvent.MOUSE_ENTERED,new ReflushEvent(mainApplication));
                                                //Thread.interrupted();
                                            }
                                            System.out.println("接收到的消息: flag "+mainApplication.getCurrentGobangMap().getOverFlag()+" isPlayerQiut "+mainApplication.isPlayerQiutSelf);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();
                        }
                    }
                    //游戏结束
                }
            }
        });
        dialog.show();

    }

    @FXML
    public void clickRenji() {
        try {
            network.getUsers();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void clickReturnToChooseModel()     //要是联机模式的话不能直接退出，要进行认输处理
    {
        if (mainApplication.isChooseGameModel == 1) {
            boolean isQiut = Alert_DIY.f_alert_confirmDialog("Qiut?", "确定退出了嘛？退出会直接认输欸..", mainApplication.primaryStage);
            if (isQiut) {
                Network.getNetwork().quit(mainApplication.getCurrentGobangMap().getTag(),1);
                mainApplication.isChooseGameModel = 3;
                //重置main里面的参数
                InitMainApplication();
                mainApplication.handleUIChange(0);
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "你还在游戏中欸");
            }
        }
        if (mainApplication.isChooseGameModel == 2) {
            mainApplication.isChooseGameModel = 0;
            //重置main里面的参数
            InitMainApplication();
            mainApplication.handleUIChange(0);
        }

    }

    //刷新用户列表
    public void flushUser() {
        try {
            playerComboBox.getItems().remove(0, playerComboBox.getItems().size());
            List<Player> playerList = network.getUsers();
            for (Player temp : playerList)
                playerComboBox.getItems().add(temp.getUsername() + " " + constant.getStates(temp.getFlag()));
        } catch (Exception e) {
            e.printStackTrace();
            ;
        }
    }

    //重玩
    @FXML
    public void refresh() {
        try {
            //重置棋盘信息
            InitMainApplication();

            //ui变换的时候调用handleUIChange（游戏模式）因为在重新进入TestController时会根据main的不同参数来进行界面/功能调整
            // 不要直接调用showxxxxUi
            if(mainApplication.isChooseGameModel==2)
            {
                mainApplication.handleUIChange(2);
            }
            else
            {
                //Network.getNetwork().quit(mainApplication.getCurrentGobangMap().getTag(),1);
                mainApplication.handleUIChange(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ImageView getBoardImage() {
        return boardImage;
    }



    //人机悔棋//按一下消失两个棋子
    @FXML
    public void Handlecheki() {

        for (int k = 0; k < 2; k++) {
            if (mainApplication.count >= 2)
            {
                int index_x = 0;
                int index_y = 0;
                for (index_x = 0; index_x < 15; index_x++) {
                    for (index_y = 0; index_y < 15; index_y++) {
                        System.out.println("main count 是：" + mainApplication.count);
                        if (mainApplication.count == mainApplication.getCurrentGobangMap().map[index_x][index_y].getPut_number()) {
                            mainApplication.getCurrentGobangMap().map[index_x][index_y].setPut_number(-1);
                            int temp = anchorPane.getChildren().size() - 1;
                            System.out.println("即将要移除的棋子index是： " + temp);
                            anchorPane.getChildren().remove(temp);
                            mainApplication.getCurrentGobangMap().map[index_x][index_y].setWhiteOrBlack(-1);
                            mainApplication.getCurrentGobangMap().map[index_x][index_y].setPut_number(-1);
                        }
                    }
                }
                mainApplication.count--;
            }
            if(mainApplication.count==1)
            {
                for(int i=0;i<15;i++)
                {
                    for(int j=0;j<15;j++)
                    {
                        mainApplication.getCurrentGobangMap().map[i][j].setPut_number(-1);
                        mainApplication.getCurrentGobangMap().map[i][j].setWhiteOrBlack(-1);
                    }
                }
                anchorPane.getChildren().remove(anchorPane.getChildren().size() - 1);
                mainApplication.count--;
            }

        }
        System.out.println("悔棋之后的main count是："+mainApplication.count);
    }


    //往后回看一步
    @FXML
    public void handleReWatch_next()
    {
        ImageView qizi;
        if(mainApplication.isEnterRewatch&&mainApplication.Rewatch_currentNum<mainApplication.count)
        {
            int index_x=0;
            int index_y=0;

                for(index_x=0;index_x<15;index_x++)
                {
                    for(index_y=0;index_y<15;index_y++)
                    {
                        if(mainApplication.gobangMap_temp.map[index_x][index_y].getPut_number()==mainApplication.Rewatch_currentNum+1)
                        {
                            int test_RightOrNot=mainApplication.gobangMap_temp.map[index_x][index_y].getPut_number();
                            if(mainApplication.gobangMap_temp.map[index_x][index_y].getWhiteOrBlack()==1)
                            {
                                System.out.println("往后回看了一个黑棋子："+test_RightOrNot+" "+mainApplication.Rewatch_currentNum+1);
                                qizi=new ImageView(new Image("images/黑棋.png"));
                                qizi.setLayoutX(index_x*43+boardImage.getLayoutX()+27-17);
                                qizi.setLayoutY(index_y*43+boardImage.getLayoutY()+27-17);
                                anchorPane.getChildren().add(qizi);

                            }
                            else if (mainApplication.gobangMap_temp.map[index_x][index_y].getWhiteOrBlack()==0)
                            {
                                System.out.println("往后回看了一个白棋子："+test_RightOrNot+" "+mainApplication.Rewatch_currentNum+1);
                                qizi=new ImageView(new Image("images/白棋.png"));
                                qizi.setLayoutX(index_x*43+boardImage.getLayoutX()+27-17);
                                qizi.setLayoutY(index_y*43+boardImage.getLayoutY()+27-17);
                                anchorPane.getChildren().add(qizi);
                            }
                            else {
                                System.out.println("往后回看出错了");
                            }

                        }
                    }
                }
            mainApplication.Rewatch_currentNum++;
        }
    }

    //往前回看一步
    @FXML
    public void handleReWatch_pre()
    {

        if(mainApplication.isEnterRewatch)
        {
            if(mainApplication.Rewatch_currentNum>0) {
                int index = anchorPane.getChildren().size() - 1;
                anchorPane.getChildren().remove(index);
                mainApplication.Rewatch_currentNum--;
            }
        }
    }

    @FXML
    public void handleOutRewatchAndReplay()
    {
        mainApplication.isEnterRewatch=false;
        InitMainApplication();
        mainApplication.handleUIChange(2);
    }


    //重置main的变量
    public void InitMainApplication()
    {
        mainApplication.chessColor=1;
        mainApplication.isPlayerQiutSelf=false;
        mainApplication.setCurrentGobangMap(new GobangMap());
        mainApplication.isEnterRewatch=false;
        mainApplication.Rewatch_currentNum=0;
        mainApplication.count=0;
    }


}
