package com.godlife.churchapp.godlifeassembly.models;

public class UserModel {
    private String names,email,address,gender, mStatus, device_token,uid;

    public UserModel() {
    }

    public UserModel(String names, String email, String address, String gender, String mStatus, String device_token, String uid) {
        this.names = names;
        this.email = email;
        this.address = address;
        this.gender = gender;
        this.mStatus = mStatus;
        this.device_token = device_token;
        this.uid = uid;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getmStatus() {
        return mStatus;
    }

    public void setmStatus(String mStatus) {
        this.mStatus = mStatus;
    }

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
