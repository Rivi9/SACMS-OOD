package com.sacms.sacmsood.Controllers;


import com.sacms.sacmsood.MainApp;
import com.sacms.sacmsood.Models.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;


public class LoginController implements Initializable {

    public AnchorPane bgAnchorPane;
    public ImageView loginImg;
    public TextField pwTextField,idTextField;
    public PasswordField pwField;
    public Button pwBtn,LoginBtn;
    public FontIcon pwBtnIcon;
    public Label errorText;

    private static String userID;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loginImg.fitWidthProperty().bind(bgAnchorPane.widthProperty());
        loginImg.fitHeightProperty().bind(bgAnchorPane.heightProperty());
        errorText.setVisible(false);
    }

    public void togglePwVisibility(){
        if (pwField.isVisible()){
            pwTextField.setText(pwField.getText());
            pwField.setVisible(false);
            pwBtnIcon.setIconLiteral("anto-eye");
        } else if (!pwField.isVisible()){
            pwField.setText(pwTextField.getText());
            pwField.setVisible(true);
            pwBtnIcon.setIconLiteral("anto-eye-invisible");
        }
    }

    // 1. onLoginBtnClick method
    public void onLoginBtnClick(ActionEvent event) throws SQLException, IOException {
        userID= idTextField.getText().trim();
        String passWord= pwField.isVisible() ? pwField.getText().trim():pwTextField.getText().trim();
        // 1.1 Notifying the User
        if(userID.isEmpty() || passWord.isEmpty()){
            errorText.setVisible(true);
        }else{
            String dashType=checkUser(userID, passWord);
            if(dashType==null){     // 1.3 Inform the User
                errorText.setVisible(true);
            }else switchToDashBoard(event, dashType);       // 1.4 switchToDashBoard
        }
    }

    // 1.2. checkUser method
    public String checkUser(String userID, String passWord) throws SQLException {
        ResultSet users;
        if (userID.startsWith("2")) {
            // 1.2.3 Getting the userId
            users=mysqlConnector.execute("SELECT * FROM Student WHERE studentid="+userID);
            if(!(users.next())){
                return null;
            }else if(users.getString(6).equals(passWord)){
                MainApp.user=new Student(
                        users.getString(1),
                        users.getString(2),
                        users.getString(3),
                        users.getInt(4),
                        users.getString(5));
                return "student-dash";      // 1.2.4 Student Dash
            }
        }else if (userID.startsWith("1")){
            // 1.2.1 Getting the userId
            users= mysqlConnector.execute("SELECT * FROM Club_Advisor WHERE advisorid="+userID);
            if(!(users.next())){
                return null;
            }else if(users.getString(5).equals(passWord)) {
                MainApp.user=new ClubAdvisor(users.getString(1),
                        users.getString(2),
                        users.getString(3),
                        users.getString(4));
                return "clubAdvisor-dash";      // 1.2.2 Advisor Dash
            }
        }
        return null;
    }

    public void switchToDashBoard(ActionEvent event, String dash) throws IOException {
        FXMLLoader root =new FXMLLoader(MainApp.class.getResource("fxmls/"+dash+".fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root.load(), 990, 660);
        stage.setScene(scene);
        stage.show();
    }

    public String getUserID() {
        return userID;
    }
}
