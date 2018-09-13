package com.android.zakaria.classmateinfo.models;

public class StudentInfo {
    private String name, id, email, phone;
    private int image;

    public StudentInfo(String name, String id, String email, String phone, int image) {
        this.name = name;
        this.id = id;
        this.email = email;
        this.phone = phone;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public int getImage() {
        return image;
    }
}
