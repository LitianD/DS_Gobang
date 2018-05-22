package Gobang.Controllers;
import Gobang.Constant.Constant;
import Gobang.Entity.GobangMap;
import Gobang.Entity.Player;
import Gobang.Main;

import Gobang.Net.Network;
import Gobang.Thread.MatchThread;
import com.google.gson.Gson;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;


public class TestController {

    Main mainApplication;
    AnchorPane anchorPane;
    Network network=Network.getNetwork();
    Constant constant=Constant.getConstant();
    @FXML
    Button cheki;

    @FXML
    Button surrender;

    @FXML
    Button peace;

    @FXML
    ImageView boardImage;
    @FXML
    Button solo;
    @FXML
    Button renji;
    @FXML
    Button refresh;

    @FXML
    ComboBox<String> playerComboBox;
    @FXML
    private void initialize()
    {
        flushUser();
    }

    public void setMainApplication(Main mainApplication, AnchorPane anchorPane){//落子
        this.mainApplication=mainApplication;
        this.anchorPane=anchorPane;

        double begin_x=boardImage.getLayoutX()+27;//第一个棋子的坐标为（195+27，78+27）
        double begin_y=boardImage.getLayoutY()+27;

        boardImage.addEventHandler(MouseEvent.MOUSE_CLICKED,new PutEvent(mainApplication,begin_x,begin_y));
    }

    @FXML
    public void clickSolo(){
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
                Player player=new Player(name.getText());
                player.setFlag(-1);
                player.setBlackOrWhite(-1);

                if(name.getText().equals("")) {
                    Alert _alert = new Alert(Alert.AlertType.INFORMATION);
                    _alert.setTitle("加入游戏");
                    _alert.setHeaderText(null);
                    _alert.setContentText("请输入用户名");
                    _alert.show();
                }
                else{
                    GobangMap gobangMap=network.joinSolo(player);
                    solo.setVisible(false);
                    //如果游戏可以开始
                    if(gobangMap.getOverFlag()==0) {
                        MatchThread matchThread = new MatchThread(gobangMap.getTag());
                        Thread thread = new Thread(matchThread);
                        thread.start();
                    }
                }
            }
        });
        dialog.show();
    }

    @FXML
    public void clickRenji(){
        try{
            network.getUsers();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //刷新用户列表
    public void flushUser(){
        try{
            playerComboBox.getItems().remove(0,playerComboBox.getItems().size());
            List<Player> playerList=network.getUsers();
            for(Player temp:playerList)
                playerComboBox.getItems().add(temp.getUsername()+" "+ constant.getStates(temp.getFlag()));
        }catch(Exception e){
            e.printStackTrace();;
        }
    }

    //重玩
    public void refresh(){
        try{
            mainApplication.showtestUi();
            mainApplication.setCurrentGobangMap(new GobangMap());
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
