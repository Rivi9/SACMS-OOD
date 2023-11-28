package com.sacms.sacmsood.Models;

import java.util.ArrayList;

public abstract class User {
    private String id;
    private String fName;
    private String lName;
    private String email;
    private ArrayList<Club> clubs =new ArrayList<Club>();
    private ArrayList<ClubEvent> events=new ArrayList<ClubEvent>();

    public ArrayList<Club> getClubs() {
        return clubs;
    }

    public void setClubs(ArrayList<Club> clubs) {
        this.clubs = clubs;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<ClubEvent> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<ClubEvent> events) {
        this.events = events;
    }

    public void addEvent(ClubEvent event){
        events.add(event);
    }

    public void removeEvent(ClubEvent event){
        events.remove(event);
    }

    public ClubEvent peekEvents(String id){
        for (int i = 0; i < events.size(); i++) {
            if(events.get(i).getEventId().equals(id)){
                return events.get(i);
            }
        }
        return null;
    }

    public void addClub(Club club){
        clubs.add(club);
    }

    public void removeClub(Club club){
        clubs.remove(club);
    }

    public Club peekClubs(String id){
        for (int i = 0; i < clubs.size(); i++) {
            if(clubs.get(i).getClubId().equals(id)){
                return clubs.get(i);
            }
        }
        return null;
    }


}
