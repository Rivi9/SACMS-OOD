package com.sacms.sacmsood.Controllers;

import com.sacms.sacmsood.MainApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SideBarController{
    public ToggleGroup sidebar;
    public ToggleButton homeBtn,clubBtn,eventBtn,signoutBtn;

    String homeURL,clubsURL,eventsURL;

    public void setURLs(String homeURL,String clubsURL,String eventsURL){
        this.homeURL=homeURL;
        this.clubsURL=clubsURL;
        this.eventsURL=eventsURL;
    }

    public void onHomeClick(ActionEvent event) throws IOException {
        FXMLLoader root =new FXMLLoader(MainApp.class.getResource("fxmls/"+homeURL+".fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root.load(), 990, 660);
        stage.setScene(scene);
        stage.show();
    }

    public void onClubsClick(ActionEvent event) throws IOException {
        FXMLLoader root =new FXMLLoader(MainApp.class.getResource("fxmls/"+clubsURL+".fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root.load(), 990, 660);
        stage.setScene(scene);
        stage.show();
    }

    public void onEventsClick(ActionEvent event) throws IOException {
        FXMLLoader root =new FXMLLoader(MainApp.class.getResource("fxmls/"+eventsURL+".fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root.load(), 990, 660);
        stage.setScene(scene);
        stage.show();
    }

    public void onSignoutClick(ActionEvent event) throws IOException {
        FXMLLoader root =new FXMLLoader(MainApp.class.getResource("fxmls/login.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root.load(), 840, 540);
        stage.setScene(scene);
        stage.show();
    }
}
