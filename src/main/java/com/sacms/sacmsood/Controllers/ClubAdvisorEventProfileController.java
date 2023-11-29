package com.sacms.sacmsood.Controllers;

import com.sacms.sacmsood.MainApp;
import com.sacms.sacmsood.Models.AttendanceReportGenerator;
import com.sacms.sacmsood.Models.ClubEvent;
import com.sacms.sacmsood.Models.Student;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
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

public class ClubAdvisorEventProfileController implements Initializable {
    public SideBarController sideBarController;
    public ClubEvent event;
    private ArrayList<Student> studentArrayList =new ArrayList<Student>();
    public AnchorPane studentPane;
    public ListController studentPaneController;

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
        viewStudents();
    }

    private void viewStudents() {
        studentPaneController.listVbox.getChildren().clear();

        studentPaneController.mainPane.getChildren().addAll(
                studentPaneController.createField(35,75,76,"Name","table_col"),
                studentPaneController.createField(350,75,76,"Level","table_col"),
                studentPaneController.createField(625,75,76,"Attendance","table_col"));

        ResultSet students=execute("SELECT * FROM student_events as e " +
                "JOIN student as s " +
                "ON e.`StudentID`=s.`StudentID` " +
                "WHERE e.`EventID`="+event.getEventId());
        try{
            while(students.next()){
                Student student=new Student(students.getString(4),
                        students.getString(5),
                        students.getString(6),
                        students.getInt(7),
                        students.getString(8));
                studentArrayList.add(student);

                studentPaneController.listVbox.getChildren().add(studentPaneController.createRecord(
                        studentArrayList.getLast().getId(),
                        new String[]{
                                studentArrayList.getLast().getfName()+" "+studentArrayList.getLast().getlName(),
                                String.valueOf(studentArrayList.getLast().getLevel()),
                                students.getBoolean(3) ? "Attended":"Absent"},
                        new int[]{14,327,600},
                        new int[]{150,150,100}));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    public void onActionUpdate(ActionEvent event) throws IOException {
        FXMLLoader root =new FXMLLoader(MainApp.class.getResource("fxmls/update_event.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root.load(), 990, 660);
        stage.setScene(scene);
        UpdateEventController updateEventController =root.getController();
        updateEventController.event=this.event;
        updateEventController.init();
        stage.show();
    }


    public void onActionDelete(ActionEvent event) throws IOException {
        execute("DELETE FROM `events` WHERE `EventID`='"+this.event.getEventId()+"'");
        FXMLLoader root =new FXMLLoader(MainApp.class.getResource("fxmls/clubAdvisor-events.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root.load(), 990, 660);
        stage.setScene(scene);
        stage.show();

    }

    public void onActionReport(){
        AttendanceReportGenerator.generateAttendanceReport();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sideBarController.setURLs("clubAdvisor-dash","clubAdvisor-clubs","clubAdvisor-events");
        sideBarController.eventBtn.setSelected(true);
    }

}
