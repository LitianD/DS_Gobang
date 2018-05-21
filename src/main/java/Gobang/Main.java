package Gobang;

import Gobang.Controllers.TestController;
import Gobang.Entity.GobangMap;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    public Stage primaryStage;

    AnchorPane anchorPane;
    GobangMap currentGobangMap=new GobangMap();
    public int chessColor=1;
    public  int count=0;
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("BJTU_Gobang");
        showtestUi();
    }

    public void showtestUi() throws Exception {
        try
        {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/FXML/test.fxml"));
            AnchorPane anchorPane = loader.load();
            this.anchorPane=anchorPane;

            TestController control = loader.getController();

            Scene scene = new Scene(anchorPane);
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(1024.0);
            primaryStage.setMinHeight(768.0);
            primaryStage.setTitle("BJTU Gobang");

            primaryStage.setResizable(true);
            TestController testControl = loader.getController();
            testControl.setMainApplication(this,anchorPane);
            primaryStage.show();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    public GobangMap getCurrentGobangMap()
    {
        return currentGobangMap;
    }

    public void setCurrentGobangMap(GobangMap currentGobangMap) {
        this.currentGobangMap = currentGobangMap;
    }

    public AnchorPane getAnchorPane() {
        return anchorPane;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

