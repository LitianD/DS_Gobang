package Gobang.Controllers;

import Gobang.Entity.GobangMap;
import Gobang.Entity.Vex;
import Gobang.Net.Network;
import Gobang.Thread.PlayThread;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import Gobang.Main;
import javafx.stage.Stage;

public class PutEvent implements EventHandler<MouseEvent> {

    Main main;
    double begin_x;
    double begin_y;

    public PutEvent(Main main, double x, double y) {
        this.main = main;
        begin_x = x;
        begin_y = y;
    }

    public void handle(MouseEvent event) {

        if (main.getCurrentGobangMap().getOverFlag() == 1 ) {
            if (main.getCurrentGobangMap().getLastPlayer() != main.getChessColor()) {
                ImageView qizi;

                double pos_x = event.getSceneX();
                double pos_y = event.getSceneY();
                int i = (int) ((pos_x - begin_x) / 43);
                int j = (int) ((pos_y - begin_y) / 43);
                double x = (pos_x - begin_x) % 43;
                double y = (pos_y - begin_y) % 43;

                if (x > 21.5) {
                    i++;
                }
                if (y > 21.5) {
                    j++;
                }

                if (main.getCurrentGobangMap().map[i][j].getWhiteOrBlack() == -1 && main.getCurrentGobangMap().getOverFlag() != 2) {
                    if (main.chessColor == 1) {
                        qizi = new ImageView(new Image("images/黑棋.png"));
                    } else {
                        qizi = new ImageView(new Image("images/白棋.png"));
                    }

                    qizi.setLayoutX(i * 43 + begin_x - 17);
                    qizi.setLayoutY(j * 43 + begin_y - 17);

                    main.getAnchorPane().getChildren().add(qizi);


                    final GobangMap gobangMap = main.getCurrentGobangMap();
                    int count = ++main.count;

                    gobangMap.map[i][j].setPut_number(count);
                    gobangMap.map[i][j].setI(i);
                    gobangMap.map[i][j].setJ(j);
                    gobangMap.map[i][j].setWhiteOrBlack(main.chessColor);
                    // main.chessColor = 1 - main.chessColor;//联机版本不需要改变本地棋子颜色

                    try {
                        Network.getNetwork().down(i, j, main.chessColor, main.getCurrentGobangMap().getTag(), count);

                        //每次下子后就更新一次map
                        Gson gson = new Gson();
                        String data = Network.getNetwork().getStrFromUri(Network.base_URI + "afterDown/" + gobangMap.getTag());
                        JsonParser parser = new JsonParser();
                        JsonObject temp = parser.parse(data).getAsJsonObject();
                        GobangMap gobangMap_ = gson.fromJson(temp, GobangMap.class);
                        main.setCurrentGobangMap(gobangMap_);
                        //main.getCurrentGobangMap().setLastPlayer(main.getChessColor());

                        if (main.getCurrentGobangMap().getOverFlag() != 2) {
                            new Thread(new Runnable() {
                                public void run() {
                                    try {
                                        while (main.getCurrentGobangMap().getLastPlayer() == main.getChessColor() && main.isPlayerQiutSelf == false) {
                                            Gson gson = new Gson();
                                            String data = Network.getNetwork().getStrFromUri(Network.base_URI + "player/" + gobangMap.getTag());
                                            JsonParser parser = new JsonParser();
                                            JsonObject temp = parser.parse(data).getAsJsonObject();
                                            GobangMap gobangMap_ = gson.fromJson(temp, GobangMap.class);
                                            main.setCurrentGobangMap(gobangMap_);
                                            if (main.getCurrentGobangMap().getOverFlag() == 3) {
                                                System.out.println("对手逃跑");
                                                main.isPlayerQiutSelf = true;
                                                main.isChooseGameModel=3;
                                                //Thread.interrupted();
                                              //  main.getAnchorPane().addEventHandler(MouseEvent.MOUSE_ENTERED,new ReflushEvent(main));
                                           //     Thread.interrupted();
                                            }
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    boolean res = gobangMap.judge(i, j);
                    if (res&&main.isChooseGameModel!=0) {
                        Alert _alert = new Alert(Alert.AlertType.INFORMATION);
                        _alert.setTitle("游戏结束");
                        _alert.setHeaderText(null);

                        if (main.chessColor == main.getCurrentGobangMap().getLastPlayer()) {
                            _alert.setContentText("己方获胜");
                        } else {
                            _alert.setContentText("对手获胜");
                        }
                        _alert.initOwner(main.primaryStage);
                        _alert.show();

                    }
                }

            }


        }
        else if(main.getCurrentGobangMap().getOverFlag()==3)
        {
            Network.getNetwork().quit(main.getCurrentGobangMap().getTag(),0);
            System.out.println("对手退出游戏，你赢了哦");
            Alert alert=new Alert(Alert.AlertType.INFORMATION,"对手认输，你赢了哦");
            alert.showAndWait();
            main.setCurrentGobangMap(new GobangMap());
            main.handleUIChange(0);
        }

    }
}
//}

