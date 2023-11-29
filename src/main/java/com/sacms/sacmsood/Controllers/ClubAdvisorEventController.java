package com.sacms.sacmsood.Controllers;

import com.sacms.sacmsood.MainApp;
import com.sacms.sacmsood.Models.ClubAdvisor;
import com.sacms.sacmsood.Models.ClubEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static com.sacms.sacmsood.Models.mysqlConnector.execute;

public class ClubAdvisorEventController implements Initializable {
    public SideBarController sideBarController;
    public AnchorPane eventPane;
    public Button createBtn;
    public ListController eventPaneController;
    private ClubAdvisor advisor =(ClubAdvisor) MainApp.user;
    private ArrayList<ClubEvent> eventArrayList =new ArrayList<ClubEvent>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sideBarController.setURLs("clubAdvisor-dash","clubAdvisor-clubs","clubAdvisor-events");
        sideBarController.eventBtn.setSelected(true);
        viewEvents();
    }

    public void onCreateBtnClick(ActionEvent event) throws IOException {
        FXMLLoader root =new FXMLLoader(MainApp.class.getResource("fxmls/create_event.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root.load(), 990, 660);
        stage.setScene(scene);
        stage.show();
    }

    private void viewEvents() {

        eventPaneController.listVbox.getChildren().clear();

        eventPaneController.mainPane.getChildren().addAll(
                eventPaneController.createField(35,75,76,"Name","table_col"),
                eventPaneController.createField(350,75,76,"Club","table_col"),
                eventPaneController.createField(625,75,76,"Date","table_col"));

        ResultSet events=execute("SELECT * FROM events as e " +
                "JOIN clubs as c " +
                "ON e.`ClubID`=c.`ClubID` " +
                "WHERE c.`AdvisorID`="+ advisor.getId());
        try{
            while(events.next()){
                ClubEvent event=new ClubEvent(events.getString(1),
                        events.getString(2),
                        events.getDate(4),
                        advisor.peekClubs(events.getString(3)),
                        events.getTime(5),
                        events.getTime(6),
                        events.getString(7),
                        events.getString(8),
                        events.getString(9),
                        events.getString(10));
                if(event.getClub().peekEvents(event.getEventId())!=null){
                    event.getClub().addEvent(event);
                }
                eventArrayList.add(event);

                eventPaneController.listVbox.getChildren().add(eventPaneController.createRecord(
                        eventArrayList.getLast().getEventId(),
                        new String[]{
                                eventArrayList.getLast().getEventName(),
                                eventArrayList.getLast().getClub().getClubName(),
                                eventArrayList.getLast().getDate().toString()},
                        new int[]{14,327,600},
                        new int[]{150,150,100}));
                eventPaneController.records.getLast().setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        try {
                            ClubEvent event = null;
                            for (int j = 0; j < eventArrayList.size(); j++) {
                                if(eventArrayList.get(j).getEventId().equals(((AnchorPane)mouseEvent.getSource()).getAccessibleText())){
                                    event=eventArrayList.get(j);
                                }
                            }
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
