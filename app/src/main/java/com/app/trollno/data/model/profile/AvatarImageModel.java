package com.app.trollno.data.model.profile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AvatarImageModel {
    @SerializedName("target_id")
    @Expose
    private String avatarId;

    @SerializedName("url")
    @Expose
    private String avatarUrl;

    public AvatarImageModel(String avatarId) {
        this.avatarId = avatarId;
    }


    public String getAvatarId() {
        return avatarId;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }
}
