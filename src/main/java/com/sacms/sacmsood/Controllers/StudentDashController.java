package com.sacms.sacmsood.Controllers;

import com.sacms.sacmsood.MainApp;
import com.sacms.sacmsood.Models.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static com.sacms.sacmsood.Models.mysqlConnector.execute;

public class StudentDashController implements Initializable {
    public Text welcomeText,clubText;
    public HBox clubShortHbox;
    public AnchorPane clubAllPane,eventPane;
    public VBox mainContentVbox,sideBar,popup;
    public ScrollPane mainScrollpane;
    public ListController clubAllPaneController,eventPaneController;
    public SideBarController sideBarController;
    public PopupController popupController;
    private Student student=(Student) MainApp.user;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        student.getEvents().clear();
        student.getClubs().clear();
        popup.setVisible(false);
//        include in all
        sideBarController.setURLs("student-dash","student-clubs","student-events");
        sideBarController.homeBtn.setSelected(true);

        clubShortHbox.managedProperty().bind(clubShortHbox.visibleProperty());
        clubAllPane.managedProperty().bind(clubAllPane.visibleProperty());
        clubAllPane.visibleProperty().bind(clubShortHbox.visibleProperty().not());
        welcomeText.setText("Welcome Back, "+student.getfName()+" !!!");
        ResultSet clubs=execute("SELECT * FROM clubs as c " +
                "JOIN student_clubs as s " +
                "ON s.`ClubID` = c.`ClubID` " +
                "WHERE s.`StudentID`="+student.getId());
        try{
            int count=0;
            while(clubs.next()){
                ResultSet advisors=execute("SELECT * FROM club_advisor as a " +
                        "JOIN clubs as c " +
                        "ON a.`AdvisorID`=c.`AdvisorID` " +
                        "WHERE c.`AdvisorID`="+clubs.getString(4));
                advisors.next();
                Club club=new JoinedClub(clubs.getString(1),
                        clubs.getString(2),
                        clubs.getString(3),
                        new ClubAdvisor(advisors.getString(1),
                                advisors.getString(2),
                                advisors.getString(3),
                                advisors.getString(4)),
                        clubs.getDate(7));

                student.addClub(club);

                if(count<4){
                    AnchorPane clubShortBox =new AnchorPane();
                    clubShortBox.getStyleClass().add("club");
                    clubShortBox.setPrefWidth(145);
                    clubShortBox.setAccessibleText(club.getClubId());

                    Text clubShortText=new Text();
                    clubShortText.getStyleClass().add("club_name");
                    clubShortText.setText(club.getClubName());
                    clubShortText.setLayoutX(14);
                    clubShortText.setLayoutY(36);
                    clubShortText.setWrappingWidth(151);
                    clubShortBox.getChildren().add(clubShortText);
                    clubShortHbox.getChildren().add(clubShortBox);
                    count++;
                    clubShortBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent mouseEvent) {
                            try {
                                Club club=student.peekClubs(((AnchorPane)mouseEvent.getSource()).getAccessibleText());
                                FXMLLoader root =new FXMLLoader(MainApp.class.getResource("fxmls/student-clubProfile.fxml"));
                                Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
                                Scene scene = new Scene(root.load(), 990, 660);
                                stage.setScene(scene);
                                StudentClubProfileController studentClubProfileController =root.getController();
                                studentClubProfileController.club=club;
                                studentClubProfileController.init();
                                stage.show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        long millis=System.currentTimeMillis();
//        System.out.println(new Date(millis));
        ResultSet events=execute("SELECT * FROM events as e " +
                "JOIN student_events as s " +
                "ON s.`EventID` = e.`EventID` " +
                "WHERE s.`StudentID`="+student.getId()+" AND s.`Attendence`=0"+
                " ORDER BY `EventDate`");
        try{
            while(events.next()){
                if(events.getDate(4).after(new Date(millis))){
                    ClubEvent event=new RegisteredEvent(events.getString(1),
                            events.getString(2),
                            events.getDate(4),
                            student.peekClubs(events.getString(3)),
                            events.getTime(5),
                            events.getTime(6),
                            events.getString(7),
                            events.getString(8),
                            events.getString(9),
                            events.getString(10),
                            events.getBoolean(13));
                    student.addEvent(event);
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        popupController.popupOk.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(student.peekEvents(popupController.getId()).getPassword().equals(popupController.passwordField.getText())){
                    ((RegisteredEvent)student.peekEvents(popupController.getId())).setAttended(true);
                    popup.setVisible(false);
                    eventPaneController.peekRecord(popupController.getId()).setVisible(false);

                    execute("UPDATE student_events " +
                            "SET `Attendence`=TRUE " +
                            "WHERE `StudentID`="+student.getId()+" AND `EventID`="+popupController.getId());
                }else{
                    popupController.errorMsg.setVisible(true);
                }
            }
        });
        popupController.popupCancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                popup.setVisible(false);
            }
        });
        viewEvents();
    }

