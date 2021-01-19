package com.apps.trollino.data.model.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseLoginModel {
    @SerializedName("current_user")
    @Expose
    private User currentUser;

    @SerializedName("csrf_token")
    @Expose
    private String token;

    @SerializedName("logout_token")
    @Expose
    private String logoutToken;

    public User getCurrentUser() {
        return currentUser;
    }

    public String getToken() {
        return token;
    }

    public String getLogoutToken() {
        return logoutToken;
    }

    public class User {
        @SerializedName("uid")
        @Expose
        String uid;

        @SerializedName("name")
        @Expose
        String userName;

        public String getUid() {
            return uid;
        }

        public String getUserName() {
            return userName;
        }
    }

}
