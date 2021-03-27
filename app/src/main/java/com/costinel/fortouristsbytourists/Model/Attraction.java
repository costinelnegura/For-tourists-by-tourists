package com.costinel.fortouristsbytourists.Model;

import java.util.List;

public class Attraction {
    private String Name;
    private String Location;
    private String Description;
    private String Price;

    // making an Upload class to create objects of attractions to upload to Firebase;
    // this constructor will be used to write data to firebase;

    public Attraction(String name, String location, String description, String price) {
        this.Name = name;
        this.Location = location;
        this.Description = description;
        this.Price = price;
    }

    // empty constructor to be used to read the data from firebase;
    public Attraction() {

    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        this.Location = location;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        this.Description = description;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        this.Price = price;
    }
}
