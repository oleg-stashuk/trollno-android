package com.apps.trollino.data.model.profile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RequestUpdateSentPushNewAnswers {
    @SerializedName("field_user_comment_answers")
    @Expose
    private List<UserProfileModel.UserBooleanData> updateSendPushNewAnswersList;

    public RequestUpdateSentPushNewAnswers(List<UserProfileModel.UserBooleanData> updateSendPushNewAnswersList) {
        this.updateSendPushNewAnswersList = updateSendPushNewAnswersList;
    }
}
