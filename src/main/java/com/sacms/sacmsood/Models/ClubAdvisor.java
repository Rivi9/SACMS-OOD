package com.sacms.sacmsood.Models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ClubAdvisor extends User{
    private static ArrayList<ClubAdvisor> advisorList=new ArrayList<ClubAdvisor>();

    public ClubAdvisor(String id, String fName, String lName, String email) {
        this.setId(id);
        this.setfName(fName);
        this.setlName(lName);
        this.setEmail(email);
        addAdvisor(this);
    }

    public void markAttendance(ClubEvent event, List<Student> presentStudents) {
        // Attendance tracking code
    }

    public void generateReport(ClubEvent event) {
        // Report generating code
    }

    public static ArrayList<ClubAdvisor> getAdvisorList() {
        return advisorList;
    }

    public static void setAdvisorList(ArrayList<ClubAdvisor> advisorList) {
        ClubAdvisor.advisorList = advisorList;
    }

    public static void addAdvisor(ClubAdvisor advisor){
        advisorList.add(advisor);
    }
}


