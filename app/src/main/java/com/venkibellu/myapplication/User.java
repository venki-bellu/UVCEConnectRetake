package com.venkibellu.myapplication;


public class User {
    private String key, name, email, phone, status, yearOfJoining, branch;

    User(String name, String key, String phone) {
        this.name = name;
        this.key = key;
        this.phone = phone;
    }

    void setCompleteInformation(String email, String branch, String status, String year) {
        this.email = email;
        this.branch = branch;
        this.status = status;
        this.yearOfJoining = year;
    }

    String getKey() {
        return key;
    }

    String getName() {
        return name;
    }

    String getEmail() {
        return email;
    }

    String getPhone() {
        return phone;
    }

    String getStatus() {
        return status;
    }

    String getYearOfJoining() {
        return yearOfJoining;
    }

    String getBranch() {
        return branch;
    }
}
