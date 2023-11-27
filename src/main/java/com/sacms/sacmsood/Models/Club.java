package com.sacms.sacmsood.Models;

import java.util.ArrayList;
import java.util.List;
public class Club {

    private String clubId;
    private String clubName;
    private ClubAdvisor advisor;
    private String description;
    private List<Student> members;
    private List<ClubEvent> events;

    public Club(String clubId, String clubName, String description, ClubAdvisor advisor) {
        this.clubId = clubId;
        this.clubName = clubName;
        this.description=description;
        this.advisor=advisor;
        this.members = new ArrayList<>();
        this.events = new ArrayList<>();
    }

    public String getClubId() {
        return clubId;
    }

    public String getClubName() {
        return clubName;
    }

    public List<Student> getMembers() {
        return members;
    }

    public List<ClubEvent> getEvents() {
        return events;
    }

    public void setClubId(String clubId) {
        this.clubId = clubId;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public void setMembers(List<Student> members) {
        this.members = members;
    }

    public void setEvents(List<ClubEvent> events) {
        this.events = events;
    }

    public void addMember(Student student) {
        members.add(student);
    }

    public void removeMember(Student student) {
        members.remove(student);
    }

    public void scheduleEvent(ClubEvent event) {
        events.add(event);
    }


    public ClubAdvisor getAdvisor() {
        return advisor;
    }

    public void setAdvisor(ClubAdvisor advisor) {
        this.advisor = advisor;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}


