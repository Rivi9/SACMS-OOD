package com.sacms.sacmsood.Controllers;

import com.sacms.sacmsood.Models.ClubEvent;
import com.sacms.sacmsood.Models.ClubMembershipReportGenerator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static com.sacms.sacmsood.Models.mysqlConnector.execute;

public class ClubAdvisorEventProfileController implements Initializable {
    public SideBarController sideBarController;
    public ClubEvent event;

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

    public void onActionUpdate(ActionEvent event) throws IOException {

    }

    public void onActionDelete() {
        execute("DELETE FROM `events` WHERE `EventID`='"+ event.getEventId() +"'");
        System.out.println(event.getEventId());
        System.out.println("Meka hari");


    }
    public void onActiionReport(ActionEvent event) throws IOException {

        ClubMembershipReportGenerator.generateClubMembershipReport();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sideBarController.setURLs("clubAdvisor-dash","clubAdvisor-clubs","clubAdvisor-events");
        sideBarController.eventBtn.setSelected(true);
    }

}
