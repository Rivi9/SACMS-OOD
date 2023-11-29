package com.sacms.sacmsood;

import com.sacms.sacmsood.Models.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;

import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class MainApp extends Application {
    public static User user;

    @Override
    public void start(Stage stage) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("fxmls/login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 840, 540);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("SACMS");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }
}