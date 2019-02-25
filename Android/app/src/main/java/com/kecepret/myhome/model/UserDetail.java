package com.kecepret.myhome.model;

import com.google.gson.annotations.SerializedName;

public class UserDetail {

    @SerializedName("email")
    private String email;
    @SerializedName("username")
    private String username;
    @SerializedName("name")
    private String name;
    @SerializedName("phone")
    private String phone;
    @SerializedName("address")
    private String address;

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }
}
