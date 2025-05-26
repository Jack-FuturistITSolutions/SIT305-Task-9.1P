package com.example.task91p;

public class Advert {
    private int id;
    private String title;
    private String type;
    private String name;
    private String phone;
    private String description;
    private String date;
    private String location;

    // Constructor
    public Advert(int id, String title, String type, String name, String phone, String description, String date, String location) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.name = name;
        this.phone = phone;
        this.description = description;
        this.date = date;
        this.location = location;
    }

    // Getters
    public int getId() {
        return id;
    }
    public String getTitle() { return title; }
    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

}