package Gobang.Util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import java.util.Optional;

/*
这个类是自己实现的自定义Alert，直接调用静态方法就可以（用来确定是否实现响应按钮点击事件）
* */
public class Alert_DIY {
    static public boolean f_alert_confirmDialog(String p_header,String p_message,Stage s){

        Alert _alert = new Alert(Alert.AlertType.CONFIRMATION,p_message,new ButtonType("取消", ButtonBar.ButtonData.NO),
                new ButtonType("确定", ButtonBar.ButtonData.YES));

        _alert.setTitle("确认");
        _alert.setHeaderText(p_header);

        _alert.initOwner(s);
//        showAndWait() 将在对话框消失以前不会执行之后的代码
        Optional<ButtonType> _buttonType = _alert.showAndWait();
////        根据点击结果返回
        if(_buttonType.get().getButtonData().equals(ButtonBar.ButtonData.YES)){
            return true;
        }
        else {
            return false;
        }
    }
}