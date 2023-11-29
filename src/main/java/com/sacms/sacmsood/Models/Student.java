package com.sacms.sacmsood.Models;

public class Student extends User {
    private int level;

    public Student(String stdId, String fName, String lName, int level, String email){
        this.setId(stdId);
        this.setfName(fName);
        this.setlName(lName);
        this.level = level;
        this.setEmail(email);
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}

