package com.sacms.sacmsood.Controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class TopBarController {
    public Button minimizeButton,closeButton;

    public Stage stage(ActionEvent event){
        return (Stage)((Node)event.getSource()).getScene().getWindow();
    }

    //    handles top bar window control button actions
    public void close(){
        Platform.exit();
    }

    public void minimize(ActionEvent event) {
        stage(event).setIconified(true);
    }


}
