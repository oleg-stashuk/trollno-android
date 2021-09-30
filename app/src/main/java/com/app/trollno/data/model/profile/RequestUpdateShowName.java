package com.app.trollno.data.model.profile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class RequestUpdateShowName {
    @SerializedName("field_showname")
    @Expose
    List<ShowNameModel> showNameModelsList = new ArrayList<>();

    public RequestUpdateShowName(String showName) {
        showNameModelsList.add(new ShowNameModel(showName));
    }
}
