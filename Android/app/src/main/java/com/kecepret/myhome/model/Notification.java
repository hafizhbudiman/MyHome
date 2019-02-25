package com.kecepret.myhome.model;

public class Notification {

    private int tipe;
    private int nominal;
    private String token;

    public Notification(int nominal, String token, int tipe) {
        this.nominal = nominal;
        this.token = token;
        this.tipe = tipe;
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

    public int getTipe() {
        return tipe;
    }

    public void setTipe(int tipe) {
        this.tipe = tipe;
    }
}
