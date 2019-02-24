package com.kecepret.myhome.model;

import com.google.gson.annotations.SerializedName;

public class ResponseBE {

    @SerializedName("success")
    public Boolean success;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

}