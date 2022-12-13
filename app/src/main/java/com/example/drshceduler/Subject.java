package com.example.drshceduler;

public class Subject {
    private String Cabinet;
    private String Subject;
    private String Teacher;
    private String Day;
    private String Type;
    private String Week;
    private String Link;
    private String Time;
    private String Number;

    public Subject() {
    }


    public String getCabinet() {
        return Cabinet;
    }

    public String getSubject() {
        return Subject;
    }

    public String getTeacher() {
        return Teacher;
    }

    public String getDay() {
        return Day;
    }

    public String getWeek() {
        return Week;
    }

    public String getLink() {
        return Link;
    }

    public String getTime() {
        return Time;
    }

    public String getType() {
        return Type;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }
}
