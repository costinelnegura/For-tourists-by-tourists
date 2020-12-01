package com.costinel.fortouristsbytourists.Model;

public class Users {
    String firstName;
    String lastName;
    String password;
    String email;

    // making an User class to create user objects to push user information to Firebase,
    // and therefore allowing users to log in;
    // this constructor will be used to write data to firebase;
    public Users(String firstName, String lastName, String password, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
    }

    // empty constructor to be used to read the data from firebase;
    public Users() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
