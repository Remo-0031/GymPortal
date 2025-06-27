package com.example.gymmembership;

import java.sql.Date;

public class member {
    private int ID;
    private String fullName;
    private Date birthDate;
    private Date joinDate;
    private Date dueDate;
    private boolean student;
    private boolean withTrainer;

    public member(int ID, String fullName, Date birthDate, Date joinDate, Date dueDate, boolean student, boolean withTrainer) {
        this.ID = ID;
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.joinDate = joinDate;
        this.dueDate = dueDate;
        this.student = student;
        this.withTrainer = withTrainer;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }


    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isStudent() {
        return this.student;
    }

    public void setStudent(boolean student) {
        this.student = student;
    }

    public boolean isWithTrainer() {
        return withTrainer;
    }

    public void setWithTrainer(boolean withTrainer) {
        this.withTrainer = withTrainer;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
