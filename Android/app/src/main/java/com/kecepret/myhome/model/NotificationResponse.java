package com.kecepret.myhome.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NotificationResponse {

    @SerializedName("count")
    private Integer count;
    @SerializedName("next")
    private String next;
    @SerializedName("total")
    private String total;
    @SerializedName("results")
    private List<Notification> results;

    public List<Notification> getResults() {
        return results;
    }

    public void setResults(List<Notification> results) {
        this.results = results;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
