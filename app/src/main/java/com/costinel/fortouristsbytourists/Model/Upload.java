package com.costinel.fortouristsbytourists.Model;

public class Upload {
    private String mName;
    private String mImageUrl;
    private String nLocation;
    private String nDescription;
    private String nPrice;

    // making an Upload class to create objects of attractions to upload to Firebase;
    // this constructor will be used to write data to firebase;

    public Upload(String name, String imageUrl, String location, String description, String price) {
        if (name.trim().equals("")) {
            name = "No Name";
        }

        mName = name;
        mImageUrl = imageUrl;
        nLocation = location;
        nDescription = description;
        nPrice = price;
    }

    // empty constructor to be used to read the data from firebase;
    public Upload() {

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
}
