package com.apps.trollino.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserProfileModel {
    @SerializedName("uid")
    @Expose
    private List<UserData> uidList;
    @SerializedName("name")
    @Expose
    private List<UserData> nameList;
    @SerializedName("mail")
    @Expose
    private List<UserData> mailList;
    @SerializedName("field_user_picture")
    @Expose
    private List<UserImage> userImageList;

    public List<UserData> getUidList() {
        return uidList;
    }

    public List<UserData> getNameList() {
        return nameList;
    }

    public List<UserData> getMailList() {
        return mailList;
    }

    public List<UserImage> getUserImageList() {
        return userImageList;
    }


    public class UserData {
        @SerializedName("value")
        @Expose
        private int value;

        public int getValue() {
            return value;
        }
    }


    public class UserImage {
        @SerializedName("target_id")
        @Expose
        private int imageId;
        @SerializedName("url")
        @Expose
        private String imageUrl;

        public int getImageId() {
            return imageId;
        }

        public String getImageUrl() {
            return imageUrl;
        }
    }

    /*
    ""field_user_comment_answers"": [
        {
            ""value"": false
        }
    ],
     */
}
