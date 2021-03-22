package com.apps.trollino.data.model.login;

import com.apps.trollino.data.model.profile.ShowNameModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class RegistrationRequestModel {
    @SerializedName("name")
    @Expose
    List<String> loginList = new ArrayList<>();

    @SerializedName("mail")
    @Expose
    List<String> mailList = new ArrayList<>();

    @SerializedName("pass")
    @Expose
    List<String> passwordList = new ArrayList<>();

    @SerializedName("field_showname")
    @Expose
    List<ShowNameModel> showNameModelsList = new ArrayList<>();

    public RegistrationRequestModel(String login, String mail, String password, String showName) {
        loginList.add(login);
        mailList.add(mail);
        passwordList.add(password);
        showNameModelsList.add(new ShowNameModel(showName));
    }
}
