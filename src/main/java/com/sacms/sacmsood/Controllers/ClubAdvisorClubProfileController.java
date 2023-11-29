package com.sacms.sacmsood.Controllers;

import com.sacms.sacmsood.MainApp;
import com.sacms.sacmsood.Models.Club;
import com.sacms.sacmsood.Models.ClubActivitiesReportGenerator;
import com.sacms.sacmsood.Models.ClubEvent;
import com.sacms.sacmsood.Models.ClubMembershipReportGenerator;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static com.sacms.sacmsood.Models.mysqlConnector.execute;

public class ClubAdvisorClubProfileController implements Initializable {
    public Club club;
    public SideBarController sideBarController;
    private ArrayList<ClubEvent> eventArrayList =new ArrayList<ClubEvent>();
    public AnchorPane eventPane;
    public ListController eventPaneController;
    @FXML
    private Text clubName;

    @FXML
    private Text clubAdvisor;

    @FXML
    private Text Description;

    @FXML
    private Button update;

    @FXML
    private Button delete;

    @FXML
    private Button report1;

    @FXML
    private Button report2;

    public void update(ActionEvent event) throws IOException {
        FXMLLoader root =new FXMLLoader(MainApp.class.getResource("fxmls/update_club.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root.load(), 990, 660);
        stage.setScene(scene);
        UpdateClubController updateClubController =root.getController();
        updateClubController.club=club;
        updateClubController.init();
        stage.show();
    }

    public void delete(ActionEvent event) throws IOException {
        execute("DELETE FROM `clubs` WHERE `ClubID`='"+ club.getClubId() +"'");
        FXMLLoader root =new FXMLLoader(MainApp.class.getResource("fxmls/clubAdvisor-clubs.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root.load(), 990, 660);
        stage.setScene(scene);
        stage.show();

    }



    public void report1(ActionEvent event) throws IOException {

        ClubMembershipReportGenerator.generateClubMembershipReport();

        FXMLLoader root =new FXMLLoader(MainApp.class.getResource("fxmls/clubAdvisor-clubs.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root.load(), 990, 660);
        stage.setScene(scene);
        stage.show();
    }
    public void report2(ActionEvent event) throws IOException {
//        generateClubAvtivities
        ClubActivitiesReportGenerator.generateClubAvtivities();
        FXMLLoader root =new FXMLLoader(MainApp.class.getResource("fxmls/create_club.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root.load(), 990, 660);
        stage.setScene(scene);
        stage.show();
    }


    public void init(){
        clubName.setText(club.getClubName());
        clubAdvisor.setText("Club advisor : "+club.getAdvisor().getfName()+" "+club.getAdvisor().getlName());
        Description.setText(club.getDescription());
        report1.setTooltip(new Tooltip("Generate Club Activities Report"));
        viewEvents();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sideBarController.setURLs("clubAdvisor-dash","clubAdvisor-clubs","clubAdvisor-events");
        sideBarController.clubBtn.setSelected(true);
    }

    private void viewEvents() {
        eventPaneController.listVbox.getChildren().clear();

        eventPaneController.mainPane.getChildren().addAll(
                eventPaneController.createField(35,75,76,"Name","table_col"),
                eventPaneController.createField(350,75,76,"Type","table_col"),
                eventPaneController.createField(625,75,76,"Date","table_col"));

        ResultSet events=execute("SELECT * FROM events " +
                "WHERE `ClubID`="+club.getClubId());
        try{
            while(events.next()){
                ClubEvent event=new ClubEvent(events.getString(1),
                        events.getString(2),
                        events.getDate(4),
                        club,
                        events.getTime(5),
                        events.getTime(6),
                        events.getString(7),
                        events.getString(8),
                        events.getString(9),
                        events.getString(10));
                event.getClub().addEvent(event);
                eventArrayList.add(event);

                eventPaneController.listVbox.getChildren().add(eventPaneController.createRecord(
                        eventArrayList.getLast().getEventId(),
                        new String[]{
                                eventArrayList.getLast().getEventName(),
                                eventArrayList.getLast().getType(),
                                eventArrayList.getLast().getDate().toString()},
                        new int[]{14,327,600},
                        new int[]{150,150,100}));
                eventPaneController.records.getLast().setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        try {
                            ClubEvent event = null;
                            event=club.peekEvents(((AnchorPane)mouseEvent.getSource()).getAccessibleText());

                            FXMLLoader root =new FXMLLoader(MainApp.class.getResource("fxmls/clubAdvisor-eventProfile.fxml"));
                            Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
                            Scene scene = new Scene(root.load(), 990, 660);
                            stage.setScene(scene);
                            ClubAdvisorEventProfileController clubAdvisorEventProfileController =root.getController();
                            clubAdvisorEventProfileController.event=event;
                            clubAdvisorEventProfileController.init();
                            stage.show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

    }


}
