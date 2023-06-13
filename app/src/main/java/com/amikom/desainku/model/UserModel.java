package com.amikom.desainku.model;

import java.io.Serializable;

public class UserModel implements Serializable {
    private String name;
    private String phoneNumber;
    private String email;
    private String photoUrl;
    private String dateCreated;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    private String userType;


    public UserModel(String name, String phoneNumber, String email, String photoUrl, String dateCreated, String userType) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.photoUrl = photoUrl;
        this.dateCreated = dateCreated;
        this.userType = userType;
    }
}
