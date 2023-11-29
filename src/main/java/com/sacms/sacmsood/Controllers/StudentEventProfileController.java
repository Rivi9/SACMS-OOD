package com.sacms.sacmsood.Controllers;

import com.sacms.sacmsood.Models.ClubEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class StudentEventProfileController implements Initializable {
    public ClubEvent event;
    public SideBarController sideBarController;

    @FXML
    private Text eventName;

    @FXML
    private Text type;

    @FXML
    private Text location;

    @FXML
    private Text time;

    @FXML
    private Text club;

    @FXML
    private Text description;


    public void init() {
        eventName.setText(event.getEventName());
        type.setText("Type : "+event.getType());
        club.setText("Club : "+event.getClub());
        location.setText(event.getLocation());
        description.setText(event.getDescription());
        time.setText("Date : "+event.getDate()+" ("+event.getStartTime()+" - "+event.getEndTime()+")");

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sideBarController.setURLs("student-dash","student-clubs","student-events");
        sideBarController.eventBtn.setSelected(true);
    }
}
