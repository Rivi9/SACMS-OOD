package com.sacms.sacmsood.Controllers;

import com.sacms.sacmsood.MainApp;
import com.sacms.sacmsood.Models.*;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static com.sacms.sacmsood.Models.mysqlConnector.execute;

public class ClubAdvisorDashController implements Initializable {
    public Text welcomeText,clubText;
    public HBox clubShortHbox;
    public AnchorPane clubAllPane,eventPane;
    public VBox mainContentVbox,sideBar;
    public ScrollPane mainScrollpane;
    public ListController clubAllPaneController,eventPaneController;
    public SideBarController sideBarController;
    private ClubAdvisor advisor =(ClubAdvisor) MainApp.user;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        include in all
        sideBarController.setURLs("clubAdvisor-dash","clubAdvisor-clubs","clubAdvisor-events");
        sideBarController.homeBtn.setSelected(true);
        advisor.getEvents().clear();
        advisor.getClubs().clear();
        clubShortHbox.managedProperty().bind(clubShortHbox.visibleProperty());
        clubAllPane.managedProperty().bind(clubAllPane.visibleProperty());
        clubAllPane.visibleProperty().bind(clubShortHbox.visibleProperty().not());
        welcomeText.setText("Welcome Back, "+ advisor.getfName()+" !!!");
        ResultSet clubs=execute("SELECT * FROM clubs WHERE `AdvisorID`="+ advisor.getId());
        try{
            int count=0;
            while(clubs.next()){
                Club club=new Club(clubs.getString(1),
                        clubs.getString(2),
                        clubs.getString(3),
                        advisor);
                advisor.addClub(club);

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
                                Club club=advisor.peekClubs(((AnchorPane)mouseEvent.getSource()).getAccessibleText());
                                FXMLLoader root =new FXMLLoader(MainApp.class.getResource("fxmls/clubAdvisor-clubProfile.fxml"));
                                Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
                                Scene scene = new Scene(root.load(), 990, 660);
                                stage.setScene(scene);
                                ClubAdvisorClubProfileController clubAdvisorClubProfileController =root.getController();
                                clubAdvisorClubProfileController.club=club;
                                clubAdvisorClubProfileController.init();
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
                event.getClub().addEvent(event);
                advisor.addEvent(event);


            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        viewEvents();
    }

    public void viewClubs() throws SQLException {
        if (clubShortHbox.isVisible()){
            clubShortHbox.setVisible(false);
            clubText.setText("Collapse");
            clubAllPaneController.listVbox.getChildren().clear();

            clubAllPaneController.mainPane.getChildren().addAll(
                    clubAllPaneController.createField(42,75,76,"Name","table_col"),
                    clubAllPaneController.createField(320,75,150,"Number of students","table_col"),
                    clubAllPaneController.createField(570,75,150,"Number of events","table_col"));

            ResultSet studentCount=execute("SELECT COUNT(`StudentID`),`ClubID` FROM student_clubs GROUP BY `ClubID`");

            for (int i = 0; i < advisor.getClubs().size(); i++) {
                int count=0;
                while(studentCount.next()){
                    if(studentCount.getString(2)==advisor.getClubs().get(i).getClubId()){
                        count=studentCount.getInt(1);
                    }
                }
                clubAllPaneController.listVbox.getChildren().add(clubAllPaneController.createRecord(
                        advisor.getClubs().get(i).getClubId(),
                        new String[]{advisor.getClubs().get(i).getClubName(),
                        String.valueOf(count),
                        String.valueOf(advisor.getClubs().get(i).getEvents().size())},
                        new int[]{14,364,599},
                        new int[]{150,150,100}));
                clubAllPaneController.records.getLast().setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        try {
                            Club club=advisor.peekClubs(((AnchorPane)mouseEvent.getSource()).getAccessibleText());
                            FXMLLoader root =new FXMLLoader(MainApp.class.getResource("fxmls/clubAdvisor-clubProfile.fxml"));
                            Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
                            Scene scene = new Scene(root.load(), 990, 660);
                            stage.setScene(scene);
                            ClubAdvisorClubProfileController clubAdvisorClubProfileController =root.getController();
                            clubAdvisorClubProfileController.club=club;
                            clubAdvisorClubProfileController.init();
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
                eventPaneController.createField(643,75,76,"Password","table_col"));

        for (int i = 0; i < advisor.getEvents().size(); i++) {
            eventPaneController.listVbox.getChildren().add(eventPaneController.createRecord(
                    advisor.getEvents().get(i).getEventId(),
                    new String[]{
                            advisor.getEvents().get(i).getEventName(),
                            advisor.getEvents().get(i).getClub().getClubName(),
                            advisor.getEvents().get(i).getDate().toString(),
                            advisor.getEvents().get(i).getLocation(),
                            advisor.getEvents().get(i).getPassword()},
                    new int[]{14,169,327,495,625},
                    new int[]{150,150,150,150,100}));

            eventPaneController.records.getLast().setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    try {
                        ClubEvent event=advisor.peekEvents(((AnchorPane)mouseEvent.getSource()).getAccessibleText());
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
    }

}
