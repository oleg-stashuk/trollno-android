package com.app.trollno.data.model.profile;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RequestPushNotificationToken {
    @SerializedName("field_device_token")
    private List<PushToken> pushTokenList;

    public RequestPushNotificationToken(List<PushToken> pushTokenList) {
        this.pushTokenList = pushTokenList;
    }


    public static class PushToken {
        @SerializedName("value")
        private String pushToken;

        public PushToken(String pushToken) {
            this.pushToken = pushToken;
        }
    }
}
