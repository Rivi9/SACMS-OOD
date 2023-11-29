package com.sacms.sacmsood.Controllers;

import com.sacms.sacmsood.MainApp;
import com.sacms.sacmsood.Models.ClubEvent;
import com.sacms.sacmsood.Models.Student;
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
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static com.sacms.sacmsood.Models.mysqlConnector.execute;

public class StudentEventController implements Initializable {
    private Student student=(Student) MainApp.user;
    private ArrayList<ClubEvent> eventArrayList= new ArrayList<ClubEvent>();
    public AnchorPane eventPane;
    public SideBarController sideBarController;
    public ListController eventPaneController;
    @Override
    // 1. Selects Events
    public void initialize(URL url, ResourceBundle resourceBundle) {
        long millis=System.currentTimeMillis();
        eventArrayList.clear();
        sideBarController.setURLs("student-dash","student-clubs","student-events");
        sideBarController.eventBtn.setSelected(true);
        // 1.1 Execute SQL Query
        // 1.2 ResultSet Events
        ResultSet events=execute("SELECT * FROM events as e " +
                "JOIN student_clubs as s " +
                "ON s.`ClubID` = e.`ClubID` " +
                "WHERE s.`StudentID`="+student.getId()+
                " ORDER BY `EventDate`");
        try{
            while (events.next()) {
                if(events.getDate(4).after(new Date(millis))) {
                    ClubEvent event = new ClubEvent(events.getString(1),
                            events.getString(2),
                            events.getDate(4),
                            student.peekClubs(events.getString(3)),     // 1.3 peekClubs() method
                            events.getTime(5),
                            events.getTime(6),
                            events.getString(7),
                            events.getString(8),
                            events.getString(9),
                            events.getString(10));
                    eventArrayList.add(event);      // 1.5 Add events list
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        viewEvents();       // 1.6 Calling viewEvents() method
    }
    // 1.6 viewEvents() method
    public void viewEvents(){
        eventPaneController.listVbox.getChildren().clear();

        eventPaneController.mainPane.getChildren().addAll(
                eventPaneController.createField(35,75,76,"Name","table_col"),
                eventPaneController.createField(250,75,76,"Club","table_col"),
                eventPaneController.createField(400,75,76,"Date","table_col"),
                eventPaneController.createField(520,75,76,"Location","table_col"),
                eventPaneController.createField(670,75,76,"Register","table_col"));
        try{
            for (int i = 0; i < eventArrayList.size(); i++) {       // 1.6.1 Get event details
                ResultSet eventRecord=execute("SELECT * FROM events as e " +
                        "JOIN student_events as s " +
                        "ON s.`EventID` = e.`EventID` " +
                        "WHERE s.`EventID`="+eventArrayList.get(i).getEventId()+
                        " AND s.`StudentID`="+student.getId()+      // 1.6.2 Get actor's ID
                        " ORDER BY `EventDate`");

                eventPaneController.listVbox.getChildren().add(eventPaneController.createRecord(
                        eventArrayList.get(i).getEventId(),
                        new String[]{
                                eventArrayList.get(i).getEventName(),
                                eventArrayList.get(i).getClub().getClubName(),
                                eventArrayList.get(i).getDate().toString(),
                                eventArrayList.get(i).getLocation()},
                        new int[]{15,230,380,500},
                        new int[]{150,150,150,150}));
                Button btn=eventPaneController.createButton(650,10,"Register","btn",eventArrayList.get(i).getEventId());
                btn.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {     // 1.6.4 & 1.6.5 Execute Query and get results
                        execute("INSERT INTO student_events VALUES " +
                                "("+student.getId()+","+btn.getAccessibleText()+",0)");
                        btn.setText("Registered");
                        btn.setDisable(true);
                    }
                });

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

                            // 1.6.6 Display Events
                            FXMLLoader root =new FXMLLoader(MainApp.class.getResource("fxmls/student-eventProfile.fxml"));
                            Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
                            Scene scene = new Scene(root.load(), 990, 660);
                            stage.setScene(scene);
                            StudentEventProfileController studentEventProfileController =root.getController();
                            studentEventProfileController.event=event;      // 2. Join Events
                            studentEventProfileController.init();
                            stage.show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

                // 2.1 Register student for events
                if(eventRecord.next()){
                    btn.setText("Registered");
                    btn.setDisable(true);}
                eventPaneController.records.getLast().getChildren().add(btn);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