    public void viewClubs() {
        if (clubShortHbox.isVisible()){
            clubShortHbox.setVisible(false);
            clubText.setText("Collapse");
            clubAllPaneController.listVbox.getChildren().clear();

            clubAllPaneController.mainPane.getChildren().addAll(
                    clubAllPaneController.createField(42,75,76,"Name","table_col"),
                    clubAllPaneController.createField(355,75,76,"Advisor","table_col"),
                    clubAllPaneController.createField(615,75,76,"Joined Date","table_col"));

            for (int i = 0; i < student.getClubs().size(); i++) {
                clubAllPaneController.listVbox.getChildren().add(clubAllPaneController.createRecord(
                        student.getClubs().get(i).getClubId(),
                        new String[]{student.getClubs().get(i).getClubName(),
                        student.getClubs().get(i).getAdvisor().getfName()+" "+student.getClubs().get(i).getAdvisor().getlName(),
                        ((JoinedClub)student.getClubs().get(i)).getJoinedDate().toString()},
                        new int[]{14,327,599},
                        new int[]{150,150,100}));
                clubAllPaneController.records.getLast().setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        try {
                            Club club=student.peekClubs(((AnchorPane)mouseEvent.getSource()).getAccessibleText());
                            FXMLLoader root =new FXMLLoader(MainApp.class.getResource("fxmls/student-clubProfile.fxml"));
                            Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
                            Scene scene = new Scene(root.load(), 990, 660);
                            stage.setScene(scene);
                            StudentClubProfileController studentClubProfileController =root.getController();
                            studentClubProfileController.club=club;
                            studentClubProfileController.init();
                            stage.show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }else{
            clubShortHbox.setVisible(true);
            clubText.setText("View all");
        }
    }

    public void viewEvents(){
        eventPaneController.listVbox.getChildren().clear();

        eventPaneController.mainPane.getChildren().addAll(
                eventPaneController.createField(36,75,76,"Name","table_col"),
                eventPaneController.createField(200,75,76,"Club","table_col"),
                eventPaneController.createField(355,75,76,"Date","table_col"),
                eventPaneController.createField(515,75,76,"Location","table_col"),
                eventPaneController.createField(643,75,76,"Attendance","table_col"));

        for (int i = 0; i < student.getEvents().size(); i++) {
            eventPaneController.listVbox.getChildren().add(eventPaneController.createRecord(
                    student.getEvents().get(i).getEventId(),
                    new String[]{
                    student.getEvents().get(i).getEventName(),
                    student.getEvents().get(i).getClub().getClubName(),
                    student.getEvents().get(i).getDate().toString(),
                    student.getEvents().get(i).getLocation()},
                    new int[]{14,169,327,495},
                    new int[]{150,150,150,150}));
            Button btn=eventPaneController.createButton(630,10,"Check In","btn",student.getEvents().get(i).getEventId());
            btn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    popup(btn.getAccessibleText());
                }
            });

            eventPaneController.records.getLast().setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    try {
                        ClubEvent event=student.peekEvents(((AnchorPane)mouseEvent.getSource()).getAccessibleText());
                        FXMLLoader root =new FXMLLoader(MainApp.class.getResource("fxmls/student-eventProfile.fxml"));
                        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
                        Scene scene = new Scene(root.load(), 990, 660);
                        stage.setScene(scene);
                        StudentEventProfileController studentEventProfileController =root.getController();
                        studentEventProfileController.event=event;
                        studentEventProfileController.init();
                        stage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            eventPaneController.records.getLast().getChildren().add(btn);
        }
    }

    public void popup(String id){
        popup.setVisible(true);
        popupController.errorMsg.setVisible(false);
        popupController.passwordField.clear();
        popupController.setId(id);
    }

}