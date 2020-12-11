package com.apps.trollino.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RequestBlockUserModel {
    @SerializedName("status")
    @Expose
    private List<StatusBlockUserModel> userStatus;

    public RequestBlockUserModel(List<StatusBlockUserModel> userStatus) {
        this.userStatus = userStatus;
    }

    public static class StatusBlockUserModel {
        @SerializedName("value")
        @Expose
        private boolean statusValue;


        public StatusBlockUserModel(boolean statusValue) {
            this.statusValue = statusValue;
        }
    }
}
