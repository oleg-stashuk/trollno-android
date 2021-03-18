package com.apps.trollino.data.model.profile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RequestUpdateShowReadPosts {
    @SerializedName("field_show_read_posts")
    @Expose
    private List<UserProfileModel.UserBooleanData> updateShowReadPostsList;

    public RequestUpdateShowReadPosts(List<UserProfileModel.UserBooleanData> updateShowReadPostsList) {
        this.updateShowReadPostsList = updateShowReadPostsList;
    }
}
