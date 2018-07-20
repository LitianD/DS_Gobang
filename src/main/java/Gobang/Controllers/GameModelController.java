package Gobang.Controllers;

import Gobang.Database.Mysql;
import Gobang.Entity.GobangMap;
import Gobang.Main;
import Gobang.Net.Network;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;


import javax.xml.soap.Text;
import java.sql.ResultSet;
import java.sql.Statement;

public class GameModelController {


    Main main;
    @FXML
    MediaView mediaView;

    @FXML
    Label label;

    @FXML
    private void initialize()
    {
        String url = Thread.currentThread().getContextClassLoader().getResource("Media/BC51.mp4").toString();
        Media media = new Media(url);
        MediaPlayer mplayer = new MediaPlayer(media);
        mediaView.setMediaPlayer(mplayer);
        mplayer.play();
        label.setVisible(false);
    }

    public void setMainApplication(Main mainApplication)
    {
        this.main=mainApplication;
    }

    @FXML
    public void HandlePVPButton()
    {
        label.setVisible(false);
        main.isPlayerQiutSelf=false;
        main.setCurrentGobangMap(new GobangMap());
        main.isChooseGameModel=1;
        main.isEnterRewatch=false;
        main.count=0;
        main.gobangMap_temp=new GobangMap();
        main.chessColor=1;
        main.Rewatch_currentNum=0;
        for(int i=0;i<15;i++)
        {
            for(int j=0;j<15;j++)
            {
                main.lastMap[i][j]=-1;
            }
        }

        main.handleUIChange(1);
    }

    @FXML
    public void HandlePVCButton()
    {
        label.setVisible(false);
        main.isChooseGameModel=2;
        main.isPlayerQiutSelf=false;
        main.setCurrentGobangMap(new GobangMap());
        main.isEnterRewatch=false;
        main.count=0;
        main.gobangMap_temp=new GobangMap();
        main.chessColor=1;
        main.Rewatch_currentNum=0;
        for(int i=0;i<15;i++)
        {
            for(int j=0;j<15;j++)
            {
                main.lastMap[i][j]=-1;
            }
        }
        main.handleUIChange(2);
    }

    //排行榜
    @FXML
    public void showLeaderBoard(){
        String str="";
        try{
            Mysql.Connect();
            //ResultSet result=Mysql.getResult();
            Statement statement= Mysql.getStmt();
            label.setVisible(true);
            String sql;
            sql = "SELECT name, count FROM gobang  order by count limit 0,10";
            ResultSet rs = statement.executeQuery(sql);

            // 展开结果集数据库
            while(rs.next()){
                // 通过字段检索
                String name = rs.getString("name");
                int count = rs.getInt("count");
                str+="名字: " + name+" "+"步数: " + count+"\n";
                // 输出数据
                System.out.println("名字: " + name);
                System.out.println("步数: " + count);
            }
            label.setText(str);
            rs.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
