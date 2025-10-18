package com.example.easyquiz.model;

import java.io.Serializable;

public class User implements Serializable {

    private int user_id;
    private String user_name;      // Đã đổi tên
    private String email;
    private String password;
    private String role;
    private double average_score;

    public User() {}

    public User(int user_id, String user_name, String email, String password, String role) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.average_score = 0.0;
    }

    public User(int user_id, String user_name, String email, String password, String role, double average_score) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.average_score = average_score;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public double getAverage_score() {
        return average_score;
    }

    public void setAverage_score(double average_score) {
        this.average_score = average_score;
    }

    @Override
    public String toString() {
        return "User{" +
                "user_id=" + user_id +
                ", user_name='" + user_name + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", average_score=" + average_score +
                '}';
    }
}
