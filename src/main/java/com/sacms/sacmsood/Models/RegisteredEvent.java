package com.sacms.sacmsood.Models;

import java.sql.Time;
import java.util.Date;

public class RegisteredEvent extends ClubEvent{
    private boolean attended;

    public RegisteredEvent(String eventId, String eventName, Date date, Club club, Time startTime, Time endTime, String location, String description, String password, boolean attended) {
        super(eventId, eventName, date, club, startTime, endTime, location, description, password);
        this.attended = attended;
    }

    public boolean isAttended() {
        return attended;
    }

    public void setAttended(boolean attended) {
        this.attended = attended;
    }
}
