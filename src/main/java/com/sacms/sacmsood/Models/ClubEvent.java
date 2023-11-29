package com.sacms.sacmsood.Models;

import java.sql.Time;
import java.util.Date;

public class ClubEvent {

    private String eventId;
    private String eventName;
    private Date date;
    private Club club;
    private Time startTime,endTime;
    private String location,description,password,type;

    public ClubEvent(String eventId, String eventName, Date date, Club club, Time startTime, Time endTime, String location, String description, String password, String type) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.date = date;
        this.club = club;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.description = description;
        this.password = password;
        this.type=type;
    }

    public String getEventId() {
        return eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public Date getDate() {
        return date;
    }

    public Club getClub() {
        return club;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setClub(Club club) {
        this.club = club;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

