package com.costinel.fortouristsbytourists.Model;

import android.net.Uri;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Attraction {
    private String mName;
    private String mImageUrl;
    private String nLocation;
    private String nDescription;
    private String nPrice;
    //Using a Map interface to store multiple images for the upload attraction method.
    private List<Uri> attractionImages;

    // making an Upload class to create objects of attractions to upload to Firebase;
    // this constructor will be used to write data to firebase;

    public Attraction(String name, String location, String description,
                      String price, List<Uri> image) {
        if (name.trim().equals("")) {
            name = "No Name";
        }
        if (description.trim().equals("")){
            description = "No Description";
        }
        if (price.trim().equals("")){
            price = "Price unknown";
        }


        this.mName = name;
        this.nLocation = location;
        this.nDescription = description;
        this.nPrice = price;
        this.mImageUrl = "N/A";
        this.attractionImages = new ArrayList<Uri>();

    }

    // empty constructor to be used to read the data from firebase;
    public Attraction() {

    }


    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    public String getnLocation() {
        return nLocation;
    }

    public void setnLocation(String nLocation) {
        this.nLocation = nLocation;
    }

    public String getnDescription() {
        return nDescription;
    }

    public void setnDescription(String nDescription) {
        this.nDescription = nDescription;
    }

    public String getnPrice() {
        return nPrice;
    }

    public void setnPrice(String nPrice) {
        this.nPrice = nPrice;
    }

    public List<Uri> getAttractionImages() {
        return new ArrayList<Uri>(attractionImages);
    }
}
