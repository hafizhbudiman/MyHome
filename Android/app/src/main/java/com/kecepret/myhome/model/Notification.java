package com.kecepret.myhome.model;

public class Notification {

    private int nominal;
    private String token;

    public Notification(int nominal, String token) {
        this.nominal = nominal;
        this.token = token;
    }

    public Notification() {

    }

    public int getNominal() {
        return nominal;
    }

    public void setNominal(int nominal) {
        this.nominal = nominal;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
