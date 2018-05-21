package Gobang.Controllers;
import Gobang.Main;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;


public class TestController {

    Main mainApplication;
    AnchorPane anchorPane;

    @FXML
    Button cheki;

    @FXML
    Button surrender;

    @FXML
    Button peace;

    @FXML
    ImageView boardImage;
    @FXML
    private void initialize()
    {

        boardImage.addEventHandler(MouseEvent.MOUSE_CLICKED,new PutEvent());
        //boardImage.setVisible(false);


       // Image cheki_image=new Image("images/悔棋.png");
      //  cheki.setGraphic(new ImageView(cheki_image));


//        DropShadow shadow = new DropShadow();
////Adding the shadow when the mouse cursor is on
//        cheki.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
//            cheki.setEffect(shadow);
//        });
//
////Removing the shadow when the mouse cursor is off
//        cheki.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
//            cheki.setEffect(null);
//        });
    }

    public void setMainApplication(Main mainApplication, AnchorPane anchorPane){
        this.mainApplication=mainApplication;
        this.anchorPane=anchorPane;
               ImageView qizi=new ImageView(new Image("images/黑棋.png"));
        double begin_x=boardImage.getLayoutX();
        begin_x=begin_x-22;  //
        double begin_y=boardImage.getLayoutY();
        begin_y=begin_y-15;  //
        qizi.setLayoutX(begin_x);
        qizi.setLayoutY(begin_y);

        ImageView qizi_test=new ImageView(new Image("images/黑棋.png"));

        begin_x=begin_x+43;

        begin_y=begin_y+43;
        qizi_test.setLayoutX(begin_x);
        qizi_test.setLayoutY(begin_y);
        //anchorPane.getChildren().add(qizi);
        anchorPane.getChildren().add(qizi_test);

    }


}
