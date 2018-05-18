package Gobang.Controllers;
import Gobang.Main;
import javafx.fxml.FXML;

public class TestController {

    Main mainApplication;

    @FXML
    private void initialize()
    {
    }

    public void setMainApplication(Main mainApplication){
        this.mainApplication=mainApplication;
    }
}
