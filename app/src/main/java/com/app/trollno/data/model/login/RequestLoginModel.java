package com.app.trollno.data.model.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestLoginModel {
    @SerializedName("name")
    @Expose
    private String login;

    @SerializedName("pass")
    @Expose
    private String pass;

    @SerializedName("mail")
    @Expose
    private String mail;

    public RequestLoginModel(String login, String pass) {
        this.login = login;
        this.pass = pass;
    }

    public RequestLoginModel(String mail) {
        this.mail = mail;
    }
}
