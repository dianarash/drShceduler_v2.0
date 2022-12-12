package com.example.drshceduler;

public class User {
    private String StudentID;
    private String FullName;
    private String Faculty;
    private String Specialty;
    private String Group;
    private String Term;
    private String FormOfEducation;
    private String TermOfEducation;
    private String Email;
    private String UserID;
    private String Language;
    private boolean ThemeDark;

    public User() {
    }


    public void setEmail(String email) {
        Email = email;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getUserID() {
        return UserID;
    }

    public String getFullName() {
        return FullName;
    }

    public String getStudentID() {
        return StudentID;
    }

    public String getFaculty() {
        return Faculty;
    }

    public String getSpecialty() {
        return Specialty;
    }

    public String getGroup() {
        return Group;
    }

    public String getTerm() {
        return Term;
    }

    public String getFormOfEducation() {
        return FormOfEducation;
    }

    public String getTermOfEducation() {
        return TermOfEducation;
    }

    public String getEmail() {
        return Email;
    }

    public String getLanguage() {
        return Language;
    }

    public boolean isThemeDark() {
        return ThemeDark;
    }

}
