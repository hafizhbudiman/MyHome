package com.kecepret.myhome.model;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("password")
    public String password;
    @SerializedName("username")
    public String username;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

}
