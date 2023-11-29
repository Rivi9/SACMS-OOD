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
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

import static com.sacms.sacmsood.Models.mysqlConnector.execute;

public class StudentClubsController implements Initializable {
    public TextField searchField;
    private Student student=(Student) MainApp.user;
    private ArrayList<Club> clubArrayList= new ArrayList<Club>();
    public AnchorPane clubPane;
    public SideBarController sideBarController;
    public ListController clubPaneController;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        clubArrayList.clear();
        sideBarController.setURLs("student-dash","student-clubs","student-events");
        sideBarController.clubBtn.setSelected(true);
        ResultSet clubs=execute("SELECT * FROM clubs " +
                "WHERE `ClubID`!=ALL (SELECT distinct `ClubID` FROM student_clubs WHERE `StudentID`="+student.getId()+")");
        try{
            while(clubs.next()){
                ResultSet advisors=execute("SELECT * FROM club_advisor as a " +
                        "JOIN clubs as c " +
                        "ON a.`AdvisorID`=c.`AdvisorID` " +
                        "WHERE c.`AdvisorID`="+clubs.getString(4));
                advisors.next();
                Club club=new Club(clubs.getString(1),
                        clubs.getString(2),
                        clubs.getString(3),
                        new ClubAdvisor(advisors.getString(1),
                                advisors.getString(2),
                                advisors.getString(3),
                                advisors.getString(4)));
                clubArrayList.add(club);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        viewClubs(clubArrayList);
    }
    public void viewClubs(ArrayList<Club> clubArrayList){
        clubPaneController.listVbox.getChildren().clear();

        clubPaneController.mainPane.getChildren().addAll(
                clubPaneController.createField(42,75,76,"Name","table_col"),
                clubPaneController.createField(391,75,76,"Advisor","table_col"),
                clubPaneController.createField(650,75,76,"Join","table_col"));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date currentDate = new Date();
        String formattedDate = dateFormat.format(currentDate);
        for (int i = 0; i < clubArrayList.size(); i++) {
            clubPaneController.listVbox.getChildren().add(clubPaneController.createRecord(
                    clubArrayList.get(i).getClubId(),
                    new String[]{
                            clubArrayList.get(i).getClubName(),
                            clubArrayList.get(i).getAdvisor().getfName()+clubArrayList.get(i).getAdvisor().getlName()},
                    new int[]{14,364},
                    new int[]{150,150}));
            Button btn=clubPaneController.createButton(630,10,"Join","btn",clubArrayList.get(i).getClubId());
            btn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {

                    long millis=System.currentTimeMillis();
                    execute("INSERT INTO student_clubs VALUES " +
                            "("+student.getId()+","+btn.getAccessibleText()+","+formattedDate+")");
                    clubPaneController.peekRecord(btn.getAccessibleText()).setVisible(false);
                }
            });
            clubPaneController.records.getLast().setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    try {
                        Club club = null;
                        for (int j = 0; j < clubArrayList.size(); j++) {
                            if(clubArrayList.get(j).getClubId().equals(((AnchorPane)mouseEvent.getSource()).getAccessibleText())){
                                club=clubArrayList.get(j);
                            }
                        }

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
            clubPaneController.records.getLast().getChildren().add(btn);
        }
    }

    public void search(MouseEvent mouseEvent) {
        String key=searchField.getText().trim().toLowerCase();
        if (!key.isEmpty()) {
            ArrayList<Club> temp=new ArrayList<Club>();
            for (int i = 0; i < clubArrayList.size(); i++) {
                if(clubArrayList.get(i).getClubName().toLowerCase().contains(key)){
                    temp.add(clubArrayList.get(i));
                }
            }
            viewClubs(temp);
        }else{
            viewClubs(clubArrayList);
        }
    }
}
