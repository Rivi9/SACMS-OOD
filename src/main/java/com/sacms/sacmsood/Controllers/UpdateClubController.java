package com.sacms.sacmsood.Controllers;

import com.sacms.sacmsood.MainApp;
import com.sacms.sacmsood.Models.Club;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static com.sacms.sacmsood.Models.mysqlConnector.execute;

public class UpdateClubController implements Initializable {
    public SideBarController sideBarController;
    public Club club;

    @FXML
    private TextField updatedClubName;

    @FXML
    private TextField updatedClubDescription;

    @FXML
    private Button updateBtn;

    public void init(){
        String id = club.getClubId();
        String name = club.getClubName();
        String Description = club.getDescription();

        updatedClubName.setText(name);
        updatedClubDescription.setText(Description);
    }

    @FXML
    private void onUpdateBtnClick(ActionEvent event) throws IOException {
        execute("DELETE FROM `clubs` WHERE `ClubID`='"+ club.getClubId() +"'");
        execute("INSERT INTO `clubs`(`ClubID`,`ClubName`, `Description`, `AdvisorID`) VALUES ('"+club.getClubId()+"','"+updatedClubName.getText()+"','"+updatedClubDescription.getText()+"','"+club.getAdvisor().getId()+"')");
        FXMLLoader root =new FXMLLoader(MainApp.class.getResource("fxmls/clubAdvisor-clubs.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root.load(), 990, 660);
        stage.setScene(scene);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sideBarController.setURLs("clubAdvisor-dash","clubAdvisor-clubs","clubAdvisor-events");
        sideBarController.clubBtn.setSelected(true);
    }
}
