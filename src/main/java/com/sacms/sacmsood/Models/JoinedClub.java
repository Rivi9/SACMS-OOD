package com.sacms.sacmsood.Models;

import java.util.Date;

public class JoinedClub extends Club{
    private Date joinedDate;

    public JoinedClub(String clubId, String clubName, String description, ClubAdvisor advisor,Date joinedDate) {
        super(clubId, clubName, description, advisor);
        this.joinedDate=joinedDate;
    }

    public Date getJoinedDate() {
        return joinedDate;
    }

    public void setJoinedDate(Date joinedDate) {
        this.joinedDate = joinedDate;
    }

}
