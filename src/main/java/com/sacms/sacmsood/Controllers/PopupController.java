package com.sacms.sacmsood.Controllers;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class PopupController implements Initializable {
    private String id;
    public Button popupOk,popupCancel;
    public TextField passwordField;
    public Text errorMsg;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        errorMsg.setVisible(false);
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
