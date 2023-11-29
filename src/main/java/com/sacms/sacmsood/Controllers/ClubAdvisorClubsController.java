package com.sacms.sacmsood.Controllers;

import com.sacms.sacmsood.MainApp;
import com.sacms.sacmsood.Models.Club;
import com.sacms.sacmsood.Models.ClubAdvisor;
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
import java.util.ResourceBundle;

import static com.sacms.sacmsood.Models.mysqlConnector.execute;

public class ClubAdvisorClubsController implements Initializable {


    public SideBarController sideBarController;
    public AnchorPane clubPane;
    public Button createBtn;
    public ListController clubPaneController;
    private ClubAdvisor advisor =(ClubAdvisor) MainApp.user;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sideBarController.setURLs("clubAdvisor-dash","clubAdvisor-clubs","clubAdvisor-events");
        sideBarController.clubBtn.setSelected(true);

        advisor.getClubs().clear();

        ResultSet clubs=execute("SELECT * FROM clubs WHERE `AdvisorID`="+ advisor.getId());
        try{
            int count=0;
            while(clubs.next()){
                Club club=new Club(clubs.getString(1),
                        clubs.getString(2),
                        clubs.getString(3),
                        advisor);
                advisor.addClub(club);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        viewClubs();
    }

    public void onCreateBtnClick(ActionEvent event) throws IOException {
            FXMLLoader root =new FXMLLoader(MainApp.class.getResource("fxmls/create_club.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root.load(), 990, 660);
            stage.setScene(scene);
            stage.show();
    }

    public void viewClubs(){
        try {
            clubPaneController.listVbox.getChildren().clear();

            clubPaneController.mainPane.getChildren().addAll(
                    clubPaneController.createField(42, 75, 76, "Name", "table_col"),
                    clubPaneController.createField(320, 75, 150, "Number of students", "table_col"),
                    clubPaneController.createField(570, 75, 150, "Number of events", "table_col"));

            ResultSet studentCount = execute("SELECT COUNT(`StudentID`),`ClubID` FROM student_clubs GROUP BY `ClubID`");

            for (int i = 0; i < advisor.getClubs().size(); i++) {
                int count = 0;
                while (studentCount.next()) {
                    if (studentCount.getString(2).equals(advisor.getClubs().get(i).getClubId())) {
                        count = studentCount.getInt(1);
                    }
                }
                clubPaneController.listVbox.getChildren().add(clubPaneController.createRecord(
                        advisor.getClubs().get(i).getClubId(),
                        new String[]{advisor.getClubs().get(i).getClubName(),
                                String.valueOf(count),
                                String.valueOf(advisor.getClubs().get(i).getEvents().size())},
                        new int[]{14, 364, 599},
                        new int[]{150,150,100}));

                clubPaneController.records.getLast().setOnMouseClicked(new EventHandler<MouseEvent>() {
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


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
