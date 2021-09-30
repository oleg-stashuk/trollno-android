package com.app.trollno.data.model.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FacebookRequestModel {
    @SerializedName("access_token")
    @Expose
    private String facebookToken;

    public FacebookRequestModel(String facebookToken) {
        this.facebookToken = facebookToken;
    }
}
