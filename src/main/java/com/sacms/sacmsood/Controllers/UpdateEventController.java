package com.sacms.sacmsood.Controllers;

import com.sacms.sacmsood.MainApp;
import com.sacms.sacmsood.Models.Club;
import com.sacms.sacmsood.Models.ClubAdvisor;
import com.sacms.sacmsood.Models.ClubEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static com.sacms.sacmsood.Models.mysqlConnector.execute;

public class UpdateEventController implements Initializable {
    public ClubEvent event;
    private ClubAdvisor advisor =(ClubAdvisor) MainApp.user;
    @FXML
    private Text welcomeText;

    @FXML
    private Button UpdateEventBtn;

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
    private ComboBox<String> eventType;

    @FXML
    private Text eventCreated;

    public SideBarController sideBarController;

    @FXML
    private Label error;

    @FXML
    public void onUpdateBtnClick(ActionEvent event) throws IOException {

        // Retrieve data from the form
        String name = eventName.getText();
        Club selectedClub = clubName.getValue();

        // Check if a club is selected
        if (selectedClub == null) {
            error.setVisible(true);
            return; // Exit the method without proceeding
        }

        String clubId = selectedClub.getClubId();

        // Retrieve day, month, and year from the respective TextFields

        String day = eventDate.getText();
        String month = eventMonth.getText();
        String year = eventYear.getText();

        // Format the date for database insertion
        String date = year + "-" + month + "-" + day;

        String startHour = startTime.getText();
        String startMinute = startTimeMinutes.getText();

        String endHour = endTime.getText();
        String endMinute = endTimeMinutes.getText();

        // Format the start and end times for database insertion
        String start = startHour + ":" + startMinute;
        String end = endHour + ":" + endMinute;
        String loc = location.getText();
        String desc = description.getText();
        String password = eventPassword.getText();
        String type = eventType.getValue();

        // Check if any of the required fields is empty
        if (name.isEmpty() || day.isEmpty() || month.isEmpty() || year.isEmpty() ||
                startHour.isEmpty() || startMinute.isEmpty() || endHour.isEmpty() || endMinute.isEmpty() ||
                loc.isEmpty() || desc.isEmpty() || password.isEmpty() || type.isEmpty()) {
            error.setVisible(true); // Show the error label
        } else {
            error.setVisible(false); // Hide the error label

            // Insert data into the database
            execute("DELETE FROM `events` WHERE `EventID`='"+this.event.getEventId()+"'");
            execute("INSERT INTO `events`(`EventName`, `ClubID`, `EventDate`, `StartTime`, `EndTime`, `Location`, " +
                    "`Description`, `EventPassword`, `EventType`) VALUES ('" + name + "','" + clubId + "','" + date + "', '" +
                    start + "', '" + end + "','" + loc + "', '" + desc + "','" + password + "', '" + type + "')");

            FXMLLoader root = new FXMLLoader(MainApp.class.getResource("fxmls/clubAdvisor-events.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root.load(), 990, 660);
            stage.setScene(scene);
            stage.show();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sideBarController.setURLs("clubAdvisor-dash", "clubAdvisor-clubs", "clubAdvisor-events");
        sideBarController.eventBtn.setSelected(true);

        for (int i = 0; i < advisor.getClubs().size(); i++) {
            clubName.getItems().add(advisor.getClubs().get(i));
        }

        eventType.getItems().addAll("Event","Meeting","Activity");

    }

    public void init() {
    }
}
