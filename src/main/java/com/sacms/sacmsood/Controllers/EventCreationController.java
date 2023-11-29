package com.sacms.sacmsood.Controllers;

import com.sacms.sacmsood.MainApp;
import com.sacms.sacmsood.Models.Club;
import com.sacms.sacmsood.Models.ClubAdvisor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static com.sacms.sacmsood.Models.mysqlConnector.execute;

public class EventCreationController implements Initializable {
    private ClubAdvisor advisor =(ClubAdvisor) MainApp.user;
    @FXML
    private Text welcomeText;

    @FXML
    private Button CreateEventBtn;

    @FXML
    private TextField eventName;

    @FXML
    private ComboBox<Club> clubName;

    @FXML
    private TextField eventDate;

    @FXML
    private TextField eventMonth;

    @FXML
    private TextField eventYear;

    @FXML
    private TextField startTime;

    @FXML
    private TextField startTimeMinutes;

    @FXML
    private TextField endTime;

    @FXML
    private TextField endTimeMinutes;

    @FXML
    private TextField location;

    @FXML
    private TextField description;

    @FXML
    private TextField eventPassword;

    @FXML
    private TextField eventType;

    @FXML
    private Text eventCreated;

    public SideBarController sideBarController;

    @FXML
    private void onCreateBtnClick(ActionEvent event) throws IOException {

        String name = eventName.getText();
        String clubId = clubName.getValue().getClubId();


        String day = eventDate.getText();
        String month = eventMonth.getText();
        String year = eventYear.getText();


        String date = year + "-" + month + "-" + day;


        String startHour = startTime.getText();
        String startMinute = startTimeMinutes.getText();

        String endHour = endTime.getText();
        String endMinute = endTimeMinutes.getText();


        String start = startHour + ":" + startMinute;
        String end = endHour + ":" + endMinute;

        String loc = location.getText();
        String desc = description.getText();
        String password = eventPassword.getText();
        String type = eventType.getText();

        // Insert data into the database
        execute("INSERT INTO `events`(`EventName`, `ClubID`, `EventDate`, `StartTime`, `EndTime`, `Location`, " +
                "`Description`, `EventPassword`, `EventType`) VALUES ('" + name + "','" + clubId + "','" + date + "', '" +
                start + "', '" + end + "','" + loc + "', '" + desc + "','" + password + "', '" + type + "')");

        FXMLLoader root = new FXMLLoader(MainApp.class.getResource("fxmls/create_event.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root.load(), 990, 660);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sideBarController.setURLs("create_event", "clubAdvisor-clubs", "clubAdvisor-events");
        sideBarController.eventBtn.setSelected(true);

        for (int i = 0; i < advisor.getClubs().size(); i++) {
            clubName.getItems().add(advisor.getClubs().get(i));
        }

    }
}
