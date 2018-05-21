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

       // boardImage.setVisible(false);
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

        //相对棋盘位置来下棋子，保证后期若由于界面调整棋盘移动后，棋子落子位置依旧准确
        ImageView qizi=new ImageView(new Image("images/黑棋.png"));
        double begin_x=boardImage.getLayoutX()+10;
        double begin_y=boardImage.getLayoutY()+11;

        qizi.setLayoutX(begin_x);
        qizi.setLayoutY(begin_y);

        System.out.println(begin_x+" "+begin_y);


        //每个棋子的像素是34px * 34px ，棋盘格子是43 * 43 px
        ImageView qizi_test=new ImageView(new Image("images/黑棋.png"));
        begin_x=begin_x+43;
        begin_y=begin_y+43;
        qizi_test.setLayoutX(begin_x);
        qizi_test.setLayoutY(begin_y);

        System.out.println(begin_x+" "+begin_y);

        ImageView qizi_test2=new ImageView(new Image("images/白棋.png"));
        begin_x=begin_x+43;
        begin_y=begin_y+43;
        qizi_test2.setLayoutX(begin_x);
        qizi_test2.setLayoutY(begin_y);

        System.out.println(begin_x+" "+begin_y);

        ImageView qizi_test3=new ImageView(new Image("images/黑棋.png"));
        begin_x=begin_x+43;
        begin_y=begin_y+43;
        qizi_test3.setLayoutX(begin_x);
        qizi_test3.setLayoutY(begin_y);

        anchorPane.getChildren().add(qizi);
        anchorPane.getChildren().add(qizi_test);
        anchorPane.getChildren().add(qizi_test2);
        anchorPane.getChildren().add(qizi_test3);

    }


}
