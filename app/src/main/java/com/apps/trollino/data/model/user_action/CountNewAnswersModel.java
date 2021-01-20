package com.apps.trollino.data.model.user_action;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CountNewAnswersModel {
    @SerializedName("new_answers")
    @Expose
    private String countNewAnswer;

    public String getCountNewAnswer() {
        return countNewAnswer;
    }
}
