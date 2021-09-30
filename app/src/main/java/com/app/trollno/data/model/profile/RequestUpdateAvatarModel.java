package com.app.trollno.data.model.profile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RequestUpdateAvatarModel {
    @SerializedName("field_user_picture")
    @Expose
    private List<AvatarImageModel> avatarImageList;

    public RequestUpdateAvatarModel(List<AvatarImageModel> avatarImageList) {
        this.avatarImageList = avatarImageList;
    }
}
