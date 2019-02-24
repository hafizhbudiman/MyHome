package com.kecepret.myhome.model;

import com.google.gson.annotations.SerializedName;

public class Register {

    @SerializedName("username")
    public String username;
    @SerializedName("email")
    public String email;
    @SerializedName("password")
    public String password;
    @SerializedName("name")
    public String name;
    @SerializedName("address")
    public String address;
    @SerializedName("google_id")
    public String google_id;
    @SerializedName("phone")
    public String phone;

    public Register(String username, String email, String password, String name, String address, String google_id, String phone) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.name = name;
        this.address = address;
        this.google_id = google_id;
        this.phone = phone;
    }

}
