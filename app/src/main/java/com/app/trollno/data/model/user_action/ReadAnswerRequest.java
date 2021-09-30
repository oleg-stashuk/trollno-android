package com.app.trollno.data.model.user_action;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReadAnswerRequest {
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("id")
    @Expose
    private String id;

    public ReadAnswerRequest(boolean isReadAnswerToOneComment, String id) {
        this.type = isReadAnswerToOneComment ? "comment": "user";
        this.id = id;
    }
}
