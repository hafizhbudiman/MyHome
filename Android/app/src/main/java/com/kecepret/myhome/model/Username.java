package com.kecepret.myhome.model;

import com.google.gson.annotations.SerializedName;

public class Username {

    @SerializedName("username")
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Username(String username) {
        this.username = username;
    }
}
