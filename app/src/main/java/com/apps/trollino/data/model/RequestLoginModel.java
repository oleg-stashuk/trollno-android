package com.apps.trollino.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestLoginModel {
    @SerializedName("name")
    @Expose
    private String login;

    @SerializedName("pass")
    @Expose
    private String pass;


    public RequestLoginModel(String login, String pass) {
        this.login = login;
        this.pass = pass;
    }
}