package Gobang.Controllers;

import Gobang.Main;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import java.util.Timer;
import java.util.TimerTask;

public class ReflushEvent implements EventHandler<MouseEvent> {

    Main main=null;

    public ReflushEvent(Main main) {
        this.main = main;
    }
    public void handle(MouseEvent event) {
        if(main.isChooseGameModel==1)
        {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                public void run() {
                    Platform.runLater(new Runnable() {
                        public void run() {
                            main.reflushMapUI();
                        }
                    });
                }
            }, 6000, 2000);
        }

    }
}
