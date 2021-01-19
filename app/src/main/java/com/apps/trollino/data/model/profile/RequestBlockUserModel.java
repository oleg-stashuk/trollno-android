package com.apps.trollino.data.model.profile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class RequestBlockUserModel {
    @SerializedName("field_delete_me")
    @Expose
    private List<StatusBlockUserModel> userStatus;

    public RequestBlockUserModel() {
        List<StatusBlockUserModel> userStatus = new ArrayList<>();
        userStatus.add(new StatusBlockUserModel());
        this.userStatus = userStatus;

    }

    private class StatusBlockUserModel {
        @SerializedName("value")
        @Expose
        private int statusValue;

        public StatusBlockUserModel() {
            this.statusValue = 1;
        }
    }
}
