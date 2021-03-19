package com.apps.trollino.data.model.login;

import com.apps.trollino.data.model.profile.ShowNameModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
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

    @SerializedName("field_showname")
    @Expose
    List<ShowNameModel> showNameModelsList = new ArrayList<>();

    public RegistrationRequestModel(List<String> login, List<String> mail, List<String> password) {
        this.login = login;
        this.mail = mail;
        this.password = password;
    }


//    public RegistrationRequestModel(List<String> login, List<String> mail, List<String> password, String showName) {
//        this.login = login;
//        this.mail = mail;
//        this.password = password;
//        showNameModelsList.add(new ShowNameModel(showName));
//    }
}
