package com.kecepret.myhome.model;

import com.google.gson.annotations.SerializedName;

public class Lamp {

    @SerializedName("id")
    public int id;
    @SerializedName("username")
    public String username;

    public Lamp(String username, int id) {
        this.username = username;
        this.id = id;
    }
}
