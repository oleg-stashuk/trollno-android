package com.apps.trollino.data.model.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RegistrationRequestModel {
    @SerializedName("name")
    @Expose
    List<String> login;

    @SerializedName("mail")
    @Expose
    List<String> mail;

    @SerializedName("pass")
    @Expose
    List<String> password;


    public RegistrationRequestModel(List<String> login, List<String> mail, List<String> password) {
        this.login = login;
        this.mail = mail;
        this.password = password;
    }
}
