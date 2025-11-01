package com.example.easyquiz.model;

public class Classroom {
    private int class_id;
    private String class_name;
    private int teacher_id;

    public Classroom() {
    }

    public Classroom(int class_id, String class_name, int teacher_id) {
        this.class_id = class_id;
        this.class_name = class_name;
        this.teacher_id = teacher_id;
    }

    // Getters and Setters
    public int getClass_id() {
        return class_id;
    }

    public void setClass_id(int class_id) {
        this.class_id = class_id;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public int getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(int teacher_id) {
        this.teacher_id = teacher_id;
    }
}