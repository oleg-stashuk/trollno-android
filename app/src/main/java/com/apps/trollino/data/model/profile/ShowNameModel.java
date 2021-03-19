package com.apps.trollino.data.model.profile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShowNameModel {
    @SerializedName("value")
    @Expose
    private String value;

    public ShowNameModel(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
