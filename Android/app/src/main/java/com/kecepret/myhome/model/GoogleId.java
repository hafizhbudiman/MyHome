package com.kecepret.myhome.model;

import com.google.gson.annotations.SerializedName;

public class GoogleId {

    @SerializedName("google_id")
    public String google_id;

    public GoogleId(String google_id) {
        this.google_id = google_id;
    }
}
